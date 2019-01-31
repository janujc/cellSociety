package uitools;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import visualization.Controller;

public class Card {
    private Group cardView;
    private String className; // Name of class to handle this particular simulation


    public Card(Image background, String label, String className) {
        this.className = className;
        cardView = new Group();
        ImageView mainCard = new ImageView(background);

        // Apply a shadow effect
        mainCard.setEffect(new DropShadow(10, Color.DARKGREY));

        Text labelView = new Text();
        labelView.setText(label);
        labelView.setFont(Controller.sofiaProSmall);
        labelView.setX(mainCard.getLayoutBounds().getWidth()/2 - labelView.getLayoutBounds().getWidth()/2);
        labelView.setY(mainCard.getLayoutBounds().getHeight() + 30 - labelView.getLayoutBounds().getHeight()/2);

        cardView.getChildren().addAll(mainCard, labelView);
        cardView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(className+" needs to be called");
            }

        });
    }
    public Group getCardView() {
        return cardView;
    }
}
