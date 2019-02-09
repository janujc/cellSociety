package controls;

import javafx.scene.Group;
import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

public class SaveControl extends Control {
    private SimulationScreen context;

    public SaveControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/save.png")));
        this.context = context;
    }
    @Override
    protected void onClick() {
        // TODO: Save current state
    }
}
