package com.vgb;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CompanyReport generates a summary of invoice counts and totals grouped by company.
 * <p>
 * Invoices are grouped by their customer‑UUID (as a String), and the report is sorted
 * by company name (case‑insensitive) then by UUID to break ties.
 * </p>
 */
public class CompanyReport {

    /**
     * @param invoices         all invoices to include
     * @param companiesByUuid  map of company UUID (String) → Companies object
     */
    public static void printReport(
        List<Invoice> invoices,
        Map<String, Companies> companiesByUuid
    ) {
        // 1) Group invoices by the stored customer UUID string
        Map<String, List<Invoice>> byCompany = invoices.stream()
            .collect(Collectors.groupingBy(Invoice::getCustomer));

        // 2) Sort keys by (companyName, then UUID)
        Comparator<String> cmp = Comparator
            .comparing((String uuid) -> {
                Companies c = companiesByUuid.get(uuid);
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
        for (String companyUuid : byCompany.keySet().stream().sorted(cmp).collect(Collectors.toList())) {
            List<Invoice> list = byCompany.get(companyUuid);
            Companies comp    = companiesByUuid.get(companyUuid);

            String name       = comp != null ? comp.getName() : "<Unknown>";
            int    count      = list.size();
            double sum        = list.stream()
                                     .mapToDouble(Invoice::getGrandTotal)
                                     .sum();

            System.out.printf("%-36s  %-30s  %8d  $%10.2f%n",
                              companyUuid, name, count, sum);

            totalInvoices += count;
            grandTotal    += sum;
        }

        // 5) Footer totals
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.printf("%-36s  %-30s  %8d  $%10.2f%n",
                          "", "Grand Totals:", totalInvoices, grandTotal);
        System.out.println("+---------------------------------------------------------------------------------+");
    }
}
