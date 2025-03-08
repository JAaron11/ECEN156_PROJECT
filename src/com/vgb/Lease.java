package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Lease extends Items{
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	/**
	 * Constructs a Lease instance.
	 */
	public Lease(UUID uuid, char type, String name, String extraField1, int extraField2,
			LocalDate startDate, LocalDate endDate) {
		super(uuid, type, name, extraField1, extraField2);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public double calculateTotalCost() {
        // Calculate inclusive days between start and end dates.
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double years = days / 365.0;
        // Amortized fraction over 5 years.
        double amortizedFraction = years / 5.0;
        double cost = extraField2; // base equipment cost
        // Lease cost calculation: fraction * baseCost * 1.5
        double leaseCost = amortizedFraction * cost * 1.5;
        // If lease cost exceeds $12,500, add a flat tax of $1,500.
        double tax = (leaseCost > 12500) ? 1500.0 : 0.0;
        double total = leaseCost + tax;
        return Math.round(total * 100.0) / 100.0;
    }
	
	public double getLeaseBaseCost() {
		// Compute and return the lease cost before tax.
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		double years = days / 365.0;
		double amortizedFraction = years / 5.0;
		return Math.round((amortizedFraction * extraField2 * 1.5) * 100.0) / 100.0;
	}
	
	public double getLeaseTax() {
		double leaseCost = getLeaseBaseCost();
		return (leaseCost > 12500) ? 1500.0 : 0.0;
	}
    
    @Override
    public String toString() {
        return super.toString() + ", Lease Period: " + startDate + " to " + endDate;
    }
}