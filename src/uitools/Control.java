package uitools;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import utils.ConfigParser;

public abstract class Control {
    ImageView icon;
    protected Control (Image image) {
        this.icon = new ImageView(image);
        this.icon.setOnMouseClicked(mouseEvent -> onClick());
    }

    protected abstract void onClick();

    public ImageView getView() {
        return icon;
    }
}
