package com.vgb;

import java.util.UUID;

public class Material extends Item {
	
	protected String material;
	protected double unitCost;
	
	private int quantity;

    public Material(UUID uuid, char type, String name, String material, double unitCost) {
        super(uuid, 'M', name, null);
        this.quantity = 0; // default value until set explicitly
        this.material = material;
        this.unitCost = unitCost;
    }
    
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }
    
    public int getQuantity() {
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
		return uuid + " (Material) " + name + "\n\t" + quantity + " @ " + "$" + String.format("%.2f", (double) unitCost) + "/" + material;
	}
}
