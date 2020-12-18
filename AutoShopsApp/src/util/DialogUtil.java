package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialogUtil
{
    public void showErrorMessage(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Alert showConfirmMessage(String title, String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }

    public Alert showMyMessage(String title, String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        ButtonType copy = new ButtonType("Скопировать");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(copy);
        return alert;
    }
}
