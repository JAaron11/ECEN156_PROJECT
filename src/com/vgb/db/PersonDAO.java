package com.vgb.db;

import com.vgb.Person;
import com.vgb.Address;
import com.vgb.Email;

import java.sql.*;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class PersonDAO {
	private static final String SEL_BY_ID = "SELECT person_uuid, first_name, last_name, role "
			+ "FROM Person WHERE person_id = ?";

	public static Person loadById(int personId) throws Exception {
		// 1) Include address_id in the SELECT
		final String SEL_BY_ID = "SELECT person_uuid, first_name, last_name, role, address_id "
				+ "FROM Person WHERE person_id = ?";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SEL_BY_ID)) {

			ps.setInt(1, personId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;

				// 2) Load emails
				List<Email> emailObjs = EmailDAO.loadByPersonId(personId);
				List<String> emailAddrs = new ArrayList<>();
				for (Email e : emailObjs) {
					emailAddrs.add(e.getEmailAddress());
				}

				// 3) Load address_id and fetch Address
				int addrId = rs.getInt("address_id");
				Address addr = null;
				if (!rs.wasNull()) {
					addr = AddressDAO.loadById(addrId);
				}

				// 4) Construct Person with address
				Person p = new Person(UUID.fromString(rs.getString("person_uuid")), rs.getString("first_name"),
						rs.getString("last_name"), rs.getString("role"), emailAddrs);
				p.setAddress(addr); // assuming you have a setter
				return p;
			}
		}
	}

	public static List<Person> loadAll() throws Exception {
        // 1) include address_id
        String sql = "SELECT person_id, person_uuid, first_name, last_name, role, address_id "
                   + "FROM Person";

        List<Person> people = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("person_id");

                // 2) load & map emails
                List<Email> emailObjs   = EmailDAO.loadByPersonId(id);
                List<String> emailAddrs = new ArrayList<>();
                for (Email e : emailObjs) {
                    emailAddrs.add(e.getEmailAddress());
                }

                // 3) load & map address
                int addrId = rs.getInt("address_id");
                Address addr = null;
                if (!rs.wasNull()) {
                    addr = AddressDAO.loadById(addrId);
                }

                // 4) construct Person
                Person p = new Person(
                    UUID.fromString(rs.getString("person_uuid")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("role"),
                    emailAddrs
                );

                // 5) attach Address
                p.setAddress(addr);

                people.add(p);
            }
        }
        return people;
    }
}