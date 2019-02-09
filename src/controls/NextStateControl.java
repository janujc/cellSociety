package controls;

import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to advance to next state of grid
 */
public class NextStateControl extends Control {
    private SimulationScreen context;

    public NextStateControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/stepright.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    public void onClick() {
        context.stepForward();
    }
}
