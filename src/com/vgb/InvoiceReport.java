//package com.vgb;
/**
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
*/
/**
 * InvoiceReport.java
 * 
 * This class loads invoice and invoice item data from CSV files in the "data" directory,
 * associates invoice items with invoices, and produces three reports.
 * 	1. A Summary Report showing all invoices and their totals.
 * 	2. A Customer Report summarizing invoices by customer.
 * 	3. A Detailed Invoice Report showing each invoice and its items.
 * 
 * To run this program ensure that the data files (Invoices.csv and InvoiceItems.csv)
 * are located in the "data" directory.
 */

/**
public class InvoiceReport {
	
	public static void main(String[] args) {
		// Load invoice data from CSV file
		List<Invoice> invoices = loadInvoices("data/Invoices.csv");
		// Load invoice item data from CSV file
		Map<String, List<Item>> invoiceItemsMap = loadInvoiceItems("data/InvoiceItems.csv");
		
		// Associate invoice items with the corresponding invoices
		for (Invoice invoice : invoices) {
			List<Item> items = invoiceItemsMap.get(invoice.getInvoiceUUID());
			if (items != null) {
				invoice.setItems(items);
			}
		}
		
		// Generate the three reports
		generateSummaryReport(invoices);
		generateCustomerReport(invoices);
		generateDetailReport(invoices);
	}
	
	/**
	 * Loads invoices from the specified CSV file.
	 * 
	 * @param filePath the path to the Invoices.csv file
	 * @return a list of Invoice objects
	 */
/**
	static List<Invoice> loadInvoices(String filePath) {
		List<Invoice> invoices = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			//Skip the header line
			for (int i = 1; i < lines.size(); i++) {
				String[] tokens = lines.get(i).split(",");
				if (tokens.length >= 4) {
					// tokens: invoiceUUID, customerUUID, salesPersonUUID, date
					Invoice inv = new Invoice(tokens[0].trim(), tokens[1].trim(),
											  tokens[2].trim(), tokens[3].trim());
					invoices.add(inv);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading invoices file: " + e.getMessage());
		}
		return invoices;
	}
	
	/**
	 * Loads invoice items from the specified CSV file.
	 * 
	 * @param filePath the path to the InvoiceItems.csv file
	 * @return a map of invoice UUID to a list of InvoiceItem objects
	 */
/**
	static Map<String, List<Item>> loadInvoiceItems(String filePath) {
		Map<String, List<Item>> invoiceItemsMap = new HashMap<String, List<Item>>();
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			// Skip the header line
			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				String[] tokens = line.split(",");
				if (tokens.length < 2) {
					continue;
				}
				String invUUID = tokens[0].trim();
				String itemUUID = tokens[1].trim();
				// The remaining tokens contain variable details
				String[] details = Arrays.copyOfRange(tokens, 2, tokens.length);
				// Trim each detail
				for (int j = 0; j < details.length; j++) {
					details[j] = details[j].trim();
				}
				Item item = new Item(invUUID, itemUUID, details);
				map.computeIfAbsent(invUUID, k -> new ArrayList<>().add(item));
			}	
		} catch (IOException e) {
			System.err.println("Error reading invoice items file: " + e.getMessage());
		}
		return map;
	}
	
	/**
	 * Generates a summary report of all invoices.
	 * 
	 * @param invoices the list of invoices
	 */
/**
	static void generateSummaryReport(List<Invoice> invoices) {
		System.out.println("=== Summary Report ===");
		double grandTotal = 0.0;
		for (Invoice inv : invoices) {
			double invoiceTotal = inv.calculateTotal();
			grandTotal += invoiceTotal;
			System.out.printf("Invoice: %s | Customer: %s | Date: %s | Total: $%.2f%n",
					inv.getInvoiceUUID(), inv.getCustomerUUID(), inv.getDate(), invoiceTotal);
		}
		System.out.printf("Grand Total for all invoices: $%.2f%n%n", grandTotal);
	}
	
	/**
	 * Generates a customer report summarizing invoices by customer.
	 * 
	 * @param invoices the list of invoices
	 */
/**
	static void generateCustomerReport(List<Invoice> invoices) {
		System.out.println("=== Customer ===");
		Map<String, Double> customerTotals = new HashMap<>();
		for (Invoice inv : invoices) {
			double total = inv.calculateTotal();
			customerTotals.put(inv.getCustomerUUID(), customerTotals.getOrDefault(inv.getCustomerUUID(), 0.0) + total);
		}
		for (Map.Entry<String, Double> entry : customerTotals.entrySet()) {
			System.out.printf("Customer: %s | Total Invoiced: $%.2f%n", entry.getKey(), entry.getValue());
		}
		System.out.println();
	}
	
	/**
	 * Generates a detailed report for each invoice.
	 * 
	 * @param invoices the list of invoices
	 */
