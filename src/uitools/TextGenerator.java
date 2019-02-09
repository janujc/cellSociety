package uitools;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Author: Anshu Dwibhashi
 * Class containing helper method(s) related to generation of text views
 */
public class TextGenerator {
    /**
     * Method to generate a generic text view
     * @param content Text to display inside
     * @param font Font to display the text in
     * @param textColor Colour to display that text in
     * @param xPos X position of this text view
     * @param yPos Y position of this text view
     * @return Text view generated
     */
    public static Text makeText(String content, Font font, Color textColor, double xPos, double yPos) {
        Text tf = processText(content, font, textColor, xPos, yPos);
        tf.setX(xPos - tf.getLayoutBounds().getWidth()/2);
        tf.setY(yPos  - tf.getLayoutBounds().getHeight()/2);
        return tf;
    }

    private static Text processText(String content, Font font, Color textColor, double xPos, double yPos) {
        Text tf = new Text();
        tf.setText(content);
        tf.setFont(font);
        tf.setFill(textColor);

        return tf;
    }

    /**
     * Method to generate a generic text view relative to a parent
     * @param content Text to display inside
     * @param font Font to display the text in
     * @param textColor Colour to display that text in
     * @param xPos X position of this text view
     * @param yPos Y position of this text view
     * @return Text view generated
     */
    public static Text makeTextRelative(String content, Font font, Color textColor, double xPos, double yPos) {
        Text tf = processText(content, font, textColor, xPos, yPos);
        tf.setLayoutX(xPos - tf.getLayoutBounds().getWidth()/2);
        tf.setLayoutY(yPos  - tf.getLayoutBounds().getHeight()/2);
        return tf;
    }
}
