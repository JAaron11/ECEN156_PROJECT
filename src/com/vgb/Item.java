package com.vgb;

import java.util.UUID;

/**
 * Abstract base class representing an item with common attributes and behavior.
 */

public abstract class Item {

	protected UUID uuid;
	protected String name;
	protected String model;
	

	public Item(UUID uuid, char type, String name) {
		this.uuid = uuid;
		this.name = name;
		this.model = model;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	public String getModel() {
		return model;
	}

	/**
	 * Forces all subclasses of item to implement each type of calculation and
	 * toString methods.
	 * 
	 * @return
	 */

	public abstract double calculateSubTotal();

	public abstract double calculateTax();

	public abstract double calculateTotalCost();

	public abstract String toString();
}