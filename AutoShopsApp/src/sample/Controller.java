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

    private SystemHelper systemHelper = new SystemHelper();
    private MastersManager mastersManager = new MastersManager();

    @FXML
    void initialize() {
        main_enter_button.setOnAction(event -> {
            String login = main_login.getText().trim();
            String password = main_pass.getText().trim();
            System.out.println(login + " " + password);
            if(main_login.getText().trim().length()>0 && main_pass.getText().trim().length()>0){
                try {
                    checkAuth(login, password);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                systemHelper.showErrorMessage("Ошибка", "Введите логин и пароль!");
            }
        });
    }

    public void checkAuth(String login, String pass) throws IOException {
        try(Connection c = systemHelper.getConnection()) {
            String sql = "SELECT name, master FROM users where login=? AND password=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, pass);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                main_enter_button.getScene().getWindow().hide();
                if(resultSet.getString("name") == null){
                    System.out.println("master");
                    FXMLLoader loader = systemHelper.showScene("contracts.fxml");
                    ContractsController controller = loader.getController();
                    System.out.println(resultSet.getInt("master"));
                    controller.initialize(mastersManager.getById(resultSet.getInt("master")).getName(), resultSet.getInt("master"));
                } else {
                    System.out.println("admin");
                    FXMLLoader loader = systemHelper.showScene("/sample/autoshops.fxml");
                    AutoshopsController controllerEditBook = loader.getController();
                    controllerEditBook.setUserLabel(resultSet.getString("name"));
                }
            } else {
                systemHelper.showErrorMessage("Ошибка", "Неверный логин или пароль!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}