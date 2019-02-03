package uitools;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

/**
 * Author: Anshu Dwibhashi
 * Abstract superclass for all types of controls
 */
public abstract class Control {
    private Button icon;
    protected Control (Image image) {
        this.icon = new Button();
        this.icon.setGraphic(new ImageView(image));
        this.icon.setBackground(Background.EMPTY);
        this.icon.setOnMouseClicked(mouseEvent -> onClick());
    }

    protected abstract void onClick();

    /**
     * Returns view associated with this control
     * @return Button associated with this control
     */
    public Button getView() {
        return icon;
    }
}
