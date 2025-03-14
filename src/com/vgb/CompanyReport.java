package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CompanyReport {
	/**
	 * Prints the company invoice summary report.
	 * @param invoices List of Invoice objects.
	 * @param companies Map of company UUID (as String) to Company object.
	 */
    public static void printReport(List<Invoice> invoices, Map<String, Companies> companies) {
    	// Group invoices by company name.
    	Map<String, List<Invoice>> companyMap = new TreeMap<>();
    	for (Invoice inv : invoices) {
    		String companyUUID = inv.getCustomer();
    		Companies comp = companies.get(companyUUID);
    		String companyName = (comp != null) ? comp.getName() : companyUUID;
    		// Use companyName as the key
    		companyMap.computeIfAbsent(companyName, k -> new ArrayList<>()).add(inv);
    	}
    	System.out.println();
    	System.out.println("+----------------------------------------------------------------+");
    	System.out.println("|                 Company Invoice Summary Report                 |");
    	System.out.println("+----------------------------------------------------------------+");
    	System.out.printf("%-30s %-12s %-10s%n", "Company", "# Invoices", "Grand Total");
    	
    	int overallInvoices = 0;
    	double overallGrand = 0;
    	for (String company : companyMap.keySet()) {
    		List<Invoice> invs = companyMap.get(company);
    		double companyTotal = 0;
    		for (Invoice inv : invs) {
    			companyTotal += inv.getGrandTotal();
    		}
    		overallInvoices += invs.size();
    		overallGrand += companyTotal;
    		System.out.printf("%-30s %-12d $%10.2f%n", company, invs.size(), companyTotal);
    	}
    	System.out.println("+----------------------------------------------------------------+");
    	System.out.printf("%-30s %-12d $%10.2f%n", "", overallInvoices, overallGrand);
    }
}
