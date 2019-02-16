package uitools;

import javafx.scene.image.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.ParserTools;

import java.io.File;

/**
 * Author: Anshu Dwibhashi
 * Class to parse XML config file and generate a grid of cards for each
 * type of simulation.
 */
public class CardGridGenerator {
    private static Card[][] cardGrid;
    private static void parseXML() {
        File inputFile = new File("data/sim_list.xml");
        Element rootNode = ParserTools.getRootNode(inputFile);
        int cardCount = getCount(rootNode.getElementsByTagName("Number"));
        int rowCount = getCount(rootNode.getElementsByTagName("Rows"));
        int colCount = getCount(rootNode.getElementsByTagName("Columns"));
        cardGrid = new Card[rowCount][colCount];
        for (int i = 0; i < cardCount; i++) {
            processCard((Element) ((Element) rootNode.getElementsByTagName("Simulations").item(0)).
                    getElementsByTagName("Simulation").item(i));
        }
    }

    private static int getCount(NodeList nodes) {
        return Integer.parseInt(((Element) nodes.item(0)).getAttribute("count"));
    }

    private static void processCard(Element simulation) {
        int x = Integer.parseInt(simulation.getAttribute("gridX"));
        int y = Integer.parseInt(simulation.getAttribute("gridY"));
        String imagePath = simulation.getAttribute("background");
        String label = simulation.getAttribute("label");
        String configFolder = simulation.getAttribute("configFolder");
        String className = simulation.getAttribute("class");
        Card card = new Card(new Image(CardGridGenerator.class.getResourceAsStream(imagePath)), label, className, configFolder);
        cardGrid[y][x] = card;
    }

    /**
     * Method to read config file and return grid of cards
     *
     * @return 2D array of cards
     */
    public static Card[][] makeGrid() {
        parseXML();
        return cardGrid;
    }
}
