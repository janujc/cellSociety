package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

public class PrevStateControl extends Control {
    private SimulationScreen context;

    public PrevStateControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/stepleft.png")));
        this.context = context;
    }

    @Override
    public void onClick() {
        context.stepBack();
    }
}
