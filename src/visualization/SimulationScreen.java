package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simulation.Simulation;

import static uitools.TextGenerator.makeText;
import static visualization.Controller.sofiaPro;

public class SimulationScreen {
    private Group myContainer;
    private Controller context;
    private Simulation simulation;
    public SimulationScreen(Scene scene, Controller context, Simulation simulation) {
        this.simulation = simulation;
        var container = new Group();
        Text titleText = makeText("test", sofiaPro, Color.SLATEGREY,
                scene.getWidth()/2,
                scene.getHeight()/10);
        container.getChildren().addAll(titleText);
        myContainer = container;
    }
    public Group getContainer() {
        return myContainer;
    }
}
