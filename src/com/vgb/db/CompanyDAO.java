package com.vgb.db;

import com.vgb.Companies;
import com.vgb.Address;
import com.vgb.Person;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyDAO {
	private static final String SEL_BY_ID = "SELECT company_id, company_uuid, company_name, address_id, primary_contact_id "
			+ "FROM Company WHERE company_id = ?";

	public static Companies loadById(int companyId) throws Exception {
		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SEL_BY_ID)) {

			ps.setInt(1, companyId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;

				// 1) Read simple fields
				int id = rs.getInt("company_id");
				UUID uuid = UUID.fromString(rs.getString("company_uuid"));
				String name = rs.getString("company_name");

				// 2) Load address (may be nullable)
				int addrId = rs.getInt("address_id");
				Address address = null;
				if (!rs.wasNull()) {
					address = AddressDAO.loadById(addrId);
				}

				// 3) Load primary contact’s UUID (if set)
				int contactPersonId = rs.getInt("primary_contact_id");
				UUID contactUuid = null;
				if (!rs.wasNull()) {
					Person contact = PersonDAO.loadById(contactPersonId);
					if (contact != null) {
						contactUuid = contact.getUuid();
					}
				}

				// 4) Call the five‑arg constructor
				return new Companies(id, uuid, contactUuid, name, address);
			}
		}
	}

	/**
	 * Bulk‐load all companies so InvoiceReport can call CompanyDAO.loadAll().
	 */
	public static List<Companies> loadAll() throws Exception {
		List<Companies> list = new ArrayList<>();
		// Only need the PK to delegate to loadById
		String sql = "SELECT company_id FROM Company";

		try (Connection conn = DatabaseUtils.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("company_id");
				list.add(loadById(id));
			}
		}
		return list;
	}
}
