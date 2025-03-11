package com.vgb;

import java.util.UUID;

public class Contract extends Item {

	private double contractAmount = 0.0;
	
	public Contract(UUID uuid, char type, String name, String extraField1) {
		// since contracts do not utilize extraField2 for the cost, passed as zero.
		super(uuid, 'C', name, extraField1, 0);
		}
	
	public double getContractAmount() {
		return contractAmount;
	}
	
	public double calculateTotalCost() {
		// Simply returns the contractAmount (no taxes)
		return contractAmount;
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
