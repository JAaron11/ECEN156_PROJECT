package com.vgb;

import java.util.List;
import java.util.Map;

public class SummaryReport {
    /**
     * Prints the summary report of invoices.
     * @param invoices List of Invoice objects.
     * @param companies Map of company UUID (as String) to Companies object.
     */
    public void printReport(List<Invoice> invoices, Map<String, Companies> companies) {
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.println("|                             SummaryReport - By Total                                   |");
        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("%-12s %-30s %-15s %-10s %-10s%n", "Invoice #", "Customer", "Num Items", "Tax", "Total");

        int overallItems = 0;
        double overallTax = 0;
        double overallTotal = 0;
        for (Invoice inv : invoices) {
            int numItems = inv.getItems().size();
            double tax = inv.getTaxTotal();
            double total = inv.getGrandTotal();
            overallItems += numItems;
            overallTax += tax;
            overallTotal += total;
            String customerUUID = inv.getCustomer();
            String companyName = companies.containsKey(customerUUID)
                    ? companies.get(customerUUID).getName()
                    : customerUUID;
            System.out.printf("%-12s %-30s %-15d $%10.2f $%10.2f%n", 
                    inv.getInvoiceId(), companyName, numItems, tax, total);
        }
        System.out.println("+----------------------------------------------------------------------------------------+");
        // Print overall totals
        System.out.printf("%-45s %-15d $%10.2f $%10.2f%n", "", overallItems, overallTax, overallTotal);
    }
}