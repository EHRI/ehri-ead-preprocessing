package eu.ehri.leave_only_one_identifier;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import java.util.List;
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
import org.apache.commons.io.IOUtils;

public class ExactlyOneMainIdentifier {

    public final static String SUFFIX = "_oneid.xml";
    private final static String LABEL = "label",
            MAINID = "ehri_main_identifier",
            INTERNALID = "ehri_internal_id",
            MULTIPLEMAINID = "ehri_multiple_identifier";
    static List<String> mainIdentifiers = new ArrayList<String>();
    static String internalIdentifier = null;
    static XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    static XMLOutputFactory factory = XMLOutputFactory.newInstance();
    static XMLEventWriter unitids;
    static StringWriter unitid;


    public static void main(String[] args) throws XMLStreamException, javax.xml.stream.FactoryConfigurationError, IOException {
        String eadfile = args[0];
        String outputfile = eadfile.replace(".xml", SUFFIX);
        ExactlyOneMainIdentifier.leaveOneMainIdentifier(eadfile, new FileWriter(outputfile));
    }

    /**
     * precondition: the eadfile has exactly one ehri_internal_id per did and zero or more ehri_main_identifiers
     * postcondition: the resulting file will have exactly one ehri_main_identifier
     *
     * @param eadfile the name of the ead file
     * @return
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws IOException
     */
    public static String leaveOneMainIdentifier(String eadfile, Writer outputWriter)
            throws XMLStreamException, FactoryConfigurationError, IOException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLEventWriter writer = factory.createXMLEventWriter(outputWriter);
        XMLEvent end = eventFactory.createDTD("\n");
        
        resetMainIdValues();

        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance().createXMLEventReader(fileInputStreamEAD);
        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();
            if (event.isStartElement()) {
                //if this is the unitid, we cannot simple copy it, but must rebuild it to overwrite the existing label
                if (event.asStartElement().getName().getLocalPart().equals("unitid")) {
                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                    
                    //if label="ehri_main_identifier", write to unitids, else write to 'normal' writer
                    XMLEventWriter unitWriter = writer;
                    while(attributes.hasNext()){
                        Attribute attribute = attributes.next();
                        String type = attribute.getValue().toString();
                         if (attribute.getName().toString().equals(LABEL) &&  type.equals(MAINID)) {
                             unitWriter=unitids;
                         }
                    }
                    //we need to process the attributes again, now to write them
                    attributes = event.asStartElement().getAttributes();
                    unitWriter.add(eventFactory.createStartElement("", null, "unitid"));
                    event = xmlEventReaderEAD.nextEvent();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals(LABEL)) {
                            String type = attribute.getValue().toString(); //value of the attribute
                            if (event instanceof Characters) {
                                if (type.equals(MAINID)) {
                                    //write this unitid with a different label
                                    mainIdentifiers.add(event.asCharacters().toString());
                                    unitWriter.add(eventFactory.createAttribute(LABEL, MULTIPLEMAINID));
                                } else {
                                    //otherwise, use the given label
                                    unitWriter.add(eventFactory.createAttribute(LABEL, type));
                                    if (type.equals(INTERNALID)) {
                                        internalIdentifier = event.asCharacters().toString();
                                    }
                                }
                                unitWriter.add(event.asCharacters());
                                
                            }
                        } else {
                            //not a LABEL
                            unitWriter.add(eventFactory.createAttribute(attribute.getName().toString(), attribute.getValue().toString()));
                        }
                    }
                    event = xmlEventReaderEAD.nextEvent();
                    unitWriter.add(event);
                    //last write to temporary unitwriter, back to the normal one (if we were indeed writing to the unitwriter ...)
                } else {
                    writer.add(event);
                    if (event.asStartElement().getName().getLocalPart().equals("revisiondesc")) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "change"));
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "date"));
                        writer.add(eventFactory.createCharacters(dateFormat.format(date)));
                        writer.add(eventFactory.createEndElement("", null, "date"));
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "item"));
                        String provenance = String.format("EHRI has choosen the %s to be the unitid with %s %s if none were given. If multiple %s were given, their %s was renamed to %s", INTERNALID, LABEL, MAINID, MAINID, LABEL, MULTIPLEMAINID);
                        writer.add(eventFactory.createCharacters(provenance));
                        writer.add(eventFactory.createEndElement("", null, "item"));
                        writer.add(end);
                        writer.add(eventFactory.createEndElement("", null, "change"));
                    }
                }
            //only add the unitid at this point if the value has been set, and not yet been processed:
            } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("did")) {
                writer.add(end);
                if (mainIdentifiers.size() > 1) {
                    unitids.add(eventFactory.createEndElement("", null, "ids"));
                    unitids.add(eventFactory.createEndDocument());
                    unitids.flush();
                    //print the stuff put in the unitids-writer, these are the former ehri_main_identifiers, now transformed to ehri_multiple_identifiers
                    XMLEventReader unitReader = XMLInputFactory.newInstance().createXMLEventReader(IOUtils.toInputStream(unitid.toString(), "UTF-8"));
                    while (unitReader.hasNext()) {
                        XMLEvent unitEvent = unitReader.nextEvent();
                        if (unitEvent.isStartDocument() || unitEvent.isEndDocument()) {
                            ; // do nothing
                        } else if (unitEvent.isStartElement() && unitEvent.asStartElement().getName().getLocalPart().equals("ids")) {
                            ; //do nothing
                        } else if (unitEvent.isEndElement() && unitEvent.asEndElement().getName().getLocalPart().equals("ids")) {
                            ; //do nothing
                        } else {
                            writer.add(unitEvent);
                        }
                    }
                }
                //now write the actual ehri_main_identifier
                writer.add(eventFactory.createStartElement("", null, "unitid"));
                writer.add(eventFactory.createAttribute("label", MAINID));
                if (mainIdentifiers.size() == 1) {
                    //create this one mainIdentifier again
                    writer.add(eventFactory.createCharacters(mainIdentifiers.get(0)));
                } else {
                    //make the internalIdentifier the new mainIdentifier
                    writer.add(eventFactory.createCharacters(internalIdentifier));
                }
                writer.add(eventFactory.createEndElement("", null, "unitid"));
                writer.add(end);
                writer.add(event); //close the did
                resetMainIdValues();

            } else {
                writer.add(event);
            }
        }

        writer.close();
        xmlEventReaderEAD.close();
        return null;
    }

    private static void resetMainIdValues() throws XMLStreamException {
        mainIdentifiers.clear();
        internalIdentifier = null;
        unitid = new StringWriter();
        unitids = factory.createXMLEventWriter(unitid);
        unitids.add(eventFactory.createStartDocument());
        unitids.add(eventFactory.createStartElement("", null, "ids"));

    }
}
