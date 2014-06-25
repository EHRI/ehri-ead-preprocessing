/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehri.language;

import java.io.IOException;
import java.net.URL;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author linda
 */
public class DetectLanguageTest {
    
    @Test
    public void detectNoLanguage() throws XMLStreamException, FactoryConfigurationError, IOException {
        URL url = ClassLoader.getSystemResource("EAD_30246.xml");
//        System.out.println(url);
        assertFalse(DetectLanguage.languageDetected(url.getPath(), "Nederlands", "dut"));

    }
     @Test
    public void detectLanguage() throws XMLStreamException, FactoryConfigurationError, IOException {
        URL url = ClassLoader.getSystemResource("EAD_30246_withDutLang.xml");
        System.out.println(url);
        assertNotNull(url);
        assertTrue(DetectLanguage.languageDetected(url.getPath(), "Nederlands", "dut"));

    }
}
