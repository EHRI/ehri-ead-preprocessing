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
public class AddMainIdentifierTest {

    @Test
    public void cegesoma() throws Exception {
        URL url = ClassLoader.getSystemResource("cegesoma.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();
        UseUNITID_Tag.use_unitid_tag(url.getPath(), "label", "Identificatie:", writer);
//        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">AA 2249</unitid>"));
    }

    @Test
    public void bundesarchive() throws Exception {
        URL url = ClassLoader.getSystemResource("bundesarchiv.xml");
        System.out.println(url);
        StringWriter writer = new StringWriter();

        UseUNITID_Tag.use_unitid_tag(url.getPath(), "encodinganalog", "Bestandssignatur", writer);
    }

    @Test
    public void itsGestapo() throws Exception {
        URL url = ClassLoader.getSystemResource("its-gestapo.xml");
        System.out.println(url);
        StringWriter writer = new StringWriter();

        UseUNITID_Tag.use_unitid_tag(url.getPath(), "type", "bestellnummer", writer);
        
        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid label=\"ehri_internal_id\">0</unitid>"));
        assertTrue(writer.toString().contains("<unitid type=\"bestellnummer\">R 2 8687</unitid>"));
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">R 2 8687</unitid>"));
    }
    @Test
    @Ignore //TODO!
    public void itsGestapoWithoutParams() throws Exception {
        URL url = ClassLoader.getSystemResource("its-gestapo.xml");
        System.out.println(url);
        StringWriter writer = new StringWriter();

        UseUNITID_Tag.use_unitid_tag(url.getPath(), "", "", writer);
        
        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid label=\"ehri_internal_id\">0</unitid>"));
        assertTrue(writer.toString().contains("<unitid type=\"bestellnummer\">R 2 8687</unitid>"));
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">R 2 8687</unitid>"));
    }
    @Test
    public void itsEsterwegen() throws Exception {
        URL url = ClassLoader.getSystemResource("its-esterwegen.xml");
        System.out.println(url);
        StringWriter writer = new StringWriter();

        UseUNITID_Tag.use_unitid_tag(url.getPath(), "", "", writer);
        
        System.out.println(writer.toString());
        assertTrue(writer.toString().contains("<unitid type=\"refcode\">DE ITS [OuS 1.1.7]</unitid>"));
        assertTrue(writer.toString().contains("<unitid label=\"ehri_main_identifier\">DE ITS [OuS 1.1.7]</unitid>"));
    }
}
