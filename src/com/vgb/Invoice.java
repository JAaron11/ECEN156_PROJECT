package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice {
	private UUID invoiceId;
	private List<Items> items;
	
	public Invoice() {
		this.invoiceId = UUID.randomUUID();
		this.items = new ArrayList<>();
	}
	
	public void addItems(Items items) {
		this.items.add(items);
	}
	
	public double calculateTotalInvoiceCost() {
		return items.stream().mapToDouble(Items::calculateTotalCost).sum();
	}
	
	public void printInvoice() {
		System.out.println("Invoice ID: " + invoiceId);
		items.forEach(items -> System.out.println(items.getName() + ": $" + items.calculateTotalCost()));
		System.out.println("Total: $" + calculateTotalInvoiceCost());
	}

}
