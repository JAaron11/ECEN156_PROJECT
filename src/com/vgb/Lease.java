package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a lease agreement for an item, with a defined start and end date.
 */
public class Lease extends Item{
	
	protected String extraField1;
	protected double extraField2;
	
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
	
	/**
	 * Calculates the total lease cost including any applicable tax.
	 * Lease cost is computed as an amortized fraction over 5 years with a multiplier of 1.5x.
	 * If the computed lease cost exceeds $12,500, a flat tax of $1,500 is applied.
	 */
	public double calculateTotalCost() {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double years = days / 365.0;
        double amortizedFraction = years / 5.0;
        double cost = extraField2; // base equipment cost
        double leaseCost = amortizedFraction * cost * 1.5;
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

	@Override
	public double getPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTax() {
		// TODO Auto-generated method stub
		return 0;
	}
}