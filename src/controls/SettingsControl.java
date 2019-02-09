package controls;

import javafx.scene.Group;
import javafx.scene.image.Image;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;

public class SettingsControl extends Control {
    private SimulationScreen context;

    public SettingsControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/settings.png")));
        this.context = context;
    }
    @Override
    protected void onClick() {
        // TODO: Show settings dialogue box
    }
}
