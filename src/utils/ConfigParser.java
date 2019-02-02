package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uitools.Card;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ConfigParser {
    public static void parseConfigFile (String fileName, String className) {
        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element rootNode = doc.getDocumentElement();
            int cardCount = Integer.parseInt(
                    ((Element) rootNode.getElementsByTagName("Number").item(0))
                            .getAttribute("count"));
            int rowCount = Integer.parseInt(
                    ((Element) rootNode.getElementsByTagName("Rows").item(0))
                            .getAttribute("count"));
            int colCount = Integer.parseInt(
                    ((Element) rootNode.getElementsByTagName("Columns").item(0))
                            .getAttribute("count"));
            cardGrid = new Card[rowCount][colCount];
            for(int i = 0; i < cardCount; i++) {
                processCard((Element) ((Element) rootNode.getElementsByTagName("Simulations").item(0)).
                        getElementsByTagName("Simulation").item(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
