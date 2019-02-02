package utils;

import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import simulation.Simulation;
import uitools.Card;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ConfigParser {
    public static Simulation parseConfigFile (String fileName, String className) {
        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element rootNode = doc.getDocumentElement();
            int sideSize = Integer.parseInt(
                    ((Element) rootNode.getElementsByTagName("SideSize").item(0))
                            .getAttribute("count"));

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
                Constructor<?> constructor = clazz.getConstructor(int.class, int[].class, double[].class, Color[].class);
                Simulation instance = (Simulation) constructor.newInstance(sideSize, states.toArray(), popFreqs.toArray(), colors.toArray());
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
