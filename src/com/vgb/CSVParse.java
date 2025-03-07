package com.vgb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVParse {

	// Helper method to safely parse UUIDs (avoid crashes due to invalid UUIDs).
	private static UUID safeParseUUID(String uuidStr) {
		if (uuidStr == null || uuidStr.trim().isEmpty()) {
			System.err.println("Skipping empty UUID field."); // Skips an empty UUID
			return null;
		}
		try {
			return UUID.fromString(uuidStr.trim()); // Makes sure a proper sized UUID has been entered
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid UUID format: " + uuidStr);
			return null;
		}
	}

	public static List<Persons> parsePersons(String filePath) throws IOException {
		List<Persons> persons = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		// Checks if a given row is empty and skips if so.
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (line.isEmpty()) {
				System.err.println("Skipping completely empty row at line " + (i + 1));
				continue;
			}

			String[] tokens = lines.get(i).split(",", -1);

			// Skips completely empty or malformed rows
			if (tokens.length < 3) { 
				System.err.println("Skipping malformed row: " + Arrays.toString(tokens));
				continue;
			}

			UUID uuid = safeParseUUID(tokens[0]);
			if (uuid == null)
				continue;

			String firstName = tokens[1].trim();
			String lastName = tokens[2].trim();
			String phoneNumber = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : null;
			List<String> emails = tokens.length > 4 && !tokens[4].isEmpty() ? Arrays.asList(tokens[4].split(","))
					: new ArrayList<>();

			persons.add(new Persons(uuid, firstName, lastName, phoneNumber, emails));
		}
		return persons;
	}

	public static List<Companies> parseCompanies(String filePath) throws IOException {
		List<Companies> companies = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		for (int i = 1; i < lines.size(); i++) {
			String[] tokens = lines.get(i).split(",", -1);

			UUID companyUuid = safeParseUUID(tokens[0]);
			UUID contactUuid = safeParseUUID(tokens[1]);
			if (companyUuid == null || contactUuid == null)
				continue; // Skips invalid UUIDs

			String name = tokens[2].trim();

			// Ensures all address fields exist before creating the Address object.
			String street = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : "Unknown";
			String city = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4].trim() : "Unknown";
			String state = tokens.length > 5 && !tokens[5].isEmpty() ? tokens[5].trim() : "Unknown";
			String zip = tokens.length > 6 && !tokens[6].isEmpty() ? tokens[6].trim() : "00000";

			Address address = new Address(street, city, state, zip); // Creates an address object

			companies.add(new Companies(companyUuid, contactUuid, name, address));
		}
		return companies;
	}

	public static List<Items> parseItems(String filePath) throws IOException {
		List<Items> items = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (line.isEmpty()) {
				System.err.println("Skipping empty row at line " + (i + 1));
				continue;
			}

			String[] tokens = lines.get(i).split(",", -1);

			if (tokens.length < 3) {
				System.err.println("Skipping malformed item row: " + Arrays.toString(tokens));
				continue;
			}

			UUID uuid = safeParseUUID(tokens[0]);
			if (uuid == null)
				continue; // Skips invalid UUIDs

			char type = tokens[1].charAt(0);
			String name = tokens[2];
			
			String extra1 = null;
			String extra2 = null;
			
			switch (type) {
			case 'E':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : null;
				extra2 = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4].trim() : null;
				items.add(new Equipment(uuid, type, name, extra1, extra2));
				break;
			case 'M':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : null;
				extra2 = tokens.length > 4 && !tokens[4].isEmpty() ? tokens[4].trim() : null;
				items.add(new Material(uuid, type, name, extra1, extra2));
				break;
			case 'C':
				extra1 = tokens.length > 3 && !tokens[3].isEmpty() ? tokens[3].trim() : null;
				items.add(new Contract(uuid, type, name, extra1));
				break;
			default:
				System.err.println("Unknown item type: " + type);
			}

		}
		return items;
	}

}
