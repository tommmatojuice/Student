package sample;

import com.jfoenix.controls.JFXButton;
import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import util.MysqlDatabase;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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

        cars_button.setOnAction(event -> {
            cars_button.getScene().getWindow().hide();
            FXMLLoader loader = null;
            try {
                loader = this.openWindow("cars.fxml", cars_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            CarsController controller = loader.getController();
            controller.setUserName(userName);
        });

        model_button.setOnAction(event -> {
            model_button.getScene().getWindow().hide();
            FXMLLoader loader = null;
            try {
                loader = this.openWindow("car_models.fxml", model_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            CarModelsController controller = loader.getController();
            controller.setUserName(userName);
        });

        consum_button.setOnAction(event -> {
            consum_button.getScene().getWindow().hide();
            FXMLLoader loader = null;
            try {
                loader = this.openWindow("consumables.fxml", consum_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            ConsumablesController controller = loader.getController();
            controller.setUserName(userName);
        });

        cintract_button.setOnAction(event -> {
            cintract_button.getScene().getWindow().hide();
            FXMLLoader loader = null;
            try {
                loader = this.openWindow("contracts.fxml", cintract_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            ContractsController controller = loader.getController();
            controller.setUserName(userName);
        });
    }

    public void doubleClickOnModels(String userName, AutoShops autoShop, JFXButton button) throws SQLException {
        button.getScene().getWindow().hide();
        FXMLLoader loader = null;
        try {
            loader = this.openWindow("shop_models.fxml", button.getScene().getWidth());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ShopModelsController controller = loader.getController();
        controller.initialize(userName, autoShop);
    }

    public void doubleClickOnContract(String userName, Contracts contract, JFXButton button) throws SQLException {
        button.getScene().getWindow().hide();
        FXMLLoader loader = null;
        try {
            loader = this.openWindow("works.fxml", button.getScene().getWidth());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        WorksController controller = loader.getController();
        controller.initialize(userName, contract);
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

    public void setUser(String user) {
        this.user = user;
    }

    public boolean phoneCheck(String phone){
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

    public boolean stateNumberCheck(String stateNumber){
        Pattern pattern = Pattern.compile("[А-Я]\\d{3}[А-Я]{2}\\d{2,3}");
        Matcher matcher = pattern.matcher(stateNumber);
        return matcher.find();
    }

    public StringConverter<Date> getStringConverter(){
        return new StringConverter<Date>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(Date date) {
                if (date != null) {
                    return dateFormatter.format(date.toLocalDate());
                } else {
                    return "";
                }
            }

            @Override
            public Date fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return Date.valueOf(string);
                } else {
                    return null;
                }
            }
        };
    }
}
