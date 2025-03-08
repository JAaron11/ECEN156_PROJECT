package com.vgb;

import java.util.UUID;

public class Contract extends Items {
	//Field to store the actual contract amount (default of 0)
	private double contractAmount = 0.0;
	
	public Contract(UUID uuid, char type, String name, String extraField1) {
		// since contracts do not utilize extraField2 for the cost, passed as zero.
		super(uuid, 'C', name, extraField1, 0);
		}
	
	public void setContractAmount(double amount) {
		this.contractAmount = amount;
	}
	
	public double getContractAmount() {
		return contractAmount;
	}
	
	public double calculateTotalCost() {
		// Simply returns the contractAmount (no taxes)
		return contractAmount;
	}
}
