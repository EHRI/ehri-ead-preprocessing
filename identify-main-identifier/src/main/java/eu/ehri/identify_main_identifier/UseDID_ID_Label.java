package eu.ehri.identify_main_identifier;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

public class UseDID_ID_Label {

	/**
	 * extract identifiers used by institutionen like City
	 * Archives Amsterdam, where identifier is given as attribute
	 * in the <did> node(<did id="xxxx">
	 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
	 */
	
	// for instance archives amsterdam

	public static String use_did_label(String eadfile)
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

		boolean top = false;
		
		while (xmlEventReaderEAD.hasNext()) {
			XMLEvent event = xmlEventReaderEAD.nextEvent();

			writer.add(event);

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")) {
					@SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = event.asStartElement()
							.getAttributes();
					while (attributes.hasNext()) {
						Attribute attribute = attributes.next();
						if (attribute.getName().toString().equals("id")) {
							value = attribute.getValue();
						}
					}

				}
			}
	
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("archdesc")) {
					top = true;
				}
			}
	
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("did")) {
					top = false;
				}
			}


				
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart()
						.equals("head")) {
					if (top == true) {
						event = xmlEventReaderEAD.nextEvent();
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"unitid"));
						writer.add(eventFactory.createAttribute("label",
								"ehri_main_identifier"));
						writer.add(eventFactory.createCharacters(value));
						writer.add(eventFactory.createEndElement("", null,
								"unitid"));
						writer.add(end);
					}
				}
			}
 
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")) {
					if (top == false) {
						event = xmlEventReaderEAD.nextEvent();
						writer.add(end);
						writer.add(eventFactory.createStartElement("", null,
								"unitid"));
						writer.add(eventFactory.createAttribute("label",
								"ehri_main_identifier"));
						writer.add(eventFactory.createCharacters(value));
						writer.add(eventFactory.createEndElement("", null,
								"unitid"));
						writer.add(end);
					}
				}
			} 

		}
		writer.close();
		return null;

	}

}
