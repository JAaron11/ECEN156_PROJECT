package com.vgb;

import java.util.UUID;

public class Material extends Items{
	private String unit;
	private int quantity;
	
	public Material(UUID uuid, String name, double price, String unit, int quantity) {
		super(uuid, 'M', name, unit, null, price);
		this.unit = unit;
		this.quantity = quantity;
	}

	public double calculateTotalCost() {
		double total = getPrice() * quantity;
		double tax = total * 0.0715;
		return Math.round((total + tax) * 100) / 100.0;
	}
	
	public double calculateTax() {
		return getPrice() * quantity * 0.0715;
	}

}
