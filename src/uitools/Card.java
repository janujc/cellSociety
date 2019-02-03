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

/**
 * Author: Anshu Dwibhashi
 * Class to contain a card and relevant views.
 */
public class Card {
    private Group cardView;
    private String className; // Name of class to handle this particular simulation
    private String label, configFolder;


    public Card(Image background, String label, String className, String configFolder) {
        this.className = className;
        this.label = label;
        this.configFolder = configFolder;
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
    }
    public Group getCardView() {
        return cardView;
    }
    public String getClassName() {
        return className;
    }
    public String getLabel() {
        return label;
    }
    public  String getConfigFolder() {
        return configFolder;
    }
}
