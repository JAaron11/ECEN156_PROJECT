package com.vgb;

import com.vgb.db.InvoiceDAO;
import com.vgb.db.CompanyDAO;
import com.vgb.db.PersonDAO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class InvoiceReport {
    public static void main(String[] args) {
        // 1) Redirect output to both console and data/output.txt
        try {
            PrintStream fileOut = new PrintStream(new FileOutputStream("data/output.txt"));
            TeePrintStream tee  = new TeePrintStream(System.out, fileOut);
            System.setOut(tee);
        } catch (IOException e) {
            System.err.println("Could not redirect output: " + e.getMessage());
        }

        try {
            // 2) Load via DAOs
            List<Invoice>   invoices      = InvoiceDAO.loadAll();
            List<Companies> companiesList = CompanyDAO.loadAll();
            List<Person>    personsList   = PersonDAO.loadAll();

            // 3a) Build company map
            Map<String, Companies> companiesByUuid = new HashMap<>();
            for (Companies c : companiesList) {
                companiesByUuid.put(c.getCompanyUuid().toString(), c);
            }

            // 4a) SummaryReport
            SummaryReport summary = new SummaryReport();
            summary.printByTotal(invoices, companiesByUuid);
            summary.printByCustomer(invoices, companiesByUuid);

            // 4b) CompanyReport
            CompanyReport companyRpt = new CompanyReport();
            companyRpt.printReport(invoices, companiesByUuid);

            // 4c) DetailedInvoiceReport
            Map<String, Person> personsByUuid = new HashMap<>();
            for (Person p : personsList) {
                personsByUuid.put(p.getUuid().toString(), p);
            }
            DetailedInvoiceReport detail = new DetailedInvoiceReport();
            detail.printReport(invoices, companiesByUuid, personsByUuid);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}