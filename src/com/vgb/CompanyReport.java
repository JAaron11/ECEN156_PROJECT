package com.vgb;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vgb.SortedList;

public class CompanyReport {

    /**
     * Prints the Company Invoice Summary Report, including companies with zero invoices.
     * Sorted by: total asc → count asc → name (CI) → UUID.
     *
     * @param invoices         all invoices to include
     * @param companiesByUuid  map of company UUID (String) → Companies object
     */
    public static void printReport(
        List<Invoice> invoices,
        Map<String, Companies> companiesByUuid
    ) {
        // 1) Aggregate counts & sums per company UUID
        Map<String, Long> countMap = invoices.stream()
            .collect(Collectors.groupingBy(
                Invoice::getCustomer, Collectors.counting()));
        Map<String, Double> sumMap = invoices.stream()
            .collect(Collectors.groupingBy(
                Invoice::getCustomer,
                Collectors.summingDouble(Invoice::getGrandTotal)));

        // 2) Comparator: total asc, then count asc, then name, then UUID
        Comparator<String> cmp = Comparator
            .comparing((String uuid) -> sumMap.getOrDefault(uuid, 0.0))
            .thenComparing((String uuid) -> countMap.getOrDefault(uuid, 0L))
            .thenComparing((String uuid) -> {
                Companies c = companiesByUuid.get(uuid);
                return c != null ? c.getName() : "";
            }, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(uuid -> uuid);

        // 3) Build a SortedList of all company UUIDs
        SortedList<String> sortedUuids = new SortedList<>(cmp);
        sortedUuids.addAll(companiesByUuid.keySet());

        // 4) Print header
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|                      Company Invoice Summary Report                             |");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.printf("%-36s  %-30s  %8s  %12s%n",
                          "Company UUID", "Company Name", "#Invoices", "Grand Total");
        System.out.println("+---------------------------------------------------------------------------------+");

        // 5) Iterate in sorted order
        int totalInvoices = 0;
        double grandTotal = 0.0;
        for (String uuid : sortedUuids) {
            Companies comp = companiesByUuid.get(uuid);
            String name    = comp != null ? comp.getName() : "<Unknown>";
            long   count   = countMap.getOrDefault(uuid, 0L);
            double sum     = sumMap.getOrDefault(uuid, 0.0);

            System.out.printf("%-36s  %-30s  %8d  $%10.2f%n",
                              uuid, name, count, sum);

            totalInvoices += count;
            grandTotal    += sum;
        }

        // 6) Footer totals
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.printf("%-36s  %-30s  %8d  $%10.2f%n",
                          "", "Grand Totals:", totalInvoices, grandTotal);
        System.out.println("+---------------------------------------------------------------------------------+");
    }
}