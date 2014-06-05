package eu.ehri.identify_main_identifier;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

		while (xmlEventReaderEAD.hasNext()) {

			XMLEvent event = xmlEventReaderEAD.nextEvent();

			writer.add(event);
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
						    }
						event = xmlEventReaderEAD.nextEvent();
						writer.add(event);
					    }
					}
				}


			if (event.isEndElement()) {
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

		writer.close();
		xmlEventReaderEAD.close();
		return null;
	}

}
