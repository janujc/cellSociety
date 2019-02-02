package uitools;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import utils.ConfigParser;

public abstract class Control {
    Button icon;
    protected Control (Image image) {
        this.icon = new Button();
        this.icon.setGraphic(new ImageView(image));
        this.icon.setBackground(Background.EMPTY);
        this.icon.setOnMouseClicked(mouseEvent -> onClick());
    }

    protected abstract void onClick();

    public Button getView() {
        return icon;
    }
}
