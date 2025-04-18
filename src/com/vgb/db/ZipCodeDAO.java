package com.vgb.db;

import com.vgb.ZipCode;
import java.sql.*;
import java.util.*;

public class ZipCodeDAO {
    private static final String SEL_BY_ID = "SELECT zip_id, zip_code FROM ZipCode WHERE zip_id = ?";
    private static final String SEL_ALL   = "SELECT zip_id, zip_code FROM ZipCode";

    public static ZipCode loadById(int id) throws Exception {
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new ZipCode(rs.getInt("zip_id"), rs.getString("zip_code"));
            }
        }
    }

    public static List<ZipCode> loadAll() throws Exception {
        List<ZipCode> list = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ZipCode(
                    rs.getInt("zip_id"),
                    rs.getString("zip_code")
                ));
            }
        }
        return list;
    }
}