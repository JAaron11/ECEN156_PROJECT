package com.vgb;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Invoice {
	private UUID invoiceId;
	private Date date;
	private String customer;
	private String salesPerson;
	private List<Item> items;
	
	/**
	 * Constructs a new Invoice with a unique ID and an empty list of items.
	 */
	public Invoice() {
		this.invoiceId = UUID.randomUUID();
		this.date = new Date();
		this.customer = "Unknown Customer"; // default value; setCustomer changes it.
		this.salesPerson = "Unknown Salesperson";
		this.items = new ArrayList<>();
	}
	
	/**
	 * Constructs a new Invoice with provided values.
	 * 
	 * @param invoiceUUID 		The invoice UUID (will be parsed; if invalid; a random UUID is generated).
	 * @param customerUUID		The customer identifier (or name, as applicable).
	 * @param salesPersonUUID	The salesperson identifier.
	 * @param dateStr			The date of the invoice in the format YYYY-MM-DD.
	 */
	public Invoice(String invoiceUUID, String customerUUID, String salesPersonUUID, String dateStr) {
		try {
			this.invoiceId = UUID.fromString(invoiceUUID);
		} catch (IllegalArgumentException e) {
			this.invoiceId = UUID.randomUUID();
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			this.date = sdf.parse(dateStr);
		} catch (ParseException e) {
			this.date = new Date();
		}
		this.customer = customerUUID;
		this.salesPerson = salesPersonUUID;
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
	 * @param item the Item to add.
	 */
	public void addItems(Item items) {
		this.items.add(items);
	}
	
	/**
	 * Calculates the subTotal by summing the subtotal of each item.
	 *  
	 * @return The subtotal rounded to one decimal place.
	 */
	public double getSubtotal() {
		double subtotal = 0.0;
		for (Item item : items) {
			subtotal += item.calculateSubTotal();
		}
		return Util.roundToTenths(subtotal);
	}
	
	/**
	 * Calculates the total tax by summing the tax of each item.
	 * 
	 * @return The tax total rounded to one decimal place.
	 */
	public double getTaxTotal() {
		double taxTotal = 0.0;
		for (Item item : items) {
			taxTotal += item.calculateTax();
		}
		return Util.roundToTenths(taxTotal);
	}
	
    /**
     * Returns the grand total: subtotal + tax total.
     * 
     * @return The final total cost of the invoice rounded to two decimal places
     */
    public double getGrandTotal() {
        return Util.roundToTenths((getSubtotal() + getTaxTotal()));
    }

    /**
     * Provides a string representation of the invoice
     * 
     * @return A formatted string representation of the invoice
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        StringBuilder sb = new StringBuilder();
        //Header details
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Customer: ").append(customer).append("\n");
        sb.append("Salesperson: ").append(salesPerson).append("\n");
        sb.append("Date: ").append(date).append("\n\n");
        for (Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Subtotal: ").append(getSubtotal()).append("\n");
        sb.append("Tax Total: ").append(getTaxTotal()).append("\n");
        sb.append("Grand Total: ").append(getGrandTotal());
        return sb.toString();
    }
    
    /**
     * Calculates the total cost of the invoice by summing the total cost of each item
     * 
     * @return The total invoice cost.
     */
	public double calculateTotalInvoiceCost() {
		return items.stream().mapToDouble(Item::calculateTotalCost).sum();
	}
	
	/**
	 * Prints the invoice details to the standard output.
	 */
	public void printInvoice() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Invoice ID: " + invoiceId.toString());
		System.out.println("Customer: " + customer);
		System.out.println("Salesperson: " + salesPerson);
		System.out.println("Date: " + sdf.format(date));
		System.out.println("Items: ");
		for (Item item : items) {
			System.out.println(item.getName() + ": $" + item.calculateTotalCost());
		}
		System.out.println("Subtotal: $" + getSubtotal());
		System.out.println("Tax Total: $" + getTaxTotal());
		System.out.println("Grand Total: $" + getGrandTotal());
	}
	
	/**
	 * Sets the customer for this invoice.
	 * 
	 * @param customer The customer name or identifier.
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public UUID getInvoiceId() {
		return this.invoiceId;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public List<Item> getItems() {
		return this.items;
	}

	public String getSalesPerson() {
		return this.salesPerson;
	}
	
	public UUID getCustomerUuid() {
        try {
            return UUID.fromString(this.customer);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

	public void setInvoiceId(UUID fromString) {
		// TODO Auto-generated method stub
		
	}

	public void setDate(java.sql.Date date2) {
		// TODO Auto-generated method stub
		
	}

	public void setSalesPerson(String string) {
		// TODO Auto-generated method stub
		
	}
}