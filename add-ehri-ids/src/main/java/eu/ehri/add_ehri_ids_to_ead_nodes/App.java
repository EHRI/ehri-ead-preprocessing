package eu.ehri.add_ehri_ids_to_ead_nodes;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Add an EHRI internal ID to each node of an EAD tree
 * 
 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
 */

public class App {
	public static void main(String[] args) throws XMLStreamException,
			FactoryConfigurationError, IOException {

		String eadfile = args[0];
		String outputfile = eadfile.replace(".xml", "_ehriID.xml");

		FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
				outputfile));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
				.createXMLEventReader(fileInputStreamEAD);

		XMLEvent end = eventFactory.createDTD("\n");
		int counter = 0;
		boolean top = false;
		boolean hashead = LookForHead.hasHeadTag(eadfile);

		while (xmlEventReaderEAD.hasNext()) {
			XMLEvent event = xmlEventReaderEAD.nextEvent();
			writer.add(event);
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
						.equals("head")
						&& top == true) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_internal_id"));
					writer.add(eventFactory.createCharacters("0"));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
					counter++;
				}
			}

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")
						&& top == true && hashead == false) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_internal_id"));
					writer.add(eventFactory.createCharacters("0"));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
					counter++;
				}
			}

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")
						&& top == false) {
					// && head == false) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_internal_id"));
					writer.add(eventFactory.createCharacters(String
							.valueOf(counter)));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
					counter++;
				}
			}
		}

		writer.close();
		xmlEventReaderEAD.close();

	}
}
