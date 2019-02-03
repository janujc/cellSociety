package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to speed up simulation.
 */
public class SpeedDownControl extends Control {
    SimulationScreen context;
    public SpeedDownControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/speeddown.png")));
        this.context = context;
    }

    @Override
    protected void onClick() {
        context.setRate(context.getRate()-1);
    }
}
