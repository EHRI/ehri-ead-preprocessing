package eu.ehri.identify_main_identifier;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
     * extract identifiers used by institutions like i.e. Yad Vashem (M19), where identifier is given as text in the
     * <did> node(<did>XXX</did>)
     *
     * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
     */
    private static final String NOATTR = "noattr";
    private static final String ATTRVALUE = "attr";
    static String value = null;
    static String kind_of_unitid = null;

    /**
     * Bundesarchiv: <unitid encodinganalog="Bestandssignatur">the unitid given by the institute</unitid>
     *
     * Cegesoma: <unitid label="Identificatie:">the unitid given by the institute</unitid>
     *
     * both will be extended with a <unitid label="ehri_main_identifier">the unitid given by the institute</unitid>
     *
     * @param eadfile the name of the ead file
     * @param unitidAttribute the attribute that denotes the unitid that should be used
     * @param unitidAttributeValue the value of the attribute that signifies this unitid should be used
     * @return
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws IOException
     */
    public static String use_unitid_tag(String eadfile, String unitidAttribute, String unitidAttributeValue, Writer outputWriter)
            throws XMLStreamException, FactoryConfigurationError, IOException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();


        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLEventWriter writer = factory.createXMLEventWriter(outputWriter);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        XMLEvent end = eventFactory.createDTD("\n");

        while (xmlEventReaderEAD.hasNext()) {

            XMLEvent event = xmlEventReaderEAD.nextEvent();
            /** i'd prefer to keep the writer.add(event) close to the nextEvent() call. 
              * however, if this is the did end-tag, we want to add the main-identifier before, so we cannot write it yet
              * every block below MUST have its own writer.add(event) call
              * and if there is a nextEvent, the writer.add(event) SHOULD be next to it
             **/

            if (event.isStartElement()) {
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
                    writer.add(eventFactory
                            .createCharacters("EHRI added a unitid with label \"ehri_main_identifier\" to indicate the"
                            + " identifier provided by the institution that EHRI will use as the identifier of"
                            + " the unit."));
                    writer.add(eventFactory.createEndElement("", null, "item"));
                    writer.add(end);
                    writer.add(eventFactory.createEndElement("", null, "change"));
                }

                if (event.asStartElement().getName().getLocalPart().equals("unitid")) {
                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();

                    event = xmlEventReaderEAD.nextEvent();
                    writer.add(event);
                    if (unitidAttribute != null && unitidAttributeValue != null && !unitidAttribute.isEmpty() && !unitidAttributeValue.isEmpty()) {
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            //for unitidAttribute && unitidAttributeValue given
                            if (attribute.getName().toString().equals(unitidAttribute)) {
                                String type = attribute.getValue().toString();
                                if (event instanceof Characters) {
                                    if (type.equals(unitidAttributeValue)) {
                                        value = event.asCharacters().toString();
                                        kind_of_unitid = ATTRVALUE;
                                    }
                                }
                            }
                        }
                    }
                    if (kind_of_unitid == null || kind_of_unitid.equals(NOATTR)) {
                        boolean isEhriCreatedIdentifier = false;
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("label") && attribute.getValue().toString().startsWith("ehri_")) {
                                isEhriCreatedIdentifier = true;
                            }
                        }
                        if ((!isEhriCreatedIdentifier) && event instanceof Characters) {
                            value = event.asCharacters().toString();
                            kind_of_unitid = NOATTR;
                        }
                    }
                }
            } else {
                //only add the unitid at this point if the value has been set, and not yet been processed:
                if (event.isEndElement() && value != null && (!value.isEmpty())) {
                    if (ATTRVALUE.equals(kind_of_unitid)) {
                        if (event.asEndElement().getName().getLocalPart().equals("unitid")) {
                            //write the close </unitid> first, then create the new one
                            writer.add(event);

                            writer.add(end);
                            writer.add(eventFactory.createStartElement("", null, "unitid"));
                            writer.add(eventFactory.createAttribute("label", "ehri_main_identifier"));
                            writer.add(eventFactory.createCharacters(value));
                            writer.add(eventFactory.createEndElement("", null, "unitid"));
                            writer.add(end);

                            resetMainIdValues();
                        }
                    } else if (event.asEndElement().getName().getLocalPart().equals("did")) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "unitid"));
                        writer.add(eventFactory.createAttribute("label", "ehri_main_identifier"));
                        writer.add(eventFactory.createCharacters(value));
                        writer.add(eventFactory.createEndElement("", null, "unitid"));
                        writer.add(end);

                        //write the close </did> last
                        writer.add(event);

                        resetMainIdValues();
                    }else{
                      // not the closing did, and the given attribute has not been detected yet 
                      writer.add(event);                      
                    }
                } else {
                    //not an end-tag, or we have not yet found the main-identifier
                    writer.add(event);
                }
            }
        }

        writer.close();
        xmlEventReaderEAD.close();
        return null;
    }

    private static void resetMainIdValues() {
        value = null;
        kind_of_unitid = null;
    }
}
