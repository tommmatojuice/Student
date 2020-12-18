package sample.ui;

import com.jfoenix.controls.JFXButton;
import data.entity.AutoShops;
import data.entity.Contracts;
import data.entity.Works;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import sample.Main;
import sample.ui.*;
import util.CheckUtil;
import util.DialogUtil;
import util.MysqlDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemHelper extends Main
{
    private DialogUtil dialogUtil = new DialogUtil();

    public Connection getConnection() throws SQLException {
        MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");
        return mysqlDatabase.getConnection();
    }

    public void initMenu(String userName, int role, JFXButton out_button, JFXButton shops_button, JFXButton masters_button, JFXButton model_button,
                         JFXButton cars_button, JFXButton client_button, JFXButton consum_button, JFXButton work_button,
                         JFXButton cintract_button, JFXButton service_button, JFXButton math_button, JFXButton users_button){
        out_button.setOnAction(event -> {
            try {
                showScene("sample.fxml");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        if (role == 0){
            shops_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("autoshops.fxml");
                    AutoshopsController controllerEditBook = loader.getController();
                    controllerEditBook.setUserLabel(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            masters_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("masters.fxml");
                    MastersController controllerEditBook = loader.getController();
                    controllerEditBook.setUserLabel(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            service_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("services.fxml");
                    ServicesController controllerEditBook = loader.getController();
                    controllerEditBook.setUserName(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            client_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("customers.fxml");
                    CustomerController controller = loader.getController();
                    controller.setUserName(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            cars_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("cars.fxml");
                    CarsController controller = loader.getController();
                    controller.setUserName(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            model_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("car_models.fxml");
                    CarModelsController controller = loader.getController();
                    controller.setUserName(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            consum_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("consumables.fxml");
                    ConsumablesController controller = loader.getController();
                    controller.setUserName(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            cintract_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("contracts.fxml");
                    ContractsController controller = loader.getController();
                    controller.initialize(userName, role);
                } catch (IOException | SQLException exception) {
                    exception.printStackTrace();
                }
            });

            math_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("analytics.fxml");
                    AnalyticsPageController controller = loader.getController();
                    controller.initialize(userName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            users_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("users.fxml");
                    UsersController controller = loader.getController();
                    controller.initialize(userName);
                } catch (IOException | SQLException exception) {
                    exception.printStackTrace();
                }
            });

            work_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("analyticsTable.fxml");
                    AnalyticsTableController controller = loader.getController();
                    controller.initialize(3, userName, role);
                } catch (IOException | SQLException exception) {
                    exception.printStackTrace();
                }
            });
        } else {
            shops_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("contracts.fxml");
                    ContractsController controller = loader.getController();
                    controller.initialize(userName, role);
                } catch (IOException | SQLException exception) {
                    exception.printStackTrace();
                }
            });

            masters_button.setOnAction(event -> {
                try {
                    FXMLLoader loader = showScene("analyticsTable.fxml");
                    AnalyticsTableController controller = loader.getController();
                    controller.initialize(3, userName, role);
                } catch (IOException | SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }

    }

    public void doubleClickOnModels(String userName, AutoShops autoShop, JFXButton button) throws SQLException {
        try {
            FXMLLoader loader = showScene("shop_models.fxml");
            ShopModelsController controller = loader.getController();
            controller.initialize(userName, autoShop);
        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void doubleClickOnContract(String userName, int role, Contracts contract) throws SQLException {
        try {
            FXMLLoader loader = showScene("works.fxml");
            WorksController controller = loader.getController();
            controller.initialize(userName, role, contract);
        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void doubleClickOnConsumables(String userName, int role, Contracts contract, Works works, JFXButton button) throws SQLException {
        try {
            FXMLLoader loader = showScene("consumables_use.fxml");
            ConsumablesUseController controller = loader.getController();
            controller.initialize(userName, role, contract, works);
        } catch (IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

//    public void showErrorMessage(String title, String message)
//    {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public Alert showConfirmMessage(String title, String header, String message)
//    {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.setContentText(message);
//        return alert;
//    }
//
//    public Alert showMyMessage(String title, String header, String message)
//    {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.setContentText(message);
//
//        ButtonType copy = new ButtonType("Скопировать");
//        alert.getButtonTypes().clear();
//        alert.getButtonTypes().add(copy);
//        return alert;
//    }

//    public boolean phoneCheck(String phone){
//        Pattern pattern = Pattern.compile("^\\+[0-9]{11}$");
//        Matcher matcher = pattern.matcher(phone);
//        return matcher.find();
//    }
//
//    public boolean priceCheck(String price){
//        Pattern pattern = Pattern.compile("^(?!0.*$)(?!,$)([0-9]{1,9}(\\.[0-9]{2})?)$");
//        Matcher matcher = pattern.matcher(price);
//        return matcher.find();
//    }
//
//    public boolean countCheck(String price){
//        Pattern pattern = Pattern.compile("^[0-9]*[.]?[0-9]+$");
//        Matcher matcher = pattern.matcher(price);
//        return matcher.find();
//    }
//
//    public boolean passportCheck(String passport){
//        Pattern pattern = Pattern.compile("^([0-9]{10})$");
//        Matcher matcher = pattern.matcher(passport);
//        return matcher.find();
//    }
//
//    public boolean dateSheetNumberCheck(String dateSheetNumber){
//        Pattern pattern = Pattern.compile("^([1-9]{1}[0-9]{5})$");
//        Matcher matcher = pattern.matcher(dateSheetNumber);
//        return matcher.find();
//    }
//
//    public boolean fullNameCheck(String fullName){
//        Pattern pattern = Pattern.compile("([А-ЯЁ][а-яё]+[\\-\\s]?){3,}");
//        Matcher matcher = pattern.matcher(fullName);
//        return matcher.find();
//    }
//
//    public boolean stateNumberCheck(String stateNumber){
//        Pattern pattern = Pattern.compile("[А-Я]\\d{3}[А-Я]{2}\\d{2,3}");
//        Matcher matcher = pattern.matcher(stateNumber);
//        return matcher.find();
//    }
//
//    public boolean countryCheck(String country){
//        Pattern pattern = Pattern.compile("^[А-Я]{1}[а-я]{1,35}$");
//        Matcher matcher = pattern.matcher(country);
//        return matcher.find();
//    }

    public StringConverter<Date> getStringConverter(){
        return new StringConverter<>() {
            final String pattern = "yyyy-MM-dd";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

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
                try {
                    if (string != null && !string.isEmpty()) {
                        return Date.valueOf(string);
                    } else {
                        return null;
                    }
                } catch (IllegalArgumentException e){
                    dialogUtil.showErrorMessage("Ошибка", "Неверно введена дата! Необходим формат ГГГГ-ММ-ДД");
                }
                return Date.valueOf(string);
            }
        };
    }

    public IntegerStringConverter getIntegerConverter(String message){
        return new IntegerStringConverter(){
            @Override public Integer fromString(String value) {
                if (value == null) {
                    return null;
                }
                value = value.trim();
                if (value.length() < 1) {
                    return null;
                }
                try {
                    Integer.valueOf(value);
                } catch (NumberFormatException e){
                    dialogUtil.showErrorMessage("Ошибка", message);
                }
                return Integer.valueOf(value);
            }
        };
    }

    public DoubleStringConverter getDoubleConverter(String message){
        return new DoubleStringConverter(){
            @Override public Double fromString(String value) {
                if (value == null) {
                    return null;
                }

                value = value.trim();

                if (value.length() < 1) {
                    return null;
                }
                try {
                    return Double.valueOf(value);
                } catch (NumberFormatException e){
                    dialogUtil.showErrorMessage("Ошибка", message);
                }
                return Double.valueOf(value);
            }
        };
    }
}


//проверка ФИО
//проверка даты