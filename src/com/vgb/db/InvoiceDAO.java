package com.vgb.db;

import com.vgb.*;
import java.sql.*;
import java.util.*;

public class InvoiceDAO {
    private static final String SEL_INV = 
      "SELECT invoice_uuid, invoice_date, customer_company_id, salesperson_id "
    + "FROM Invoice";

    private static final String SEL_LINES =
      "SELECT il.line_uuid, il.quantity, "
    + "       it.item_uuid, it.item_type, it.description, it.base_cost, "
    + "       ms.unit_price, cs.vendor_company_id, cs.contract_terms, "
    + "       rs.hours, rs.hourly_rate, "
    + "       ls.start_date, ls.end_date, ls.lease_rate, "
    + "       es.daily_rate, es.equipment_size "
    + "FROM InvoiceLine il "
    + "JOIN Item it         ON il.item_id = it.item_id "
    + "LEFT JOIN MaterialSpec  ms ON il.line_id = ms.line_id "
    + "LEFT JOIN ContractSpec  cs ON il.line_id = cs.line_id "
    + "LEFT JOIN RentalSpec    rs ON il.line_id = rs.line_id "
    + "LEFT JOIN LeaseSpec     ls ON il.line_id = ls.line_id "
    + "LEFT JOIN EquipmentSpec es ON il.line_id = es.line_id "
    + "WHERE il.invoice_id = (SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?)";

    public static List<Invoice> loadAll() throws Exception {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection c = DatabaseUtils.getConnection();
             PreparedStatement psInv = c.prepareStatement(SEL_INV);
             ResultSet rsInv = psInv.executeQuery())
        {
            while (rsInv.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(UUID.fromString(rsInv.getString("invoice_uuid")));
                inv.setDate(rsInv.getDate("invoice_date"));
                inv.setCustomer(CompanyDAO.loadById(rsInv.getInt("customer_company_id")).getName());
                inv.setSalesPerson(PersonDAO.loadById(rsInv.getInt("salesperson_id")).getFullName());

                try (PreparedStatement psLine = c.prepareStatement(SEL_LINES)) {
                    psLine.setString(1, inv.getInvoiceId().toString());
                    try (ResultSet rsLine = psLine.executeQuery()) {
                        while (rsLine.next()) {
                            Item item = ItemFactory.create(
                                rsLine.getString("item_type"),
                                UUID.fromString(rsLine.getString("item_uuid")),
                                rsLine.getString("description"),
                                rsLine.getInt("quantity"),
                                rsLine.getDouble("base_cost"),
                                rsLine.getObject("unit_price",    Double.class),
                                rsLine.getObject("vendor_company_id", Integer.class),
                                rsLine.getObject("hours",          Double.class),
                                rsLine.getObject("hourly_rate",    Double.class),
                                rsLine.getDate("start_date"),
                                rsLine.getDate("end_date"),
                                rsLine.getObject("lease_rate",     Double.class),
                                rsLine.getObject("daily_rate",     Double.class),
                                rsLine.getString("equipment_size")
                            );
                            inv.addItems(item);
                        }
                    }
                }
                invoices.add(inv);
            }
        }
        return invoices;
    }
}
