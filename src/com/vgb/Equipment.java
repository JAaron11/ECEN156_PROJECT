package com.vgb;

import java.util.UUID;

public class Equipment extends Items {
	private String model;
	
	public Equipment(UUID uuid, String type, String name, String extraField1, String extraField2) {
		super(uuid, 'E', name, extraField1, extraField2, price);
		this.model = model;
	}
	
	public double calculatePurchaseCost() {
		double tax = getPrice() * 0.0525; // 5.25% tax
		return getPrice() + Math.round(tax * 100) / 100.0;
	}
	
	public double calculateRentalCost(int hours) {
		double perHourCost = getPrice() * 0.001;
		double total = perHourCost * hours;
		double tax = total * 0.0438; // 4.38% tax
		return Math.round((total + tax) * 100) / 100.0;
	}
	
	public double calculateLeaseCost(int days) {
		double years = days / 365.0;
		double amortizedFactor = years / 5.0;
		double leaseCost = amortizedFactor * getPrice() * 1.5;
		return leaseCost > 12500 ? leaseCost + 1500 : leaseCost;
	}
	
	public double calculateTotalCost() {
		return calculatePurchaseCost();
	}
	
	public double calculateTax() {
		return getPrice() * 0.0525;
	}
	
}

