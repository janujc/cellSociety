package uitools;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextGenerator {
    public static Text makeText(String content, Font font, Color textColor, double xPos, double yPos) {
        Text tf = new Text();
        tf.setText(content);
        tf.setFont(font);
        tf.setFill(textColor);
        tf.setX(xPos - tf.getLayoutBounds().getWidth()/2);
        tf.setY(yPos  - tf.getLayoutBounds().getHeight()/2);
        return tf;
    }
}
