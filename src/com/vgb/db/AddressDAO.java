package com.vgb.db;

import com.vgb.Address;
import com.vgb.State;
import com.vgb.ZipCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddressDAO {
    private static final String SEL_BY_ID =
        "SELECT street, city, state_id, zip_id FROM Address WHERE address_id = ?";

    public static Address loadById(int id) throws Exception {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEL_BY_ID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String street = rs.getString("street");
                String city   = rs.getString("city");

                // Load lookup rows
                State  stateObj = StateDAO.loadById(rs.getInt("state_id"));
                ZipCode zipObj   = ZipCodeDAO.loadById(rs.getInt("zip_id"));

                // Pass only street, city, stateCode, zipCode
                return new Address(
                    street,
                    city,
                    stateObj.getStateCode(),
                    zipObj.getZipCode()
                );
            }
        }
    }
}