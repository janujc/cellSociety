package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import simulation.Simulation;

public class SimulationScreen {
    private Group myContainer;
    private Controller context;
    private Simulation simulation;
    public SimulationScreen(Scene scene, Controller context, Simulation simulation) {
        this.simulation = simulation;
    }
    public Group getContainer() {
        return myContainer;
    }
}
