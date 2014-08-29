package eu.ehri.relativize_identifiers;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.Stack;
import java.util.regex.Pattern;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;


public class RelativizeIdentifiers {

    public final static String SUFFIX = "_relid.xml";

    static XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    static XMLOutputFactory factory = XMLOutputFactory.newInstance();


    public static void main(String[] args) throws XMLStreamException, javax.xml.stream.FactoryConfigurationError, IOException {
        String eadfile = args[0];
        String outputfile = eadfile.replace(".xml", SUFFIX);
        RelativizeIdentifiers.relativizeIdentifiers(eadfile, new FileWriter(outputfile));
    }

    /**
     * precondition: The EAD file has absolute identifiers, where unitids in each c-level
     * include the full ID of their parent unitid
     * postcondition: The EAD file has relative identifiers.
     *
     * @param eadfile the name of the ead file
     * @throws javax.xml.stream.XMLStreamException
     * @throws javax.xml.parsers.FactoryConfigurationError
     * @throws java.io.IOException
     */
    public static String relativizeIdentifiers(String eadfile, Writer outputWriter)
            throws XMLStreamException, FactoryConfigurationError, IOException {

        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLEventWriter writer = factory.createXMLEventWriter(outputWriter);

        Stack<String> idStack = new Stack<String>();
        Pattern childPattern = Pattern.compile("c\\d\\d");

        String thisId;

        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance().createXMLEventReader(fileInputStreamEAD);
        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();

            if (event.isStartElement()) {
                if (event.asStartElement().getName().getLocalPart().equals("unitid")) {
                    writer.add(event);
                    XMLEvent nextEvent = xmlEventReaderEAD.nextEvent();
                    if (nextEvent.isCharacters()) {
                        thisId = nextEvent.asCharacters().getData();
                        if (!idStack.empty() && thisId.contains(idStack.peek())) {
                            // Replace the ID and any non-ID trailing chars, such as spaces,
                            // colons, or dashes.
                            String regex = "^" + Pattern.quote(idStack.peek()) + "[\\s\\-:_\\/]*";
                            String newId = thisId.replaceFirst(regex, "");
                            System.out.println("ID: " + thisId + " -> " + newId);
                            Characters chars = eventFactory.createCharacters(newId);
                            writer.add(chars);
                        } else {
                            writer.add(nextEvent);
                        }
                        idStack.push(thisId);
                    }
                } else {
                    writer.add(event);
                }
            } else if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart()
                        .matches(childPattern.pattern())) {
                    idStack.pop();
                }
                writer.add(event);
            } else {
                writer.add(event);
            }
        }

        writer.close();
        xmlEventReaderEAD.close();
        return null;
    }
}
