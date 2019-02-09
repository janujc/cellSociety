package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to open menu
 */
public class CloseControl extends Control {
    private SimulationScreen context;

    public CloseControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/close.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    public void onClick() {
        context.closeMenu();
    }
}
