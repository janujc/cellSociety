package controls;

import javafx.scene.Group;
import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

public class GraphControl extends Control {
    private SimulationScreen context;

    public GraphControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/graph.png")));
        this.context = context;
    }
    @Override
    protected void onClick() {
        // TODO: Show graph
    }
}
