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

	/**
	 * Parses a CSV file containing person data and converts it into a list of Persons objects.
	 * Each row should contain at least a UUID, first name, and last name.
	 * 
	 * @param 	filePath Path to the CSV file
	 * @return 	List of Persons objects
	 * @throws 	IOException if file reading fails
	 */
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

	/**
	 * Parses a CSV file containing company data and converts in into a list of Companies objects.
	 * Ensures that each company has a valid UUID and associated contact UUID.
	 * 
	 * @param 	filePath Path to the CSV file
	 * @return	List of Companies objects
	 * @throws 	IOException
	 */
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

	/**
	 * Parses a CSV file containing item data and converts it into a list of Items objects.
	 * Ensures that each item has a valid UUID and appropriate values.
	 * 
	 * @param 	filePath Path to the CSV file
	 * @return 	List of Items objects
	 * @throws 	IOException if file reading fails
	 */
	
	public static Item createItem(String[] tokens) {
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
	
	public static List<Item> parseItems(String filePath) throws IOException {
		List<Item> items = new ArrayList<>();
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

			try {
				Item item = createItem(tokens);
				items.add(item);
			} catch (IllegalArgumentException e) {
				System.err.println("Skipping row due to error: " + e.getMessage());
			}

		}
		return items;
	}

}
