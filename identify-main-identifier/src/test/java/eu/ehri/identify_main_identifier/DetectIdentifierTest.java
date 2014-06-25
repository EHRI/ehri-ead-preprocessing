/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehri.identify_main_identifier;

import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;


/**
 *
 * @author linda
 */
public class DetectIdentifierTest {
    
   

    @Test
    public void testDetectIdentifier() throws Exception {
        URL url = ClassLoader.getSystemResource("EAD_30246.xml");
//        System.out.println(url);
        String did = DetectIdentifier.detectIdentifier(url.getPath());
        assertEquals("DID_ID", did);
    }
}
