package com.vgb;

import com.vgb.Invoice;
import com.vgb.Item;
import java.sql.*;
import java.util.*;

public class InvoiceDAO {
    private static final String SEL_ALL =
      "SELECT invoice_id, date, customer, salesperson FROM Invoice";
    private static final String SEL_ITEMS =
      "SELECT item_id, description, quantity, unit_price, item_type "
    + "FROM InvoiceItem WHERE invoice_id = ?";

    public static List<Invoice> loadAll() throws Exception {
        List<Invoice> list = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_ALL);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next()) {
                Invoice inv = mapRowToInvoice(rs);
                inv.getItems().addAll(loadItemsFor(inv.getInvoiceId()));
                list.add(inv);
            }
        }
        return list;
    }

    private static Invoice mapRowToInvoice(ResultSet rs) throws SQLException {
        Invoice inv = new Invoice();
        inv.setInvoiceId(UUID.fromString(rs.getString("invoice_id")));
        inv.setDate(rs.getDate("date"));
        inv.setCustomer(rs.getString("customer"));
        inv.setSalesPerson(rs.getString("salesperson"));
        return inv;
    }

    private static List<Item> loadItemsFor(UUID invoiceId) throws Exception {
        List<Item> items = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement ps = c.prepareStatement(SEL_ITEMS))
        {
            ps.setString(1, invoiceId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID itemId      = UUID.fromString(rs.getString("item_id"));
                    String desc      = rs.getString("description");
                    int qty          = rs.getInt("quantity");
                    double price     = rs.getDouble("unit_price");
                    String type      = rs.getString("item_type");
                    Item item = ItemFactory.create(type, itemId, desc, qty, price);
                    items.add(item);
                }
            }
        }
        return items;
    }
}
