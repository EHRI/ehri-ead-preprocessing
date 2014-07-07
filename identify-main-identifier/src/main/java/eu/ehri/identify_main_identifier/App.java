package eu.ehri.identify_main_identifier;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

/**
 * Detect the kind of identifier used for each node and if it exist, add it as main EHRI identifier
 *
 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
 */
public class App {

    public static final String DID = "DID_ID";
    public static final String UNITID = "UNITID";

    /**
     * usage: 
     * choose one of these:
     * 
     * cmd eadfile DID
     * cmd eadfile UNITID 
     * cmd eadfile UNITID attributeName attributeValue
     * 
     * @param args
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     * @throws IOException 
     */
    public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
        String eadfile = args[0];

        String identifier;
        String attrName=null;
        String attrValue=null;

        if (args.length >= 2) {
            if (args[1].equals(DID)) {
                identifier = DID;
            } else if (args[1].equals(UNITID)) {
                if(args.length == 4){
                    attrName=args[2];
                    attrValue=args[3];
                }
                identifier = UNITID;
            } else {
                System.out.format("unknown identifier-location given, expected %s or %s, was: %s", DID, UNITID, args[1]);
                identifier = DetectIdentifier.detectIdentifier(eadfile);
            }
        } else {
            identifier = DetectIdentifier.detectIdentifier(eadfile);
        }
        System.out.printf("Identifier is %s (%s)\n", identifier, eadfile);
        String outputfile = eadfile.replace(".xml", "_mainids.xml");

        if (identifier.equals(DID)) {
            UseDID_ID_Label.use_did_label(eadfile);
        } else if (identifier.equals(UNITID)) {
            UseUNITID_Tag.use_unitid_tag(eadfile, attrName, attrValue, new FileWriter(outputfile));
        }

    }
}
