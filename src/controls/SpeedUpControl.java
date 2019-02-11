package controls;

import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;
import visualization.SimulationShell;

/**
 * Author: Anshu Dwibhashi
 * Control to speed up simulation
 */
public class SpeedUpControl extends Control {
    SimulationScreen context;
    public SpeedUpControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/speedup.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    protected void onClick() {
        int rate = context.getRate()+1;
        context.setRate(rate);
    }
}
