package visualization;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uitools.Card;
import uitools.CardGridGenerator;

import java.util.ArrayList;

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

        Card[][] cardGrid = CardGridGenerator.makeGrid();
        Group grid = new Group();
        for (int i = 0; i < cardGrid.length; i++) {
            for (int j = 0; j < cardGrid[0].length; j++) {
                if(cardGrid[i][j] != null) {
                    Group card = cardGrid[i][j].getCardView();
                    card.setLayoutY(150*i + 50*i);
                    card.setLayoutX(150*j + 25*j);
                    card.setCursor(Cursor.HAND);
                    grid.getChildren().add(card);
                }
            }
        }

        grid.setLayoutY(myScene.getHeight()/2 - grid.getLayoutBounds().getHeight()/2 + 25); // extra margin
        grid.setLayoutX(myScene.getWidth()/2 - grid.getLayoutBounds().getWidth()/2 + 9); // extra margin

        container.getChildren().addAll(titleText, subTitleText);
        container.getChildren().addAll(grid);
        root.getChildren().add(container);
    }
    public Scene getScene() {
        return myScene;
    }
}
