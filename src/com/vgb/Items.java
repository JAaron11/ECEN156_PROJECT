package com.vgb;

import java.util.UUID;

/**
 * Abstract base class representing an item with common attributes and behavior.
 */

public abstract class Items {
	protected UUID uuid;
	protected char type;
	protected String name;
	protected String extraField1;
	protected double extraField2;
	protected double price;

	public Items(UUID uuid, char type, String name, String extraField1, double extraField2) {
		this.uuid = uuid;
		this.type = type;
		this.name = name;
		this.extraField1 = extraField1;
		this.extraField2 = extraField2;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public char getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getField1() {
		return extraField1;
	}

	public double getField2() {
		return extraField2;
	}

	public double getPrice() {
		return price;
	}
	
	public abstract double calculateTotalCost(); //Force subclasses to implement cost calculation

	// Output to string
	public String toString() {
		return "Item{uuid=" + uuid + ", type=" + type + ", name=" + name + "', extraField1=" + extraField1
				+ ", extraField2=" + extraField2 + "'}";
	}
	
	/**
	 * Factory method to create an Item instance based on the type.
	 * @param tokens Array of strings representing item attributes
	 * @return An instance of the appropriate Item subclass
	 * @throws IllegalArgumentException if the item type is unknown
	 */
	public static Items createItem(String[] tokens) {
		UUID uuid = UUID.fromString(tokens[0]);
		char type = tokens[1].charAt(0);
		String name = tokens[2];
		String extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : null;
		String extra2Str = tokens.length > 4 ? tokens [4] : "";
		int extra2 = (extra2Str != null && !extra2Str.trim().isEmpty())
					 ? Integer.parseInt(extra2Str.trim())
					 : 0;
		
		switch (type) {
		case 'E':
			return new Equipment(uuid, type, name, extra1, extra2);
		case 'M':
			return new Equipment(uuid, type, name, extra1, extra2);
		case 'C':
			return new Equipment(uuid, type, name, extra1, 0);
		default:
			throw new IllegalArgumentException("Unknown item type: " + type);
		}
	}
}


