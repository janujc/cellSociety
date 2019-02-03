package utils;

import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import simulation.Simulation;
import uitools.Card;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Author: Anshu Dwibhashi
 * Class to read config file associated with a particular initial configuration
 * of a simulation and generate a Simulation instance.
 */
public class ConfigParser {
    /**
     * Generates an instance of Simulation from given config file
     * @param fileName File name of config file.
     * @param className Class name of subclass of Simulation to instantiate
     * @return Simulation instance that was generated
     */
    public static Simulation parseConfigFile (String fileName, String className) {
        try {
            File inputFile = new File(fileName);
            Element rootNode = ParserTools.getRootNode(inputFile);
            int sideSize = Integer.parseInt(
                    ((Element) rootNode.getElementsByTagName("SideSize").item(0))
                            .getAttribute("count"));
            String metadata;
            if (rootNode.getElementsByTagName("Metadata").getLength() > 0) {
                metadata = ((Element) rootNode.getElementsByTagName("Metadata").item(0))
                        .getAttribute("value");
            } else {
                metadata = null;
            }

            Element data = ((Element) rootNode.getElementsByTagName("Data").item(0));
            if (data.getAttribute("type").equals("frequencies")) {
                ArrayList<Double> popFreqs = new ArrayList<>();
                ArrayList<Integer> states = new ArrayList<>();
                ArrayList<Color> colors = new ArrayList<>();
                for(int i = 0; i < data.getElementsByTagName("Item").getLength(); i++) {
                    Element item = (Element) data.getElementsByTagName("Item").item(i);
                    popFreqs.add(Double.valueOf(item.getAttribute("popFreq")));
                    states.add(Integer.valueOf(item.getAttribute("state")));
                    colors.add(Color.web(item.getAttribute("color")));
                }
                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getConstructor(int.class, Integer[].class, Double[].class, Color[].class, String.class);
                Simulation instance = (Simulation) constructor.newInstance(sideSize, states.toArray(new Integer[0]), popFreqs.toArray(new Double[0]), colors.toArray(new Color[0]), metadata);
                return instance;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
