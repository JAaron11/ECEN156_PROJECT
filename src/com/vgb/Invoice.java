package com.vgb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Invoice {
	private UUID invoiceId;
	private Date date;
	private String customer;
	private List<Item> items;
	
	/**
	 * Constructs a new Invoice with a unique ID and an empty list of items.
	 */
	public Invoice() {
		this.invoiceId = UUID.randomUUID();
		this.date = new Date();
		this.customer = "Unknown Customer"; // default value; setCustomer changes it.
		this.items = new ArrayList<>();
	}
	
	/**
	 * Alternative constructor that accepts a customer name.
	 * @param customer The customer (company name) associated with the invoice.
	 */
	public Invoice(String customer) {
		this();
		this.customer = customer;
	}
	
	/**
	 * Adds an item to the invoice.
	 * @param items
	 */
	public void addItems(Item items) {
		this.items.add(items);
	}
	
	public double getSubtotal() {
		double subtotal = 0.0;
		for (Item item : items) {
			subtotal += item.calculateSubTotal();
		}
		return Util.roundToTenths(subtotal);
	}
	
	public double getTaxTotal() {
		double taxTotal = 0.0;
		for (Item item : items) {
			taxTotal += item.calculateTax();
		}
		return Util.roundToTenths(taxTotal);
	}
	
    /**
     * Returns the grand total: subtotal + tax total.
     * @return The final total cost of the invoice rounded to two decimal places
     */
    public double getGrandTotal() {
        return Util.roundToTenths((getSubtotal() + getTaxTotal()));
    }

    /**
     * Provides a string representation of the invoice
     * @return A formatted string representation of the invoice
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //Header details
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Customer: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(invoiceId).append("\n\n");
        for (Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Subtotal: ").append(getSubtotal()).append("\n");
        sb.append("Tax Total: ").append(getTaxTotal()).append("\n");
        sb.append("Grand Total: ").append(getGrandTotal());
        return sb.toString();
    }
    
	public double calculateTotalInvoiceCost() {
		return items.stream().mapToDouble(Item::calculateTotalCost).sum();
	}
	
	public void printInvoice() {
		System.out.println("Invoice ID: " + invoiceId);
		System.out.println("Customer: " + customer);
		System.out.println("Date: " + date);
		System.out.println("Items: ");
		for (Item item : items) {
			System.out.println(item.getName() + ": $" + item.calculateTotalCost());
		}
		System.out.println("Total: $" + calculateTotalInvoiceCost());
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
}