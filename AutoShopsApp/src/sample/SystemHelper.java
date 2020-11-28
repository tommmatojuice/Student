package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemHelper
{
    private String user;

    public Connection getConnection() throws SQLException {
        MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");
        return mysqlDatabase.getConnection();
    }

    public FXMLLoader openWindow(String path, double width) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        if (path != "sample.fxml"){
            if (width<1000){
                stage.setWidth(870);
                stage.setHeight(650);
            } else {
                stage.setWidth(1700);
                stage.setHeight(920);
            }
        }
//        stage.getIcons().add(new Image("sample/asseats/logo.png"));
        stage.setTitle("Conner Brothers auto shops");
        stage.show();
        return loader;
    }

    public void initMenu(String userName, JFXButton out_button, JFXButton shops_button, JFXButton masters_button, JFXButton model_button,
                         JFXButton cars_button, JFXButton client_button, JFXButton consum_button, JFXButton work_button,
                         JFXButton cintract_button, JFXButton service_button, JFXButton math_button, JFXButton users_button){
        System.out.println(userName);
        out_button.setOnAction(event -> {
            try {
                out_button.getScene().getWindow().hide();
                this.openWindow("sample.fxml",  out_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        shops_button.setOnAction(event -> {
            try {
                shops_button.getScene().getWindow().hide();
                FXMLLoader loader = this.openWindow("autoshops.fxml",  masters_button.getScene().getWidth());
                AutoshopsController controllerEditBook = loader.getController();
                controllerEditBook.setUserLabel(userName);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        masters_button.setOnAction(event -> {
            try {
                masters_button.getScene().getWindow().hide();
                FXMLLoader loader = this.openWindow("masters.fxml",  masters_button.getScene().getWidth());
                MastersController controllerEditBook = loader.getController();
                controllerEditBook.setUserLabel(userName);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        service_button.setOnAction(event -> {
            try {
                service_button.getScene().getWindow().hide();
                FXMLLoader loader = this.openWindow("services.fxml", service_button.getScene().getWidth());
                ServicesController controller = loader.getController();
                controller.setUserName(userName);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        client_button.setOnAction(event -> {
            client_button.getScene().getWindow().hide();
            FXMLLoader loader = null;
            try {
                loader = this.openWindow("customers.fxml", client_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            CustomerController controller = loader.getController();
            controller.setUserName(userName);
        });
    }

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean phoneCheck(String phone){
//        Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
//        Pattern pattern = Pattern.compile("/^+([0-9]{11})$/");
        Pattern pattern = Pattern.compile("^\\+[0-9]{11}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    public boolean priceCheck(String price){
        Pattern pattern = Pattern.compile("^([1-9].*.[0-9]*)$");
        Matcher matcher = pattern.matcher(price);
        return matcher.find();
    }

    public boolean passportCheck(String passport){
        Pattern pattern = Pattern.compile("^([0-9]{10})$");
        Matcher matcher = pattern.matcher(passport);
        return matcher.find();
    }
}
