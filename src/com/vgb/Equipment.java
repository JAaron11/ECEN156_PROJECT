package com.vgb;

import java.util.UUID;

public class Equipment extends Items {

    public Equipment(UUID uuid, char type, String name, String extraField1, int extraField2) {
        super(uuid, 'E', name, extraField1, extraField2);
    }
    
    public double getCost() {
    	return extraField2;
    }

    public double calculateTax() {
        return Math.round((extraField2 * 0.0525) * 100.0) / 100.0;
    }
    
    public double calculateTotalCost() {
    	double cost = extraField2;
    	double tax = cost * 0.0525;
    	return Math.round((cost + tax) * 100.0) / 100.0;
    }
    
    public String toString() {
    	//Ensure the string representation includes the name, model and cost formatted to two decimals.
    	return "Equipment{name='" + name + "', model='" + extraField1 + "', cost=" + String.format("%.2f", (double) extraField2) + "}";
    }
}

