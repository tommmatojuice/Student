package sample;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller{
    @FXML
    private PasswordField main_pass;

    @FXML
    private TextField main_login;

    @FXML
    private JFXButton main_enter_button;

    private SystemHelper helper = new SystemHelper();

    @FXML
    void initialize() {
        main_enter_button.setOnAction(event -> {
            String login = main_login.getText().trim();
            String password = main_pass.getText().trim();
            System.out.println(login + " " + password);
            if(main_login.getText().trim().length()>0 && main_pass.getText().trim().length()>0){
                checkAuth(login, password);
            } else {
                helper.showErrorMessage("Ошибка", "Введите логин и пароль!");
            }
        });
    }

    public void checkAuth(String login, String pass){
        try(Connection c = helper.getConnection()) {
            String sql = "SELECT name FROM users where login=? AND password=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, pass);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                helper.setUser(resultSet.getString("name"));
                main_enter_button.getScene().getWindow().hide();
                try {
                    FXMLLoader loader = helper.openWindow("/sample/autoshops.fxml", main_enter_button.getScene().getWidth());
                    AutoshopsController controllerEditBook = loader.getController();
                    controllerEditBook.setUserLabel(resultSet.getString("name"));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                helper.showErrorMessage("Ошибка", "Неверный логин или пароль!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}