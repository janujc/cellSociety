package uitools;

import javafx.scene.image.Image;
import visualization.SimulationScreen;

/**
 * Author: Anshu Dwibhashi
 * Control to open menu
 */
public class MenuControl extends Control {
    private SimulationScreen context;

    public MenuControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/hamburger.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    public void onClick() {
        context.showMenu();
    }
}
