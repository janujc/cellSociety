package utils;

import javafx.scene.paint.Color;
import org.w3c.dom.Element;
import simulation.Simulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Author: Anshu Dwibhashi
 * Class to read config file associated with a particular initial configuration
 * of a simulation and generate a Simulation instance.
 */
public class ConfigParser {
    /**
     * Generates an instance of Simulation from given config file
     *
     * @param fileName  File name of config file.
     * @return Simulation instance that was generated
     * @throws Exception if a config file is chosen that is corrupt or doesn't define this type
     *                   of a simulation
     */
    public static Simulation parseConfigFile(String fileName) throws Exception {
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

            String className = ((Element) rootNode.getElementsByTagName("Class").item(0))
                    .getAttribute("class");
            String displayName = ((Element) rootNode.getElementsByTagName("Name").item(0))
                    .getAttribute("name");

            Element data = ((Element) rootNode.getElementsByTagName("Data").item(0));
            if (data.getAttribute("type").equals("frequencies")) {
                ArrayList<Double> popFreqs = new ArrayList<>();
                ArrayList<Integer> states = new ArrayList<>();
                ArrayList<Color> colors = new ArrayList<>();
                for (int i = 0; i < data.getElementsByTagName("Item").getLength(); i++) {
                    Element item = (Element) data.getElementsByTagName("Item").item(i);
                    popFreqs.add(Double.valueOf(item.getAttribute("popFreq")));
                    states.add(Integer.valueOf(item.getAttribute("state")));
                    colors.add(Color.web(item.getAttribute("color")));
                }
                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getConstructor(int.class, Integer[].class, Double[].class, Color[].class, String.class);
                Simulation returnable = (Simulation) constructor.newInstance(sideSize, states.toArray(new Integer[0]), popFreqs.toArray(new Double[0]), colors.toArray(new Color[0]), metadata);
                returnable.setDisplayName(displayName);
                returnable.setCurrentFileName(fileName);
                return returnable;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw (e);
        }
    }

    public static void addGridToFile(File chosenFile, Simulation simulation) {
        try {
            File inputFile = new File(simulation.getCurrentFileName());
            Scanner scanner = new Scanner(inputFile);
            String text = scanner.useDelimiter("\\A").next();
            scanner.close();

            String datawrapper = "<Data type=\"specific\">\n";
            for (Cell[] row : simulation.getGrid()) {
                String currentRow = "\t\t<Row>";
                for (Cell cell : row) {
                    currentRow = currentRow.concat(cell.getCurrState() + ",");
                }
                currentRow = currentRow.substring(0, currentRow.length() - 1);
                currentRow = currentRow.concat("</Row>\n");
                datawrapper = datawrapper.concat(currentRow);
            }
            datawrapper = datawrapper.concat("\t</Data>\n");

            String substringToReplace = text.substring(
                    text.indexOf("<Data"), text.indexOf("</Data>") + "</Data>".length()
            );

            String contents = text.replace(substringToReplace, datawrapper);

            try (PrintStream out = new PrintStream(new FileOutputStream(chosenFile.getAbsolutePath()))) {
                out.print(contents);
            }
        } catch (Exception e) {
            Dialogs.showAlert("File not found");
        }
    }
}
