package com.vgb.db;

import com.vgb.Person;
import com.vgb.Email;

import java.sql.*;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class PersonDAO {
    private static final String SEL_BY_ID =
      "SELECT person_uuid, first_name, last_name, role " +
      "FROM Person WHERE person_id = ?";

    public static Person loadById(int personId) throws Exception {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEL_BY_ID)) {

            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                // 1) Load emails and convert to List<String>
                List<Email> emailObjs   = EmailDAO.loadByPersonId(personId);
                List<String> emailAddrs = new ArrayList<>();
                for (Email e : emailObjs) {
                    emailAddrs.add(e.getEmailAddress());
                }

                // 2) Construct with the five‑arg constructor:
                //    (UUID, firstName, lastName, phoneNumber, emails)
                return new Person(
                    UUID.fromString(rs.getString("person_uuid")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("role"),    // using 'role' as phoneNumber for now
                    emailAddrs
                );
            }
        }
    }

    public static List<Person> loadAll() throws Exception {
        String sql = "SELECT person_id, person_uuid, first_name, last_name, role FROM Person";
        List<Person> people = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("person_id");

                // load & map emails
                List<Email> emailObjs   = EmailDAO.loadByPersonId(id);
                List<String> emailAddrs = new ArrayList<>();
                for (Email e : emailObjs) {
                    emailAddrs.add(e.getEmailAddress());
                }

                // use the five‑arg constructor
                Person p = new Person(
                    UUID.fromString(rs.getString("person_uuid")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("role"),
                    emailAddrs
                );
                people.add(p);
            }
        }
        return people;
    }
}