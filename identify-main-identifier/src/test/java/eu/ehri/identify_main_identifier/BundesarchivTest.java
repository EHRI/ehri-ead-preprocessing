/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehri.identify_main_identifier;

import java.io.StringWriter;
import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;


/**
 *
 * @author linda
 */
public class BundesarchivTest {
    
   

    @Test
    public void testDetectIdentifier() throws Exception {
        URL url = ClassLoader.getSystemResource("bundesarchiv.xml");
        System.out.println(url);
                StringWriter writer = new StringWriter();

        UseUNITID_Tag.use_unitid_tag(url.getPath(), "encodinganalog", "Bestandssignatur", writer);
    }
}
