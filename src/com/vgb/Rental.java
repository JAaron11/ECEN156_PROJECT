package com.vgb;

import java.util.UUID;

public class Rental extends Items {

    private double hours;
    
    /**
     * Constructs a Rental instance.
     * @param uuid The unique identifier.
     * @param type The item type (should be 'R' for rental).
     * @param name The equipment name.
     * @param extraField1 Typically, the model or description.
     * @param extraField2 The base cost (price) of the equipment.
     * @param hours The number of rental hours.
     */
    public Rental(UUID uuid, char type, String name, String extraField1, int extraField2, double hours) {
        super(uuid, type, name, extraField1, extraField2);
        this.hours = hours;
    }

    /**
     *  Calculates the total rental cost including tax.
     *  Rental cost is determined based on 0.1% of the base cost per hour.
     *  A tax rate of 4.38% is applied to the rental cost.
     */
    
    @Override
    public double calculateTotalCost() {
        double baseCost = extraField2; // equipment's base cost
        double perHourCharge = baseCost * 0.001;
        double rentalCost = perHourCharge * hours;
        double tax = rentalCost * 0.0438;
        double total = rentalCost + tax;
        return Math.round(total * 100.0) / 100.0;
    }
    
    // Rental cost before taxes
    public double getRentalBaseCost() {
    	double baseCost = extraField2; // base equipment cost stored as int in extraField2
    	double perHourCharge = baseCost * 0.001;
    	double rentalCost = perHourCharge * hours;
    	return Math.round(rentalCost * 100.0) / 100.0;
    }
    
    // Tax applied to the rental cost.
    public double getRentalTax() {
    	double baseCost = getRentalBaseCost();
    	double tax = baseCost * 0.0438;
    	return Math.round(tax * 100.0) / 100.0;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Rental Hours: " + hours;
    }
}
