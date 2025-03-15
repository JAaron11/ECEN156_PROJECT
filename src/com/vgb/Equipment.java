package com.vgb;

import java.util.UUID;

public class Equipment extends Item {
	
	protected String model;
	protected double equipmentPrice;

	/**
	 * Constructs an Equipment instance.
	 * @param uuid 
	 * @param type Type identifier ('E' for equipment)
	 * @param name Name of the equipment
	 * @param extraField1 Model name of the equipment
	 * @param extraField2 Cost of the equipment
	 */
    public Equipment(UUID uuid, char type, String name, String model, double equipmentPrice) {
        super(uuid, 'E', name);
        this.model = model;
        this.equipmentPrice = equipmentPrice;
    }
    
    public double calculateSubTotal() {
    	return Util.roundToTenths(equipmentPrice);
    }

    public double calculateTax() {
    	double tax = equipmentPrice * 0.0525;
        return Util.roundToTenths(tax); // Tax rate is fixed at 5.25%.
    }
    
    public double calculateTotalCost() {
    	return Util.roundToTenths(calculateTax() + equipmentPrice);
    }
    
    public String toString() {
    	return uuid + " (Purchase) " + "\t" + name + "-" + model + String.format("%.2f", equipmentPrice);
    }

	public double getEquipmentPrice() {
		return equipmentPrice;
	}
	
	public double getSubTotal() {
		return calculateSubTotal();
	}
	
	public double getTaxTotal() {
		return calculateTax();
	}
}