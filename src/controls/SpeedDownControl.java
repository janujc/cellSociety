package controls;

import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to speed down simulation.
 */
public class SpeedDownControl extends Control {
    SimulationScreen context;
    public SpeedDownControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/speeddown.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    protected void onClick() {
        context.setRate(context.getRate()-1);
    }
}
