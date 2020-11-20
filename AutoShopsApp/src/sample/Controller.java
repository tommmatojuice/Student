package sample;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.MysqlDatabase;

public class Controller{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField main_pass;

    @FXML
    private TextField main_login;

    @FXML
    private JFXButton main_enter_button;

    @FXML
    private ImageView main_image;

    private SystemHelper helper = new SystemHelper();

    @FXML
    void initialize() {
        main_enter_button.setOnAction(event -> {
            String login = main_login.getText().trim();
            String password = main_pass.getText().trim();
            System.out.println(login + " " + password);
            if(login.length()>0 && password.length()>0){
                checkAuth(login, password);
            } else {
                helper.showMessage("Ошибка", "Введите логин и пароль!");
            }
        });
    }

    public void checkAuth(String login, String pass){
        try(Connection c = helper.getConnection()) {
            String sql = "SELECT * FROM users";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            int authFlag = 0;
            while (resultSet.next()){
                String dblogin = resultSet.getString("login");
                String dbpass = resultSet.getString("password");
                System.out.println(dblogin + " " + dbpass);
                if(dblogin.equals(login) && dbpass.equals(pass)){
//                    System.out.println();
                    main_enter_button.getScene().getWindow().hide();
                    try {
                        helper.openWindow("/sample/autoshops.fxml");
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    authFlag = 1;
                    break;
                }
            }
            if (authFlag==0) helper.showMessage("Ошибка", "Неверный логин или пароль!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}