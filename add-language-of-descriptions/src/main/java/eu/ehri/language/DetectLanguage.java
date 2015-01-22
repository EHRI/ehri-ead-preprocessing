package eu.ehri.language;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author linda
 */
public class DetectLanguage {
    
    
     public static boolean languageDetected(String eadfile, String language, String langCode)
            throws XMLStreamException, FactoryConfigurationError, IOException {



        FileInputStream fileInputStreamEAD = new FileInputStream(eadfile);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEventReader xmlEventReaderEAD = XMLInputFactory.newInstance()
                .createXMLEventReader(fileInputStreamEAD);

        XMLEvent end = eventFactory.createDTD("\n");

        String value = "";

        boolean inLangUsage=false;
        boolean languageAlreadyAvailable = false;
        boolean langusageFound = false;

        while (xmlEventReaderEAD.hasNext()) {
            XMLEvent event = xmlEventReaderEAD.nextEvent();

            /**
             * <profiledesc> 
             *   <langusage> 
             *     <language langcode="dut">Nederlands</language> 
             *   </langusage> 
             * </profiledesc>
             */
            if (event.isStartElement()) {
                if (event.asStartElement().getName().getLocalPart().equals("langusage")){
                    inLangUsage = true;
                } 
                if (event.asStartElement().getName().getLocalPart()
                        .equals("language") && inLangUsage) {
                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("langcode")) {
                            languageAlreadyAvailable=true;
                        }
                    }

                }
            }



        }
        return languageAlreadyAvailable;

    }
}
