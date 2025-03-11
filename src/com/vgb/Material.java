package com.vgb;

import java.util.UUID;

public class Material extends Item {
	
	protected String extraField1;
	protected double extraField2;
	
	private int quantity;

    public Material(UUID uuid, char type, String name, String extraField1, double extraField2) {
        super(uuid, 'M', name, extraField1, extraField2);
        this.quantity = 0; // default value until set explicitly
    }
    
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }
    
    public int getQuantity() {
    	return quantity;
    }
    
    // Cost before tax
    public double getBaseCost() {
    	double unitPrice = getPrice();
    	double baseCost = unitPrice * quantity;
    	return Math.round(baseCost * 100.0) / 100.0;
    }
    // Total cost including taxes at rate of 7.15%.
    public double calculateTotalCost() {
        double unitPrice = getPrice();
        double cost = unitPrice * quantity;
        double tax = cost * 0.0715;
        return Math.round((cost + tax) * 100) / 100.0;
    }

    public double calculateTax() {
    	double baseCost = getBaseCost();
    	double tax = baseCost * 0.0715;
        return Math.round(tax * 100.0) / 100.0;
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
