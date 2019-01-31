package uitools;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import utils.Cell;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class CardGridGenerator {
    private static Card[][] cardGrid;
    private static void parseXML () {
        try {
            File inputFile = new File("data/sim_list.xml");
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

    private static void processCard(Element simulation) {
        int x = Integer.parseInt(simulation.getAttribute("gridX"));
        int y = Integer.parseInt(simulation.getAttribute("gridY"));
        String imagePath = simulation.getAttribute("background");
        String label = simulation.getAttribute("label");
        String className = simulation.getAttribute("class");
        Card card = new Card(new Image(CardGridGenerator.class.getResourceAsStream(imagePath)), label, className);
        cardGrid[y][x] = card;
    }

    public static Card[][] makeGrid() {
        parseXML();
        return cardGrid;
    }
}