package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Creates a Persons class with a means of calling it.

public class Persons {

	private UUID uuid;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private List<String> emails;

	public Persons(UUID uuid, String firstName, String lastName, String phoneNumber, List<String> emails) {
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emails = new ArrayList<>(emails);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

}
