package uitools;

import javafx.scene.image.Image;

public class SpeedUpControl extends Control {
    public SpeedUpControl() {
        super(new Image(CardGridGenerator.class.getResourceAsStream("/img/speedup.png")));
    }

    @Override
    protected void onClick() {
        System.out.println("Clicked speedup");
    }
}
