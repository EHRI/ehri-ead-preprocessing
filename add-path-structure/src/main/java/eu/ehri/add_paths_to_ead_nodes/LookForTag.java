/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehri.add_paths_to_ead_nodes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author linda
 */
public class LookForTag {
    public static boolean hasTag(String eadfile, String tag) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError{
		boolean result = false;
		
		FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);

		XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
				.createXMLEventReader(fileInputStreamEAD);
		
		while (xmlEventReaderEAD.hasNext()) {
			XMLEvent event = xmlEventReaderEAD.nextEvent();

			if (event.isStartElement()) {
				if (event.asStartElement().getName().getLocalPart()
						.equals(tag)) {
					result = true;
				}
			}
		}
		
		return result;
	}
}
