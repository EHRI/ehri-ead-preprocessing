package eu.ehri.language;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linda
 */
public class AddLanguage {

    public static final String SUFFIX = "_lang";
    public static final String REV_CHANGE = "EHRI added the finding aid language: ";
    static boolean languageAdded = false;
    static boolean revisionChangeAdded = false;

    public static String addLanguage(String eadfile, String language, String langCode)
            throws XMLStreamException, FactoryConfigurationError, IOException {

        String outputfile = eadfile.replace(".xml", SUFFIX + ".xml");


        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(
                outputfile));
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        XMLEvent end = eventFactory.createDTD("\n");


        boolean eventAdded;

        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();
            eventAdded = false;


            if (event.isStartElement()) {
                if (event.asStartElement().getName().getLocalPart().equals("revisiondesc")) {

                    writer.add(event);
                    eventAdded = true;

                    addRevisionChange(eventFactory, writer, end, language);

                }
            }
            /**
             * <profiledesc> <langusage> <language langcode="dut">Nederlands</language> </langusage> </profiledesc>
             */
            if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart().equals("profiledesc")) {


                    addLanguageXml(eventFactory, writer, language, langCode, end);

                    writer.add(event);
                    eventAdded = true;
                } else if (event.asEndElement().getName().getLocalPart().equals("eadheader")) {
                    if (!languageAdded) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "profiledesc"));

                        addLanguageXml(eventFactory, writer, language, langCode, end);

                        writer.add(end);
                        writer.add(eventFactory.createEndElement("", null, "profiledesc"));
                    }
                    if (!revisionChangeAdded) {
                        writer.add(end);
                        writer.add(eventFactory.createStartElement("", null, "revisiondesc"));

                        addRevisionChange(eventFactory, writer, end, language);

                        writer.add(end);
                        writer.add(eventFactory.createEndElement("", null, "revisiondesc"));
                    }

                    writer.add(event);
                    eventAdded = true;
                }

            }

            if (!eventAdded) {
                writer.add(event);
            }
        }
        writer.close();
        return null;

    }

    private static void addLanguageXml(XMLEventFactory eventFactory, XMLEventWriter writer, String language, String langCode, XMLEvent end) throws XMLStreamException {

        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "langusage"));
        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "language"));
        writer.add(eventFactory.createAttribute("langcode", langCode));
        writer.add(eventFactory.createCharacters(language));
        writer.add(eventFactory.createEndElement("", null, "language"));
        writer.add(end);
        writer.add(eventFactory.createEndElement("", null, "langusage"));

        languageAdded = true;

    }

    private static void addRevisionChange(XMLEventFactory eventFactory, XMLEventWriter writer, XMLEvent end, String language) throws XMLStreamException {
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        DateTime date = new DateTime();

        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "change"));
        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "date"));
        writer.add(eventFactory.createCharacters(fmt.print(date)));
        writer.add(eventFactory.createEndElement("", null, "date"));
        writer.add(end);
        writer.add(eventFactory.createStartElement("", null, "item"));
        writer.add(eventFactory.createCharacters(REV_CHANGE + language));
        writer.add(eventFactory.createEndElement("", null, "item"));
        writer.add(end);
        writer.add(eventFactory.createEndElement("", null, "change"));

        revisionChangeAdded = true;

    }
}
