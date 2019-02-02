package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

public class NextStateControl extends Control {
    private SimulationScreen context;

    public NextStateControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/stepright.png")));
        this.context = context;
    }

    @Override
    public void onClick() {
        context.stepForward();
    }
}
