package com.vgb;

import java.util.UUID;

public class Items {
	private UUID uuid;
	private char type;
	private String name;
	private String extraField1;
	private String extraField2;
	
	public Items(UUID uuid, char type, String name, String extraField1, String extraField2) {
		this.uuid = uuid;
		this.type = type;
		this.name = name;
		this.extraField1 = extraField1;
		this.extraField2 = extraField2;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField1() {
		return extraField1;
	}

	public void setField1(String field1) {
		this.extraField1 = field1;
	}

	public String getField2() {
		return extraField2;
	}

	public void setField2(String field2) {
		this.extraField2 = field2;
	}
	
	public String toString() {
		return "Item{uuid=" + uuid + ", type=" + type + ", name=" + name + "', extraField1=" + extraField1 + ", extraField2=" + extraField2 + "'}";
	}
	
}
