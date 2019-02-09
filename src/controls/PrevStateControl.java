package controls;

import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to move to previous state of grid.
 */
public class PrevStateControl extends Control {
    private SimulationScreen context;

    public PrevStateControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/stepleft.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    public void onClick() {
        context.stepBack();
    }
}