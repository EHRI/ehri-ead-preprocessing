package eu.ehri.identify_main_identifier;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class UseUNITID_Tag {

	/**
	 * extract identifiers used by institutions like i.e. Yad Vashem
	 * (M19), where identifier is given as text
	 * in the <did> node(<did>XXX</did>)
	 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
	 */
	
	

	public static String use_unitid_tag(String eadfile)
			throws XMLStreamException, FactoryConfigurationError, IOException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		String outputfile = eadfile.replace(".xml", "_mainids.xml");

		FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
				outputfile));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
				.createXMLEventReader(fileInputStreamEAD);

		XMLEvent end = eventFactory.createDTD("\n");

		String value = "";
		String kind_of_unitid = "";
		while (xmlEventReaderEAD.hasNext()) {

			XMLEvent event = xmlEventReaderEAD.nextEvent();

			writer.add(event);
			
			
			
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("revisiondesc")) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"change"));
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"date"));
					writer.add(eventFactory.createCharacters(dateFormat.format(date)));
					writer.add(eventFactory.createEndElement("", null,
							"date"));
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"item"));
					writer.add(eventFactory
							.createCharacters("EHRI added a unitid with label \"ehri_main_identifier\" to indicate the"
									+ " identifier provided by the institution that EHRI will use as the identifier of"
									+ " the unit."));
					writer.add(eventFactory.createEndElement("", null,
							"item"));
					writer.add(end);
					writer.add(eventFactory.createEndElement("", null,
							"change"));
				}
			}
		
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("unitid")) {
					@SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = event.asStartElement()
							.getAttributes();
					if (!attributes.hasNext()) {
						event = xmlEventReaderEAD.nextEvent();
						writer.add(event);
					    if (event instanceof Characters) {
						      value = event.asCharacters().toString();
						      kind_of_unitid = "noattr";
						    }
						event = xmlEventReaderEAD.nextEvent();
						writer.add(event);
					    }
					//for Bundesarchiv
					else {
						if (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals("encodinganalog")) {
								String type = attribute.getValue().toString();
								event = xmlEventReaderEAD.nextEvent();
								writer.add(event);
							    if (event instanceof Characters) {
							    	if (type.equals("Bestandssignatur")){
								      value = event.asCharacters().toString();
								      kind_of_unitid = "barch";
							    	}
								    }
								event = xmlEventReaderEAD.nextEvent();
								writer.add(event);
								
							}
						}
					}
					// until here specific for bundesarchiv
					}
				}


			if (event.isEndElement()) {
				
				if (kind_of_unitid.equals("barch")){
				if (event.asEndElement().getName().getLocalPart()
						.equals("physdesc")) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_main_identifier"));
					writer.add(eventFactory.createCharacters(value));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
					writer.add(end);

				}
				} else{
				
				if (event.asEndElement().getName().getLocalPart()
						.equals("unittitle")) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_main_identifier"));
					writer.add(eventFactory.createCharacters(value));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
					writer.add(end);

				}
				}
			}

		}

		writer.close();
		xmlEventReaderEAD.close();
		return null;
	}

}
