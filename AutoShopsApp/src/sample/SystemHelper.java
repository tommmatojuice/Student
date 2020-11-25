package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SystemHelper
{
    private String user;

    public Connection getConnection() throws SQLException {
        MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");
        return mysqlDatabase.getConnection();
    }

    public FXMLLoader openWindow(String path, double width, double height) throws IOException
    {
//        Parent parent = FXMLLoader.load(getClass().getResource(path));
//        Stage stage = new Stage();
//        stage.setScene(new Scene(parent));
//        stage.setWidth(width+20);
//        stage.setHeight(height+20);
//        stage.setTitle("Conner Brothers auto shops");
////        stage.getIcons().add(new Image("/asseats/logo.png"));
//        stage.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/autoshops.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setWidth(width+20);
        stage.setHeight(height+20);
        stage.setTitle("Conner Brothers auto shops");
        stage.show();
        return loader;

//        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
//        Parent root = loader.load();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        ControllerClass controllerEditBook = loader.getController(); //получаем контроллер для второй формы
//        controllerEditBook.someMethod(someParameters); // передаем необходимые параметры
//        Main.primaryStage.show();
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
