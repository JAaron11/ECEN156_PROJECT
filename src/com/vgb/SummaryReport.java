package com.vgb;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vgb.SortedList;

public class SummaryReport {

	/**
	 * Prints the summary report of invoices, ordered by grand-total descending.
	 *
	 * @param invoices  List of Invoice objects.
	 * @param companies Map from company‑UUID to Company.
	 */
	public void printReport(List<Invoice> invoices, Map<String, Companies> companies) {
		// 1) Build a SortedList<Invoice> ordered by grand‑total desc, then UUID
		Comparator<Invoice> byTotalDesc = Comparator.comparing(Invoice::getGrandTotal).reversed()
				.thenComparing(inv -> inv.getInvoiceId().toString());

		SortedList<Invoice> sorted = new SortedList<>(byTotalDesc);
		sorted.addAll(invoices);

		// 2) Print header
		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.println(
				"|                               SummaryReport - By Total                                      |");
		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.printf("%-36s  %-30s  %-10s  %-10s  %-10s%n", "Invoice #", "Customer", "NumItems", "Tax", "Total");

		// 3) Iterate sorted instead of raw list
		int overallItems = 0;
		double overallTax = 0;
		double overallTotal = 0;

		for (Invoice inv : sorted) {
			String custUuid = inv.getCustomer();
			Companies comp = companies.get(custUuid);
			String name = (comp != null) ? comp.getName() : custUuid;

			int numItems = inv.getItems().size();
			double tax = inv.getTaxTotal();
			double total = inv.getGrandTotal();

			overallItems += numItems;
			overallTax += tax;
			overallTotal += total;

			System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n", inv.getInvoiceId().toString(), name, numItems,
					tax, total);
		}

		// 4) Print footer
		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n", "", // blank UUID
				"Overall Totals:", overallItems, overallTax, overallTotal);
	}

	/** Invoices by grand‑total descending. */
	public void printByTotal(List<Invoice> invoices, Map<String, Companies> companies) {
		Comparator<Invoice> cmp = Comparator.comparing(Invoice::getGrandTotal).reversed()
				.thenComparing(inv -> inv.getInvoiceId().toString());
		SortedList<Invoice> sorted = new SortedList<>(cmp);
		sorted.addAll(invoices);

		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.println(
				"|                               SummaryReport - By Total                                      |");
		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.printf("%-36s  %-30s  %-10s  %-10s  %-10s%n", "Invoice #", "Customer", "NumItems", "Tax", "Total");

		int overallItems = 0;
		double overallTax = 0;
		double overallTotal = 0;
		for (Invoice inv : sorted) {
			Companies comp = companies.get(inv.getCustomer());
			String name = comp != null ? comp.getName() : inv.getCustomer();

			int numItems = inv.getItems().size();
			double tax = inv.getTaxTotal();
			double total = inv.getGrandTotal();

			overallItems += numItems;
			overallTax += tax;
			overallTotal += total;

			System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n", inv.getInvoiceId(), name, numItems, tax, total);
		}

		System.out.println(
				"+---------------------------------------------------------------------------------------------+");
		System.out.printf("%-36s  %-30s  %10d  $%9.2f  $%9.2f%n", "", "Overall Totals:", overallItems, overallTax,
				overallTotal);
		System.out.println();
	}

	/** Invoices by customer name A→Z. */
	public void printByCustomer(List<Invoice> invoices, Map<String, Companies> companies) {
		Comparator<Invoice> cmp = Comparator.comparing((Invoice inv) -> {
			Companies c = companies.get(inv.getCustomer());
			return c != null ? c.getName() : inv.getCustomer();
		}, String.CASE_INSENSITIVE_ORDER).thenComparing(inv -> inv.getInvoiceId().toString());
		SortedList<Invoice> sorted = new SortedList<>(cmp);
		sorted.addAll(invoices);

		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("|                               Invoices by Customer                      |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("%-36s  %-30s  %10s%n", "Invoice", "Customer", "Total");

		for (Invoice inv : sorted) {
			Companies comp = companies.get(inv.getCustomer());
			String name = comp != null ? comp.getName() : inv.getCustomer();
			double total = inv.getGrandTotal();

			System.out.printf("%-36s  %-30s  $%10.2f%n", inv.getInvoiceId(), name, total);
		}
		System.out.println();
	}

	/** Customer invoice totals, including zero‑invoice companies. */
	public void printCustomerTotals(List<Invoice> invoices, Map<String, Companies> companiesByUuid) {
		Map<String, Long> countMap = invoices.stream()
				.collect(Collectors.groupingBy(Invoice::getCustomer, Collectors.counting()));
		Map<String, Double> sumMap = invoices.stream()
				.collect(Collectors.groupingBy(Invoice::getCustomer, Collectors.summingDouble(Invoice::getGrandTotal)));

		Comparator<String> cmp = Comparator.comparing((String uuid) -> {
			Companies c = companiesByUuid.get(uuid);
			return c != null ? c.getName() : uuid;
		}, String.CASE_INSENSITIVE_ORDER).thenComparing(uuid -> uuid);
		SortedList<String> sortedUuids = new SortedList<>(cmp);
		sortedUuids.addAll(companiesByUuid.keySet());

		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("|                      Customer Invoice Totals                            |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("%-25s  %-24s  %10s%n", "Customer", "#Invoices", "Total");

		for (String uuid : sortedUuids) {
			Companies comp = companiesByUuid.get(uuid);
			String name = comp != null ? comp.getName() : uuid;
			long cnt = countMap.getOrDefault(uuid, 0L);
			double total = sumMap.getOrDefault(uuid, 0.0);

			System.out.printf("%-25s  %24d  $%10.2f%n", name, cnt, total);
		}
		System.out.println("+-------------------------------------------------------------------------+");
	}
}