package com.vgb;

import java.util.UUID;

public class Contract extends Items {
	private double fixedAmount;
	
	public Contract(UUID uuid, String name, String extra1, double fixedAmount) {
		super(uuid, 'C', name, null, null, fixedAmount);
		this.fixedAmount = fixedAmount;
		}
	
	public double calculateTotalCost() {
		return fixedAmount;
	}
	
	public double calculateTax() {
		return 0.0;
	}
}
