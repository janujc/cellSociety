package uitools;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.util.Duration;
import utils.ScreenType;
import visualization.Controller;

/**
 * Author: Anshu Dwibhashi
 * Purpose: Class with helper function for transitions between screens
 */
public class Animator {
    /**
     * Sliding animation between screens
     * @param myScene Scene containing both screens
     * @param container Container for target screen
     * @param finalScreen Type of the screen that will be in display after animation
     * @param startX1 Screen 1's starting X pos
     * @param startX2 Screen 2's starting X pos
     * @param finishX1 Screen 1's ending X pos
     * @param finishX2 Screen 2's ending X pos
     * @param remove Whether or not to remove original screen from tree
     * @param context Controller instance to access methods
     */
    public static void animate(Scene myScene, Group container, ScreenType finalScreen, double startX1, double startX2,
                               double finishX1, double finishX2, boolean remove, Controller context) {
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(container.translateXProperty(), startX1),
                new KeyValue(((Group) myScene.getRoot()).getChildren().get(0).translateXProperty(), startX2));
        KeyFrame end = new KeyFrame(Duration.seconds(1),
                new KeyValue(container.translateXProperty(), finishX1),
                new KeyValue(((Group) myScene.getRoot()).getChildren().get(0).translateXProperty(), finishX2));
        Timeline slide = new Timeline(start, end);

        slide.setOnFinished(e -> {
            context.setInTransition(false);
            context.setCurrentScreen(finalScreen);
            if (remove) {
                ((Group) myScene.getRoot()).getChildren().remove(1);
            }
        });
        context.setInTransition(true);
        slide.play();
    }
}
