package com.vgb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InvoiceReport {
    public static void main(String[] args) {
        // Redirect output to data/output.txt so all output goes to that file.
        try {
        	PrintStream fileOut = new PrintStream(new FileOutputStream("data/output.txt"));
        	// Create a TeePrintStream that writes to both System.out and fileOut
        	TeePrintStream tee = new TeePrintStream(System.out, fileOut);
        	System.setOut(tee);
        } catch (IOException e) {
            System.err.println("Could not redirect output: " + e.getMessage());
        }

        try {
            // Load CSV data using CSVDataLoader methods.
            List<Invoice> invoices = CSVDataLoader.parseInvoices("data/Invoices.csv");
            List<Item> itemsLookup = CSVDataLoader.parseItems("data/Items.csv");
            Map<UUID, List<Item>> invoiceItemsMap = CSVDataLoader.parseInvoiceItems("data/InvoiceItems.csv", itemsLookup);
            List<Person> personsList = CSVDataLoader.parsePersons("data/Persons.csv");
            List<Companies> companiesList = CSVDataLoader.parseCompanies("data/Companies.csv");

            // Convert persons and companies lists into maps for easier lookup.
            Map<String, Person> persons = CSVDataLoader.convertPersonsToMap(personsList);
            Map<String, Companies> companies = CSVDataLoader.convertCompaniesToMap(companiesList);

            // Associate invoice items with invoices.
            for (Invoice inv : invoices) {
                List<Item> items = invoiceItemsMap.get(inv.getInvoiceId());
                if (items != null) {
                    for (Item item : items) {
                        inv.addItems(item);
                    }
                }
            }

            // Create report objects and print reports.
            SummaryReport summaryReport = new SummaryReport();
            summaryReport.printReport(invoices, companies);

            CompanyReport companyReport = new CompanyReport();
            CompanyReport.printReport(invoices, companies);

            DetailedInvoiceReport detailedReport = new DetailedInvoiceReport();
            detailedReport.printReport(invoices, companies, persons);

        } catch (IOException e) {
            System.err.println("Error loading CSV data: " + e.getMessage());
        }
    }
}