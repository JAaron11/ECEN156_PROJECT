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
	private static final String DEFAULT_DATA_FOLDER = "data"; // Default data folder
	private static final String EXTRA_DATA_FOLDER = "extraData";// Extra data folder
	private static String activeFolder = DEFAULT_DATA_FOLDER; // Active data folder

	public static void main(String[] args) throws IOException {

		List<Persons> persons = CSVParse.parsePersons(activeFolder + "/Persons.csv");
		List<Companies> companies = CSVParse.parseCompanies(activeFolder + "/Companies.csv");
		List<Items> items = CSVParse.parseItems(activeFolder + "/Items.csv");

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
		xstream.alias("person", Persons.class);
		xstream.alias("company", Companies.class);
		xstream.alias("item", Items.class);

		xstream.registerConverter(new CollectionConverter(xstream.getMapper()));

		try (FileWriter writer = new FileWriter(activeFolder + "/Persons.xml")) {
			writer.write(xstream.toXML(persons));
		}

	}

}
