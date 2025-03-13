package com.vgb;

import java.util.UUID;

public class Rental extends Item {

	protected String model;
	protected double rentalCost;
	private double hours;

	/**
	 * Constructs a Rental instance.
	 * 
	 * @param uuid        The unique identifier.
	 * @param type        The item type (should be 'R' for rental).
	 * @param name        The equipment name.
	 * @param extraField1 Typically, the model or description.
	 * @param rentalCost  The base cost (price) of the equipment.
	 * @param hours       The number of rental hours.
	 */
	public Rental(UUID uuid, char type, String name, String model, double rentalCost, double hours) {
		super(uuid, type, name);
		this.model = model;
		this.rentalCost = rentalCost;
		this.hours = hours;
	}

	/**
	 * Calculates the total rental cost including tax. Rental cost is determined
	 * based on 0.1% of the base cost per hour. A tax rate of 4.38% is applied to
	 * the rental cost.
	 */

	@Override
	public double calculateTotalCost() {
		double baseCost = rentalCost; // equipment's base cost
		double perHourCharge = baseCost * 0.001;
		double subTotal = perHourCharge * hours;
		double tax = subTotal * 0.0438;
		double total = subTotal + tax;
		return Util.roundToTenths(total);
	}

	// Rental cost before taxes
	public double calculateSubTotal() {
		double subTotal = rentalCost; // base equipment cost stored as int in extraField2
		double perHourCharge = subTotal * 0.001;
		double rentalCost = perHourCharge * hours;
		return Util.roundToTenths(rentalCost);
	}

	@Override
	public double calculateTax() {
		double subTotal = calculateSubTotal();
		double tax = subTotal * 0.0438;
		return Util.roundToTenths(tax);
	}

	@Override
	public String toString() {
		return "Rental{name='" + name + "', model='" + model + "', baseCost=" + rentalCost + ", hours=" + hours + "}";
	}
}
