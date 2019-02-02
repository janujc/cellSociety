package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simulation.PredatorPrey;
import simulation.Simulation;
import uitools.SpeedUpControl;

import static uitools.TextGenerator.makeText;
import static visualization.Controller.bebasKai;
import static visualization.Controller.sofiaPro;

public class SimulationScreen {
    private Group myContainer;
    private Controller context;
    private Simulation simulation;
    public SimulationScreen(Scene scene, Controller context, Simulation simulation, String label) {
        this.simulation = simulation;

        var container = new Group();
        Text titleText = makeText(label, sofiaPro, Color.SLATEGREY,
                scene.getWidth()/2,
                scene.getHeight()/10);

        myContainer = container;

        Text pressEscape = makeText("Press Escape To Exit", bebasKai, Color.SLATEGREY, scene.getWidth()/2, scene.getHeight()-15);
        SpeedUpControl speedUpControl = new SpeedUpControl();
        speedUpControl.getView().setX(25);
        speedUpControl.getView().setY(scene.getHeight()-70);

        container.getChildren().addAll(titleText, pressEscape, speedUpControl.getView());
    }
    public Group getContainer() {
        return myContainer;
    }
}
