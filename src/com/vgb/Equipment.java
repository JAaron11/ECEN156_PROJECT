package com.vgb;

import java.util.UUID;

public class Equipment extends Item {
	
	protected String extraField1;
	protected double extraField2;

	/**
	 * Constructs an Equipment instance.
	 * @param uuid 
	 * @param type Type identifier ('E' for equipment)
	 * @param name Name of the equipment
	 * @param extraField1 Model name of the equipment
	 * @param extraField2 Cost of the equipment
	 */
    public Equipment(UUID uuid, char type, String name, String extraField1, int extraField2) {
        super(uuid, 'E', name, extraField1, extraField2);
    }
    
    public double getPrice() {
    	return extraField2;
    }

    public double calculateTax() {
        return Math.round((extraField2 * 0.0525) * 100.0) / 100.0; // Tax rate is fixed at 5.25%.
    }
    
    public double calculateTotalCost() {
    	double cost = extraField2;
    	double tax = cost * 0.0525;
    	return Math.round((cost + tax) * 100.0) / 100.0;
    }
    
    public String toString() {
    	return "Equipment{name='" + name + "', model='" + extraField1 + "', cost=" + String.format("%.2f", (double) extraField2) + "}";
    }
}

