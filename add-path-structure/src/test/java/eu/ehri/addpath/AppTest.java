package eu.ehri.addpath;

import eu.ehri.add_paths_to_ead_nodes.App;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    

    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp() throws XMLStreamException, FactoryConfigurationError, IOException{
        URL url = ClassLoader.getSystemResource("its-gestapo.xml");
//        System.out.println(url);
        StringWriter writer = new StringWriter();
        App.addPath(url.getPath(), writer);
        
//        System.out.println(writer.toString());
    }
}
