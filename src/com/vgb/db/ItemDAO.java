package com.vgb.db;

import com.vgb.*;
import java.sql.*;
import java.util.*;

public class ItemDAO {
	private static final String SEL_ALL = "SELECT item_uuid, item_type, description, base_cost FROM Item";

	public static List<Item> loadAll() throws Exception {
		List<Item> list = new ArrayList<>();
		try (Connection c = DatabaseUtils.getConnection();
				PreparedStatement ps = c.prepareStatement(SEL_ALL);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(ItemFactory.create(rs.getString("item_type"), UUID.fromString(rs.getString("item_uuid")),
						rs.getString("description"), 0, // qty=0 for catalog
						rs.getDouble("base_cost")));
			}
		}
		return list;
	}
}
