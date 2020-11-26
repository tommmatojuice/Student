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
                stage.setWidth(800);
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

    public boolean phoneCheck(String phone){
        Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        Matcher matcher = pattern.matcher(phone);
//        while (matcher.find()) {
//            int start=matcher.start();
//            int end=matcher.end();
//            System.out.println("Найдено совпадение " + phone.substring(start,end) + " с "+ start + " по " + (end-1) + " позицию");
//        }
//        System.out.println(text);
        return matcher.find();
    }
}
