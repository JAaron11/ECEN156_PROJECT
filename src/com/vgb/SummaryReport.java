package com.vgb;

import java.util.List;
import java.util.Map;

public class SummaryReport {
    /**
     * Prints the summary report of invoices.
     * @param invoices  List of Invoice objects.
     * @param companies Map from company‐UUID to Company.
     */
    public void printReport(List<Invoice> invoices, Map<String,Companies> companies) {
        System.out.println("+---------------------------------------------------------------------------------------------+");
        System.out.println("|                               SummaryReport ‑ By Total                                      |");
        System.out.println("+---------------------------------------------------------------------------------------------+");

        System.out.printf("%-36s  %-30s  %-10s  %-10s  %-10s%n",
                          "Invoice #", "Customer", "NumItems", "Tax", "Total");

        int  overallItems = 0;
        double overallTax   = 0;
        double overallTotal = 0;

        for (Invoice inv : invoices) {
            String custUuid = inv.getCustomer();  
            Companies comp    = companies.get(custUuid);
            String name     = (comp != null)
                                ? comp.getName()
                                : custUuid;

            int    numItems = inv.getItems().size();
            double tax      = inv.getTaxTotal();
            double total    = inv.getGrandTotal();

            overallItems += numItems;
            overallTax   += tax;
            overallTotal += total;

            System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n",
                              inv.getInvoiceId().toString(),
                              name,
                              numItems,
                              tax,
                              total);
        }

        System.out.println("+---------------------------------------------------------------------------------------------+");
        // Print overall totals row (label in first two columns)
        System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n",
                          "",    // blank UUID
                          "Overall Totals:",
                          overallItems,
                          overallTax,
                          overallTotal);
    }
}