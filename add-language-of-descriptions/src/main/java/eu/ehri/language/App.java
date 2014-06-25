package eu.ehri.language;

import java.io.IOException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author linda
 */
public class App {

    public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
        String eadfile = args[0];
        String language = args[1];
        String langcode = args[2];

        boolean isLanguageDetected = DetectLanguage.languageDetected(eadfile, language, langcode);
        if (isLanguageDetected) {
            System.out.printf("Language detected (%s)\n", eadfile);
        } else {
            AddLanguage.addLanguage(eadfile, language, langcode);
        }
    }
}
