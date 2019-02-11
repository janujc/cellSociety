package controls;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uitools.CardGridGenerator;
import visualization.SimulationScreen;
import visualization.SimulationShell;

/**
 * Author: Anshu Dwibhashi
 * Control to toggle between play and pause states
 */
public class PlayPauseToggleControl extends Control {
    private SimulationScreen context;

    public PlayPauseToggleControl(SimulationScreen context) {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/play.png")));
        this.context = context;
    }

    /**
     * Method executed when this control is clicked
     */
    @Override
    public void onClick() {
        context.setIsPaused(!context.getIsPaused());

        if (context.getIsPaused()) {
            this.getView().setGraphic(new ImageView(new Image(CardGridGenerator.class.getResourceAsStream("/img/play.png"))));
        } else {
            this.getView().setGraphic(new ImageView(new Image(CardGridGenerator.class.getResourceAsStream("/img/pause.png"))));
        }
    }
}
