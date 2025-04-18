package com.vgb;

import com.vgb.db.InvoiceDAO;
import com.vgb.db.CompanyDAO;
import com.vgb.db.PersonDAO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class InvoiceReport {
    public static void main(String[] args) {
        // 1) Redirect output
        try {
            PrintStream fileOut = new PrintStream(new FileOutputStream("data/output.txt"));
            TeePrintStream tee = new TeePrintStream(System.out, fileOut);
            System.setOut(tee);
        } catch (IOException e) {
            System.err.println("Could not redirect output: " + e.getMessage());
        }

        try {
            // 2) Load via DAOs
            List<Invoice> invoices      = InvoiceDAO.loadAll();
            List<Companies> companiesList = CompanyDAO.loadAll();
            List<Person> personsList    = PersonDAO.loadAll();

            // 3a) Build ID‑keyed map for CompanyReport
            Map<Integer, Companies> companiesById = new HashMap<>();
            for (Companies c : companiesList) {
                companiesById.put(c.getCompanyId(), c);
            }

            // 3b) Build UUID‑string‑keyed map for SummaryReport & DetailedInvoiceReport
            Map<String, Companies> companiesByUuid = new HashMap<>();
            for (Companies c : companiesList) {
                companiesByUuid.put(c.getCompanyUuid().toString(), c);
            }

            // 3c) Build person UUID‑string‑keyed map for DetailedInvoiceReport
            Map<String, Person> personsByUuid = new HashMap<>();
            for (Person p : personsList) {
                // adjust to match your getter name (getPersonUuid() or getUuid())
                personsByUuid.put(p.getUuid().toString(), p);
            }

            // 4) Invoke reports with matching signature

            // SummaryReport wants (List<Invoice>, Map<String,Companies>)
            SummaryReport summary = new SummaryReport();
            summary.printReport(invoices, companiesByUuid);

            // CompanyReport wants (List<Invoice>, Map<Integer,Companies>)
            CompanyReport companyRpt = new CompanyReport();
            companyRpt.printReport(invoices, companiesById);

            // DetailedInvoiceReport wants (List<Invoice>, Map<String,Companies>, Map<String,Person>)
            DetailedInvoiceReport detail = new DetailedInvoiceReport();
            detail.printReport(invoices, companiesByUuid, personsByUuid);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}