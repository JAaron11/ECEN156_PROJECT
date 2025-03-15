package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a lease agreement for an item, with a defined start and end date.
 */
public class Lease extends Item{
	
	protected String model;
	protected double leasePrice;
	private LocalDate startDate;
	private LocalDate endDate;
	
	/**
	 * Constructs a Lease instance.
	 */
	public Lease(UUID uuid, char type, String name, String model, double leasePrice,
			LocalDate startDate, LocalDate endDate) {
		super(uuid, type, name);
		this.model = model;
		this.leasePrice = leasePrice;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * Calculates the total lease cost including any applicable tax.
	 * Lease cost is computed as an amortized fraction over 5 years with a multiplier of 1.5x.
	 * If the computed lease cost exceeds $12,500, a flat tax of $1,500 is applied.
	 */
	public double calculateTotalCost() {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double years = days / 365.0;
        double amortizedFraction = years / 5.0;
        double cost = leasePrice; // base equipment cost
        double leaseCost = amortizedFraction * cost * 1.5;
        double tax = (leaseCost > 12500) ? 1500.0 : 0.0;
        double total = leaseCost + tax;
        return Util.roundToTenths(total);
    }
	
	public double calculateSubTotal() {
		// Compute and return the lease cost before tax.
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		double years = days / 365.0;
		double amortizedFraction = years / 5.0;
		return Util.roundToTenths(amortizedFraction * leasePrice * 1.5);
	}
	
	public double calculateTax() {
		double leaseCost = calculateSubTotal();
		return (leaseCost > 12500) ? 1500.0 : 0.0;
	}
    
    @Override
    public String toString() {
        return uuid + " (Lease) " + name + "-" + model + "\n\t" + (ChronoUnit.DAYS.between(startDate, endDate) + 1) + " days (" + startDate + " -> " + endDate + ")";
    }
    
    public double getSubTotal() {
		return calculateSubTotal();
	}
	
	public double getTaxTotal() {
		return calculateTax();
	}
}