package eu.ehri.add_paths_to_ead_nodes;

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
 * Add a path to each node of an EAD tree
 * 
 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
 */

public class App {
	public static void main(String[] args) throws XMLStreamException,
			FactoryConfigurationError, IOException {
		String eadfile = args[0];
		String outputfile = eadfile.replace(".xml", "_wpath.xml");

		FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
				outputfile));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
				.createXMLEventReader(fileInputStreamEAD);
		
		XMLEvent end = eventFactory.createDTD("\n");

		boolean hashead = LookForHeadTag.hasHeadTag(eadfile);
		
		String node = "";
		String top = "0";
		int cntC01 = -1;
		int cntC02 = -1;
		int cntC03 = -1;
		int cntC04 = -1;
		int cntC05 = -1;
		int cntC06 = -1;
		int cntC07 = -1;
		int cntC08 = -1;
		int cntC09 = -1;
		int cntC10 = -1;
		int cntC11 = -1;
		int cntC12 = -1;
		boolean toplevel = false;

		
		while (xmlEventReaderEAD.hasNext()) {
			XMLEvent event = xmlEventReaderEAD.nextEvent();
			writer.add(event);

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("archdesc")) {
					node = "topnode";
					toplevel = true;
				}
			}
			

			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("did")) {
					toplevel = false;
				}
			}
			if (event.isStartElement()) {

				if (event.asStartElement().getName().getLocalPart()
						.equals("c01")) {
					cntC01++;
					node = "c01";
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c02")) {
					node = "c02";
					cntC02++;
				}
			}

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c03")) {
					node = "c03";
					cntC03++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c04")) {
					node = "c04";
					cntC04++;
				}
			}

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c05")) {
					node = "c05";
					cntC05++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c06")) {
					node = "c06";
					cntC06++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c07")) {
					node = "c07";
					cntC07++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c08")) {
					node = "c08";
					cntC08++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c09")) {
					node = "c09";
					cntC09++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c10")) {
					node = "c10";
					cntC10++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c11")) {
					node = "c11";
					cntC11++;
				}
			}
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("c12")) {
					node = "c12";
					cntC12++;
				}
			}

			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart()
						.equals("head")
						&& toplevel == true) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_structure"));
					writer.add(eventFactory.createCharacters(top));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
				}
			}

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")
						&& toplevel == true && hashead == false) {
					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_structure"));
					writer.add(eventFactory.createCharacters(top));
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
				}
			}
				
			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals("did")
						&& toplevel == false) {

					writer.add(end);
					writer.add(eventFactory.createStartElement("", null,
							"unitid"));
					writer.add(eventFactory.createAttribute("label",
							"ehri_structure"));
					if (node.equals("c01")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01));
					}
					if (node.equals("c02")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02));
					}
					if (node.equals("c03")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03));
					}
					if (node.equals("c04")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04));
					}
					if (node.equals("c05")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05));
					}
					if (node.equals("c06")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06));
					}
					if (node.equals("c07")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07));
					}
					if (node.equals("c08")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07 + "." + cntC08));
					}
					if (node.equals("c09")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07 + "." + cntC08 + "." + cntC09));
					}
					if (node.equals("c10")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07 + "." + cntC08 + "." + cntC09 + "."
								+ cntC10));
					}
					if (node.equals("c11")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07 + "." + cntC08 + "." + cntC09 + "."
								+ cntC10 + "." + cntC11));
					}
					if (node.equals("c12")) {
						writer.add(eventFactory.createCharacters(top + "."
								+ cntC01 + "." + cntC02 + "." + cntC03 + "."
								+ cntC04 + "." + cntC05 + "." + cntC06 + "."
								+ cntC07 + "." + cntC08 + "." + cntC09 + "."
								+ cntC10 + "." + cntC11 + "." + cntC12));
					}
					writer.add(eventFactory
							.createEndElement("", null, "unitid"));
				}
			}

			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c01")) {
					cntC02 = 0;

				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c02")) {
					cntC03 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c03")) {
					cntC04 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c04")) {
					cntC05 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c05")) {
					cntC06 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c06")) {
					cntC07 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c07")) {
					cntC08 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c08")) {
					cntC09 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c09")) {
					cntC10 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c10")) {
					cntC11 = 0;
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals("c11")) {
					cntC12 = 0;
				}
			}
		}
		writer.close();
		xmlEventReaderEAD.close();
	}

}
