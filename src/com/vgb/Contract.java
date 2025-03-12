package com.vgb;

import java.util.UUID;

public class Contract extends Item {

	private double contractAmount;
	private String companyUUID;
	
	public Contract(UUID uuid, char type, String name, String companyUUID, double contractAmount) {
		// since contracts do not utilize extraField2 for the cost, passed as zero.
		super(uuid, 'C', name);
		this.contractAmount = contractAmount;
		this.companyUUID = companyUUID;
		}
	
	public double calculateTotalCost() {
		// Simply returns the contractAmount (no taxes)
		return contractAmount;
	}

	@Override
	public double calculateSubTotal() {
		return Util.roundToTenths(contractAmount + calculateTax());
	}

	@Override
	public double calculateTax() {
		return 0;
	}

	@Override
	public String toString() {
		return "Contract{Service='" + name + "', company='" + companyUUID + "', cost=" + String.format("%.2f", (double) contractAmount) + "}";
	}
}
