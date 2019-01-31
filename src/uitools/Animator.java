package uitools;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.util.Duration;
import utils.ScreenType;
import visualization.Controller;

public class Animator {
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
