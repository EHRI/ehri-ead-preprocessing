package eu.ehri.identify_main_identifier;

import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

/**
 * Detect the kind of identifier used for each node and
 * if it exist, sadd it as main EHRI identifier 
 * 
 * @author Kepa J. Rodriguez (https://github.com/KepaJRodriguez)
 */

public class App {
	public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
		System.out.println("Hello World!");
		String eadfile = args[0];

		String identifier = DetectIdentifier.detectIdentifier(eadfile);
		System.out.println();
		System.out.println("DETECTED");
		System.out.println("Identifier is " + identifier);
		System.out.println();
		
		if (identifier.equals("DID_ID")){
			UseDID_ID_Label.use_did_label(eadfile);
		}
		if (identifier.equals("UNITID")){
			UseUNITID_Tag.use_unitid_tag(eadfile);
		}
		
	}
}
