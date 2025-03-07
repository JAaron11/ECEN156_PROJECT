package com.vgb;

import java.util.UUID;

// Creates a Items class with an ability to call it.

public abstract class Items {
	protected UUID uuid;
	protected char type;
	protected String name;
	protected String extraField1;
	protected String extraField2;
	protected double price;

	public Items(UUID uuid, char type, String name, String extraField1, String extraField2, double price) {
		this.uuid = uuid;
		this.type = type;
		this.name = name;
		this.extraField1 = extraField1;
		this.extraField2 = extraField2;
		this.price = price;
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

	public String getField2() {
		return extraField2;
	}

	public double getPrice() {
		return price;
	}
	
	public abstract double calculateTotalCost(); //Force subclasses to implement cost calculation

	// Output
	public String toString() {
		return "Item{uuid=" + uuid + ", type=" + type + ", name=" + name + "', extraField1=" + extraField1
				+ ", extraField2=" + extraField2 + "'}";
	}
	
	public static Items createItem(String type, String[] data) {
		UUID uuid = UUID.fromString(data[0]);
		String name = data[2];
		double price = data.length > 3 && !data[3].isEmpty() ? Double.parseDouble(data[3]) : 0.0;
		String extra1 = data.length > 4 ? data[4] : null;
		int quantity = data.length > 5 && !data[5].isEmpty() ? Integer.parseInt(data[5]) : 1; // For Material
		
		return switch (type) {
		case "E" -> new Equipment(uuid, name, price, extra1);
		case "M" -> new Material(uuid, name, price, extra1, quantity);
		case "C" -> new Contract(uuid, name, extra1, price);
		default -> throw new IllegalArgumentException("Unknown item type: " + type);
	};
	
	}

}

