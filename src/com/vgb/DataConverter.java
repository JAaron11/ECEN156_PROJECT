package com.vgb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataConverter {

	// Default and alternate data folders
	private static final String DEFAULT_DATA_FOLDER = "data"; 
	private static final String EXTRA_DATA_FOLDER = "extraData";
	private static String activeFolder = EXTRA_DATA_FOLDER;

	public static void main(String[] args) throws IOException {

		List<Person> persons = CSVDataLoader.parsePersons(activeFolder + "/Persons.csv");
		List<Companies> companies = CSVDataLoader.parseCompanies(activeFolder + "/Companies.csv");
		List<Item> items = CSVDataLoader.parseItems(activeFolder + "/Items.csv");

		// Serialize to JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(activeFolder + "/Persons.json")) {
			gson.toJson(persons, writer);
		}
		try (FileWriter writer = new FileWriter(activeFolder + "/Companies.json")) {
			gson.toJson(companies, writer);
		}
		try (FileWriter writer = new FileWriter(activeFolder + "/Items.json")) {
			gson.toJson(items, writer);
		}

		// Serialize to XML
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("person", Person.class);
		xstream.alias("company", Companies.class);
		xstream.alias("item", Item.class);

		xstream.registerConverter(new CollectionConverter(xstream.getMapper()));

		try (FileWriter writer = new FileWriter(activeFolder + "/Persons.xml")) {
			writer.write(xstream.toXML(persons));
		}

	}

}
