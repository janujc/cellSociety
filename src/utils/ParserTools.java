package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Author: Anshu Dwibhashi
 * Class with tools to be used by parsers
 */
public class ParserTools {
    /**
     * Method to fetch the root node of an XML file.
     * @param file XML File object
     * @return Element pointing to the root node.
     */
    public static Element getRootNode(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            return doc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
