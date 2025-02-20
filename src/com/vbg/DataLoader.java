package com.vbg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataLoader {

	public static void main(String[] args) throws IOException{
		
		List<Persons> persons = CSVParse.parsePersons("data/Persons.csv");
		List<Companies> companies = CSVParse.parseCompanies("data/Companies.csv");
		List<Items> items = CSVParse.parseItems("data/Items.csv");
		
		//Serialize to JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try(FileWriter writer = new FileWriter("data/Persons.json")) {
			gson.toJson(persons, writer);
		}
		try(FileWriter writer = new FileWriter("data/Companies.json")) {
			gson.toJson(companies, writer);
		}
		try(FileWriter writer = new FileWriter("data/Items.json")) {
			gson.toJson(items, writer);
		}
		
		//Serialize to XML
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("person", Persons.class);
		xstream.alias("company", Companies.class);
		xstream.alias("item", Items.class);
		
		xstream.registerConverter(new CollectionConverter(xstream.getMapper()));
		
		try (FileWriter writer = new FileWriter("data/Persons.xml")) {
			writer.write(xstream.toXML(persons));
		}

	}

}
