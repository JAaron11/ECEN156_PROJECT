package com.vgb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vgb.Companies;

/**
 * CompanyReport generates a summary of invoice counts and totals grouped by company.
 * <p>
 * Invoices are grouped by their customer‐UUID, and the report is sorted by company name
 * (case‑insensitive) then by UUID to break ties.
 * </p>
 */
public class CompanyReport {

    /**
     * Prints the company invoice summary report.
     *
     * @param invoices     all invoices to include
     * @param companiesById map of company UUID → Companies object
     */
    public static void printReport(
        List<Invoice> invoices,
        Map<Integer, Companies> companiesById
    ) {
        // 1) Group invoices by customer‑UUID
        Map<UUID, List<Invoice>> byCompany = invoices.stream()
            .collect(Collectors.groupingBy(Invoice::getCustomerUuid));

        // 2) Sort keys by (companyName, then UUID)
        Comparator<UUID> cmp = Comparator
            .comparing((UUID u) -> {
                Companies c = companiesById.get(u);
                return c != null ? c.getName() : "";
            }, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(uuid -> uuid);

        // 3) Print header
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|                      Company Invoice Summary Report                             |");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.printf("%-36s  %-30s  %8s  %12s%n",
                          "Company UUID", "Company Name", "#Invoices", "Grand Total");
        System.out.println("+---------------------------------------------------------------------------------+");

        // 4) Iterate in sorted order
        int totalInvoices = 0;
        double grandTotal = 0.0;
        for (UUID companyUuid : byCompany.keySet().stream().sorted(cmp).toList()) {
            List<Invoice> list = byCompany.get(companyUuid);
            Companies comp = companiesById.get(companyUuid);

            String name = comp != null ? comp.getName() : "<Unknown>";
            int count = list.size();
            double sum = list.stream()
                             .mapToDouble(Invoice::getGrandTotal)
                             .sum();

            System.out.printf("%-36s  %-30s  %8d  %12.2f%n",
                              companyUuid, name, count, sum);

            totalInvoices += count;
            grandTotal += sum;
        }

        // 5) Footer totals
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.printf("%-36s  %-30s  %8d  %12.2f%n",
                          "", "Grand Totals:", totalInvoices, grandTotal);
        System.out.println("+---------------------------------------------------------------------------------+");
    }
}