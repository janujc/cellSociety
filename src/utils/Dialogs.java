package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Author: Anshu Dwibhashi
 * Dialog utilities
 */
public class Dialogs {
    /**
     * Method to show an alert dialog that can be OK'd
     * @param message
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
