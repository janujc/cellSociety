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
    public static void animate(Scene myScene, Group root, ScreenType finalScreen, double startY1, double startY2,
                               double finishY1, double finishY2, boolean remove, Controller context) {
        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(root.getChildren().get(0).translateYProperty(), startY1),
                new KeyValue(((Group) myScene.getRoot()).getChildren().get(0).translateYProperty(), startY2));
        KeyFrame end = new KeyFrame(Duration.seconds(1),
                new KeyValue(root.getChildren().get(0).translateYProperty(), finishY1),
                new KeyValue(((Group) myScene.getRoot()).getChildren().get(0).translateYProperty(), finishY2));
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
