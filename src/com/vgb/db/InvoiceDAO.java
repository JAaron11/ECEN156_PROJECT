package com.vgb.db;

import com.vgb.Companies;
import com.vgb.Invoice;
import com.vgb.Item;
import com.vgb.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceDAO {
    // 1) Select the PK
    private static final String SEL_INV =
      "SELECT invoice_id, invoice_uuid, customer_company_id, salesperson_id, invoice_date "
    + "  FROM Invoice";

    // 2) Use the integer PK to fetch its lines
    private static final String SEL_LINES =
      "SELECT il.line_uuid, il.quantity, "
    + "       it.item_uuid, it.item_type, it.description, it.base_cost, "
    + "       ms.unit_price, cs.vendor_company_id, cs.contract_terms, "
    + "       rs.hours, rs.hourly_rate, "
    + "       ls.start_date, ls.end_date, ls.lease_rate, "
    + "       es.daily_rate, es.equipment_size "
    + "  FROM InvoiceLine il "
    + "  JOIN Item it ON il.item_id = it.item_id "
    + "  LEFT JOIN MaterialSpec  ms ON il.line_id = ms.line_id "
    + "  LEFT JOIN ContractSpec  cs ON il.line_id = cs.line_id "
    + "  LEFT JOIN RentalSpec    rs ON il.line_id = rs.line_id "
    + "  LEFT JOIN LeaseSpec     ls ON il.line_id = ls.line_id "
    + "  LEFT JOIN EquipmentSpec es ON il.line_id = es.line_id "
    + " WHERE il.invoice_id = ?";

    public static List<Invoice> loadAll() throws Exception {
        List<Invoice> invoices = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement psInv = conn.prepareStatement(SEL_INV);
             ResultSet rsInv = psInv.executeQuery())
        {
            while (rsInv.next()) {
                int sqlId  = rsInv.getInt("invoice_id");
                int custId = rsInv.getInt("customer_company_id");
                int spId   = rsInv.getInt("salesperson_id");

                // Fetch the company and person once
                Companies comp = CompanyDAO.loadById(custId);
                Person    sp   = PersonDAO.loadById(spId);

                Invoice inv = new Invoice();

                inv.setCustomer(comp.getCompanyUuid().toString());
                inv.setSalesPerson(sp.getUuid().toString());

                inv.setCustomerCompanyId(custId);
                inv.setInvoiceId(UUID.fromString(rsInv.getString("invoice_uuid")));
                inv.setDate(rsInv.getDate("invoice_date"));

                // now load the lines by PK
                try (PreparedStatement psLine = conn.prepareStatement(SEL_LINES)) {
                    psLine.setInt(1, sqlId);
                    try (ResultSet rsLine = psLine.executeQuery()) {
                        while (rsLine.next()) {
                            Item item = ItemFactory.create(
                                rsLine.getString("item_type"),
                                UUID.fromString(rsLine.getString("item_uuid")),
                                rsLine.getString("description"),
                                rsLine.getInt("quantity"),
                                rsLine.getDouble("base_cost"),
                                rsLine.getObject("unit_price", Double.class),
                                rsLine.getObject("vendor_company_id", Integer.class),
                                rsLine.getObject("hours", Double.class),
                                rsLine.getObject("hourly_rate", Double.class),
                                rsLine.getDate("start_date"),
                                rsLine.getDate("end_date"),
                                rsLine.getObject("lease_rate", Double.class),
                                rsLine.getObject("daily_rate", Double.class),
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
