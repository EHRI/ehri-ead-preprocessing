/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehri.leave_only_one_identifier;

import java.io.StringWriter;
import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;


/**
 *
 * @author linda
 */
public class ExactlyOneMainIdentifierTest {

    @Test
    public void removeMultipleIdentifiers() throws Exception {
        URL url = ClassLoader.getSystemResource("multipleMain.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();
        ExactlyOneMainIdentifier.leaveOneMainIdentifier(url.getPath(), writer);
//        System.out.println("_________________________________");
//System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">CEGESOMA_160202_12</unitid>"));
        assertFalse(writer.toString().contains("<unitid label=\"ehri_main_identifier\">AA 2351</unitid>"));
        assertTrue(writer.toString().contains("<unitid label=\"ehri_multiple_identifier\">AA 2351</unitid>"));
    }
    
    @Test
    public void addMainIdentifier() throws Exception {
        URL url = ClassLoader.getSystemResource("zeroMain.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();

        ExactlyOneMainIdentifier.leaveOneMainIdentifier(url.getPath(), writer);
//        System.out.println("_________________________________");
//        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">CEGESOMA_160202_12</unitid>"));
        assertTrue(writer.toString().contains("<unitid label=\"ehri_internal_id\">12</unitid>"));
        assertFalse(writer.toString().contains("<unitid label=\"ehri_main_identifier\">AA 2351</unitid>"));
        assertFalse(writer.toString().contains("<unitid label=\"ehri_multiple_identifier\">AA 2351</unitid>"));
    }

}
