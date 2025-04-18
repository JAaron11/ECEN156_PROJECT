package com.vgb.db;

import com.vgb.State;
import java.sql.*;
import java.util.*;

public class StateDAO {
    private static final String SEL_BY_ID  = "SELECT state_id, state_code, state_name FROM State WHERE state_id = ?";
    private static final String SEL_ALL    = "SELECT state_id, state_code, state_name FROM State";

    public static State loadById(int id) throws Exception {
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new State(
                    rs.getInt("state_id"),
                    rs.getString("state_code"),
                    rs.getString("state_name")
                );
            }
        }
    }

    public static List<State> loadAll() throws Exception {
        List<State> list = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new State(
                    rs.getInt("state_id"),
                    rs.getString("state_code"),
                    rs.getString("state_name")
                ));
            }
        }
        return list;
    }
}