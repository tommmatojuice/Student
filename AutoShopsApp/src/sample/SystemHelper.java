package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SystemHelper
{
    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/auto_shops", "postgres", "1234");
//        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/auto_shops", "postgres", "1234");
        MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");
        return mysqlDatabase.getConnection();
    }

    public void openWindow(String path, double width, double height) throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setWidth(width+20);
        stage.setHeight(height+20);
        stage.show();
    }

    public void showErrorMessage(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
}