/**
	static void generateDetailReport(List<Invoice> invoices) {
		System.out.println("=== Detail Report ===");
		for (Invoice inv : invoices) {
			System.out.printf("Invoice: %s%nCustomer: %s%nSalesperson: %s%nDate: %s%n",
					inv.getInvoiceUUID(), inv.getCustomerUUID(), inv.getSalesPersonUUID(), inv.getDate());
			if (inv.getItems() != null && !inv.getItems().isEmpty()) {
				for (Item item : inv.getItems()) {
					System.out.print(" Item: " + item.getItemUUID());
					if (item.getDetails() != null && item.getDetails().length > 0) {
						System.out.print(" [");
						System.out.print(String.join(", ", item.getDetails()));
						System.out.print("]");
					}
					System.out.println();
				}
			}
			System.out.printf("Subtotal: $%.2f$n", inv.calculateTotal());
			System.out.println("------------------------------");
		}
	}

}
*/

package com.vgb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InvoiceReport {
    public static void main(String[] args) {
        // Load invoices from the CSV file
        List<Invoice> invoices = loadInvoices("data/Invoices.csv");
        // Load invoice items from the CSV file (each mapped by invoice UUID)
        Map<UUID, List<Item>> invoiceItemsMap = loadInvoiceItems("data/InvoiceItems.csv");

        // Associate items with their corresponding invoices
        for (Invoice inv : invoices) {
            List<Item> items = invoiceItemsMap.get(inv.getInvoiceId());
            if (items != null) {
                for (Item item : items) {
                    inv.addItems(item);
                }
            }
        }
        
        // Print a report for each invoice
        for (Invoice inv : invoices) {
            System.out.println(inv);
            System.out.println("-----------------------------------------------------");
        }
    }
    
    /**
     * Loads invoices from the specified CSV file.
     * Expected format per line (after header):
     * invoiceUUID,customerUUID,salesPersonUUID,date
     */
    public static List<Invoice> loadInvoices(String filePath) {
        List<Invoice> invoices = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Skip the header line
            for (int i = 1; i < lines.size(); i++) {
                String[] tokens = lines.get(i).split(",");
                if (tokens.length >= 4) {
                    // Create an Invoice using the provided tokens
                    Invoice inv = new Invoice(
                        tokens[0].trim(), 
                        tokens[1].trim(), 
                        tokens[2].trim(), 
                        tokens[3].trim());
                    invoices.add(inv);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading invoices file: " + e.getMessage());
        }
        return invoices;
    }
    
    /**
     * Loads invoice items from the specified CSV file.
     * Expected format per line (after header):
     * invoiceUUID,itemUUID,field(s)
     * The fields vary based on item type:
     *   - Equipment: indicator "P" optionally followed by purchase cost.
     *   - Lease: indicator "L", then start date, end date, and daily cost.
     *   - Rental: indicator "R", then number of hours (assumes a default hourly rate).
     *   - Material or Contract: a single numeric token. For demonstration, if the number is
     *     greater than or equal to 1000, it is treated as a contract; otherwise, as material.
     */
    public static Map<UUID, List<Item>> loadInvoiceItems(String filePath) {
        Map<UUID, List<Item>> map = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Skip header line
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] tokens = line.split(",");
                if (tokens.length < 3)
                    continue;
                
                // Parse invoice and item UUIDs
                UUID invoiceUUID = UUID.fromString(tokens[0].trim());
                UUID itemUUID = UUID.fromString(tokens[1].trim());
                
                // The third token determines the type or value
                String indicator = tokens[2].trim();
                Item item = null;
                
                if (indicator.equals("P")) {
                    // Equipment purchase.
                    // Optionally, token[3] may be the purchase cost.
                    double cost = (tokens.length > 3) ? Double.parseDouble(tokens[3].trim()) : 1000.0;
                    item = new Equipment(itemUUID, 'E', "Equipment", "model", cost);
                } else if (indicator.equals("L")) {
                    // Lease: expected tokens: indicator, startDate, endDate, dailyCost.
                    if (tokens.length >= 6) {
                        String startDate = tokens[3].trim();
                        String endDate = tokens[4].trim();
                        double dailyCost = Double.parseDouble(tokens[5].trim());
                        item = new Lease(itemUUID, 'L', "Lease", "model", dailyCost, startDate, endDate);
                    }
                } else if (indicator.equals("R")) {
                    // Rental: expected tokens: indicator, hours.
                    double hours = (tokens.length > 3) ? Double.parseDouble(tokens[3].trim()) : 1.0;
                    double hourlyRate = 50.0; // default hourly rate
                    item = new Rental(itemUUID, 'R', "Rental", "model", hourlyRate, hours);
                } else {
                    // No indicator letter; assume a numeric value.
                    double value = Double.parseDouble(indicator);
                    if (value >= 1000) {
                        // Treat as a Contract with the provided total amount.
                        item = new Contract(itemUUID, 'C', "Contract", "Company", value);
                    } else {
                        // Otherwise, treat as Material with quantity.
                        double unitCost = 10.0; // default unit cost for materials
                        item = new Material(itemUUID, 'M', "Material", "material", value);
                    }
                }
                
                if (item != null) {
                    map.computeIfAbsent(invoiceUUID, k -> new ArrayList<>()).add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading invoice items file: " + e.getMessage());
        }
        return map;
    }
}

