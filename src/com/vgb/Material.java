package com.vgb;

import java.util.UUID;

public class Material extends Item {
	
	protected String unit;
	protected double unitCost;
	private double quantity;

    public Material(UUID uuid, char type, String name, String unit, double quantity, double unitCost) {
        super(uuid, 'M', name);
        this.quantity = quantity; // default value until set explicitly
        this.unit = unit;
        this.unitCost = unitCost;
    }
    
    public void setQuantity(double quantity) {
    	this.quantity = quantity;
    }
    
    public double getQuantity() {
    	return quantity;
    }
    
    // Cost before tax
    public double calculateSubTotal() {
    	double subTotal = unitCost * quantity;
    	return Util.roundToTenths(subTotal);
    }
    // Total cost including taxes at rate of 7.15%.
    public double calculateTotalCost() {
        double tax = calculateSubTotal() * 0.0715;
        return Util.roundToTenths(calculateSubTotal() + tax);
    }

    public double calculateTax() {
    	double tax = calculateSubTotal() * 0.0715;
        return Util.roundToTenths(tax);
    }

	@Override
	public String toString() {
		return uuid + " (Material) " + name + "\n\t" + quantity + " @ " + "$" + unitCost + "/" + unit;
	}

	public double getUnitCost() {
		return unitCost;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public double getSubTotal() {
		return calculateSubTotal();
	}
	
	public double getTaxTotal() {
		return calculateTax();
	}
}
