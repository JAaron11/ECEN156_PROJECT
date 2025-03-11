package com.vgb;

import java.util.UUID;

/**
 * Abstract base class representing an item with common attributes and behavior.
 */

public abstract class Item {
	protected UUID uuid;
	protected String name;
	protected double price;
	protected double tax;

	public Item(UUID uuid, char type, String name, String extraField1, double extraField2) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public abstract double getPrice();
	
	public abstract double getTax();
	
	public abstract double calculateTotalCost(); //Force subclasses to implement cost calculation

	// Output to string
	public String toString() {
		return "Item{uuid=" + uuid + ", type=" + type + ", name=" + name + "', extraField1=" + extraField1
				+ ", extraField2=" + extraField2 + "'}";

	}
}