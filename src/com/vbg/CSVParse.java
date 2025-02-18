package com.vbg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVParse {
	
	public static List<Persons> parsePersons(String filePath) throws IOException {
		List<Persons> persons = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		
		for(int i = 1; i < lines.size(); i++) {
			String[] tokens = lines.get(i).split(",", -1);
			UUID uuid = UUID.fromString(tokens[0]);
			String firstName = tokens[1];
			String lastName = tokens[2];
			String phoneNumber = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3] : null;
			List<String> emails = tokens.length > 4 && !tokens[4].isEmpty() ? Arrays.asList(tokens[4].split(",")) : new ArrayList<>();
			
			persons.add(new Persons(uuid, firstName, lastName, phoneNumber, emails));
		}
		return persons;
	}
	
	public static List<Companies> parseCompanies(String filePath) throws IOException {
		List<Companies> companies = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		
		for(int i = 1; i < lines.size(); i++) {
			String[] tokens = lines.get(i).split(",", -1);
			
			UUID companyUuid = UUID.fromString(tokens[0]);
			UUID contactUuid = UUID.fromString(tokens[1]);
			String name = tokens[2];
			
			String street = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3] : "Unknown";
			String city = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4] : "Unknown";
			String state = tokens.length > 5 && !tokens[5].isEmpty() ? tokens[5] : "Unknown";
			String zip = tokens.length > 6 && !tokens[6].isEmpty() ? tokens[6] : "00000";
			
			Address address = new Address(street, city, state, zip); //Creates and address object
			
			companies.add(new Companies(companyUuid, contactUuid, name, address));
		}
		return companies;
	}
	
	public static List<Items> parseItems(String filePath) throws IOException {
		List<Items> items = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		
		for(int i = 1; i < lines.size(); i++) {
			String[] tokens = lines.get(i).split(",", -1);
			UUID uuid = UUID.fromString(tokens[0]);
			char type = tokens[1].charAt(0);
			String name = tokens[2];
			String extra1 = null;
			String extra2 = null;
			
			switch(type) {
			case 'E':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3] : null;
				extra2 = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4] : null;
				break;
			case 'M':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3] : null;
				extra2 = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4] : null;
				break;
			case 'C':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3] : null;
				break;
			default:
				System.err.println("Unknown item type: " + type);
			}
			
			items.add(new Items(uuid, type, name, extra1, extra2));
		}
		return items;
	}

}
