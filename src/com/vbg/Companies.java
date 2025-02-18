package com.vbg;

import java.util.UUID;

public class Companies {

	private UUID companyUuid;
	private UUID contactUuid;
	private String name;
	private Address address;
	
	public Companies(UUID companyUuid, UUID contactUuid, String name, Address address) {
		this.companyUuid = companyUuid;
		this.contactUuid = contactUuid;
		this.name = name;
		this.address = address;
	}

	public UUID getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(UUID companyUuid) {
		this.companyUuid = companyUuid;
	}

	public UUID getContactUuid() {
		return contactUuid;
	}

	public void setContactUuid(UUID contactUuid) {
		this.contactUuid = contactUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String toString() {
		return "Company{name='" + name + "', address=" + address + "}";
	}
	
}
