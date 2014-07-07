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
public class CegesomaTest {
    
   

    @Test
    public void testDetectIdentifier() throws Exception {
        URL url = ClassLoader.getSystemResource("cegesoma.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();
        
        
        UseUNITID_Tag.use_unitid_tag(url.getPath(), "label", "Identificatie:", writer);
//        System.out.println(writer.toString());
        
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">AA 2249</unitid>"));
    }
}
