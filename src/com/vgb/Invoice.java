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
	
	public double getSubtotal() {
		double subtotal = 0.0;
        for (Items item : items) {
            if (item instanceof Equipment) {
                // Equipment: assume getCost() returns the base cost.
                subtotal += ((Equipment)item).getCost();
            } else if (item instanceof Lease) {
                // Lease: assume getLeaseBaseCost() returns the base lease cost before tax.
                subtotal += ((Lease)item).getLeaseBaseCost();
            } else if (item instanceof Rental) {
                // Rental: assume getRentalBaseCost() returns the base rental cost before tax.
                subtotal += ((Rental)item).getRentalBaseCost();
            } else if (item instanceof Material) {
                // Material: assume getBaseCost() returns (unit price * quantity).
                subtotal += ((Material)item).getBaseCost();
            } else if (item instanceof Contract) {
                // Contract: assume getContractAmount() returns the contract amount.
                subtotal += ((Contract)item).getContractAmount();
            }
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }
	
	/**
     * Returns the total tax for the invoice.
     */
    public double getTaxTotal() {
        double taxTotal = 0.0;
        for (Items item : items) {
            if (item instanceof Equipment) {
                taxTotal += ((Equipment)item).calculateTax();
            } else if (item instanceof Lease) {
                taxTotal += ((Lease)item).getLeaseTax();
            } else if (item instanceof Rental) {
                taxTotal += ((Rental)item).getRentalTax();
            } else if (item instanceof Material) {
                taxTotal += ((Material)item).calculateTax();
            }
            // Contracts have no tax.
        }
        return Math.round(taxTotal * 100.0) / 100.0;
    }
	
    /**
     * Returns the grand total: subtotal + tax total.
     */
    public double getGrandTotal() {
        return Math.round((getSubtotal() + getTaxTotal()) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Items item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Subtotal: ").append(getSubtotal()).append("\n");
        sb.append("Tax Total: ").append(getTaxTotal()).append("\n");
        sb.append("Grand Total: ").append(getGrandTotal());
        return sb.toString();
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
