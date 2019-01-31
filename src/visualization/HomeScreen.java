package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uitools.CardGridGenerator;

import static uitools.TextGenerator.makeText;
import static visualization.Controller.bebasKai;
import static visualization.Controller.sofiaPro;

public class HomeScreen {
    Scene myScene;
    public HomeScreen(Group root, double width, double height, Color background) {
        myScene = new Scene(root, width, height, background);
        var container = new Group();
        Text titleText = makeText("SimuCell", sofiaPro, Color.SLATEGREY,
                myScene.getWidth()/2,
                myScene.getHeight()/10);
        Text subTitleText = makeText("Pick a simulation below", bebasKai, Color.SLATEGREY,
                myScene.getWidth()/2,
                myScene.getHeight()/10 + 25); // 25 px below previous text

        container.getChildren().addAll(titleText, subTitleText);
        root.getChildren().add(container);
        CardGridGenerator.makeGrid();
    }
    public Scene getScene() {
        return myScene;
    }
}
