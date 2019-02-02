package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

public class SpeedUpControl extends Control {
    SimulationScreen context;
    public SpeedUpControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/speedup.png")));
        this.context = context;
    }

    @Override
    protected void onClick() {
        context.setRate(context.getRate()+1);
    }
}
