package visualization;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Group myContainer;
    private Controller context;
    public HomeScreen(Scene myScene, Controller context) {
        this.context = context;

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
                    Group cardView = cardGrid[i][j].getCardView();
                    cardView.setLayoutY(150*i + 50*i);
                    cardView.setLayoutX(150*j + 25*j);
                    cardView.setCursor(Cursor.HAND);
                    final var card = cardGrid[i][j];
                    cardView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println(card.getClassName()+" needs to be called");
                        }
                    });
                    grid.getChildren().add(cardView);
                }
            }
        }

        grid.setLayoutY(myScene.getHeight()/2 - grid.getLayoutBounds().getHeight()/2 + 25); // extra margin
        grid.setLayoutX(myScene.getWidth()/2 - grid.getLayoutBounds().getWidth()/2 + 9); // extra margin

        container.getChildren().addAll(titleText, subTitleText);
        container.getChildren().addAll(grid);
        myContainer = container;
    }

    public Group getContainer() {
        return myContainer;
    }
}
