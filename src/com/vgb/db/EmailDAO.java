package com.vgb.db;

import com.vgb.Email;
import java.sql.*;
import java.util.*;

public class EmailDAO {
    private static final String SEL_BY_PERSON =
      "SELECT email_uuid, email_address FROM Email WHERE person_id = ?";

    public static List<Email> loadByPersonId(int personId) throws Exception {
        List<Email> list = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_BY_PERSON)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Email(
                        UUID.fromString(rs.getString("email_uuid")),
                        rs.getString("email_address")
                    ));
                }
            }
        }
        return list;
    }
}
