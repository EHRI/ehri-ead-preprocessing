package eu.ehri.relativize_identifiers;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author Mike Bryant (http://github.com/mikesname)
 */
public class RelativizeIdentifiersTest {

    DocumentBuilder builder;
    XPath xpath;

    @Before
    public void setUp() throws Exception {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        xpath = XPathFactory.newInstance().newXPath();
    }

    @Test
    public void testRelativizeIdentifiersWithSpaces() throws Exception {
        Document outDoc = getOutputDocument("/absoluteids-spaces.xml");
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/c03/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/c03/did/unitid").evaluate(outDoc));
        assertEquals("2", xpath.compile("/ead/archdesc/dsc/c01/c02[2]/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[2]/c03/did/unitid").evaluate(outDoc));
        assertEquals("2 root 1 2", xpath.compile("/ead/archdesc/dsc/c01/c02[2]/c03[2]/did/unitid").evaluate(outDoc));
    }

    @Test
    public void testRelativizeIdentifiersWithHyphens() throws Exception {
        Document outDoc = getOutputDocument("/absoluteids-hyphens.xml");
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/c03/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[1]/c03/did/unitid").evaluate(outDoc));
        assertEquals("2", xpath.compile("/ead/archdesc/dsc/c01/c02[2]/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01/c02[2]/c03/did/unitid").evaluate(outDoc));
    }

    @Test
    public void testRelativizeIdentifiersWithSlashes() throws Exception {
        Document outDoc = getOutputDocument("/wp2_jmp_ead.xml");
        assertEquals("COLLECTION.JMP.SHOAH/T", xpath.compile("/ead/archdesc/did/unitid").evaluate(outDoc));
        assertEquals("2", xpath.compile("/ead/archdesc/dsc/c01[1]/did/unitid").evaluate(outDoc));
        assertEquals("A", xpath.compile("/ead/archdesc/dsc/c01[1]/c02[1]/did/unitid").evaluate(outDoc));
        assertEquals("1", xpath.compile("/ead/archdesc/dsc/c01[1]/c02[1]/c03[1]/did/unitid").evaluate(outDoc));
        assertEquals("a", xpath.compile("/ead/archdesc/dsc/c01[1]/c02[1]/c03[1]/c04[1]/did/unitid").evaluate(outDoc));
        assertEquals("028", xpath.compile("/ead/archdesc/dsc/c01[1]/c02[1]/c03[1]/c04[1]/c05[1]/did/unitid").evaluate
                (outDoc));
    }

    private Document getOutputDocument(String resourceName) throws URISyntaxException, XMLStreamException,
            IOException, SAXException {
        URL resource = RelativizeIdentifiersTest.class.getResource(resourceName);
        String path = new File(resource.toURI()).getAbsolutePath();
        StringWriter stringWriter = new StringWriter();
        RelativizeIdentifiers.relativizeIdentifiers(path, stringWriter);
        stringWriter.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stringWriter.getBuffer().toString().getBytes());
        return builder.parse(byteArrayInputStream);
    }
}
