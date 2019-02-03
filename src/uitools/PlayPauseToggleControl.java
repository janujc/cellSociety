package uitools;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import visualization.SimulationScreen;

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
