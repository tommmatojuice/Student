package sample;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class AutoshopsController
{
    @FXML
    private TableView<AutoShops> auto_table= new TableView<>();

    @FXML
    private TextField adress_enter;

    @FXML
    private TextField name_enter;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private JFXButton shops_button;

    @FXML
    private JFXButton masters_button;

    @FXML
    private JFXButton model_button;

    @FXML
    private JFXButton cars_button;

    @FXML
    private JFXButton client_button;

    @FXML
    private JFXButton consum_button;

    @FXML
    private JFXButton work_button;

    @FXML
    private JFXButton cintract_button;

    @FXML
    private JFXButton service_button;

    @FXML
    private JFXButton math_button;

    @FXML
    private JFXButton users_button;

    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private SystemHelper systemHelper = new SystemHelper();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
    }

    void setUserLabel(String name){
        userName = name;
        user_label.setText(name);
    }

    void setTable() throws SQLException {
        TableColumn<AutoShops, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<AutoShops, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        auto_table.setItems(autoShopsManager.getAll());
        auto_table.getColumns().addAll(addressColumn, nameColumn);
        auto_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        auto_table.setEditable(true);

        addressColumn.setOnEditCommit((TableColumn.CellEditEvent<AutoShops, String> event) -> {
            TablePosition<AutoShops, String> pos = event.getTablePosition();
            AutoShops autoShop = event.getTableView().getItems().get(pos.getRow());
            autoShop.setAddress(event.getNewValue());
            changeCheck(autoShop, "Адресс");
        });

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<AutoShops, String> event) -> {
            TablePosition<AutoShops, String> pos = event.getTablePosition();
            AutoShops autoShop = event.getTableView().getItems().get(pos.getRow());
            autoShop.setName(event.getNewValue());
            changeCheck(autoShop, "Название");
        });
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addAutoShop();
        });

        out_button.setOnAction(event -> {
            try {
                out_button.getScene().getWindow().hide();
                systemHelper.openWindow("sample.fxml",  out_button.getScene().getWidth());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        masters_button.setOnAction(event -> {
            try {
                masters_button.getScene().getWindow().hide();
                FXMLLoader loader = systemHelper.openWindow("masters.fxml",  masters_button.getScene().getWidth());
                MastersController controllerEditBook = loader.getController();
                controllerEditBook.setUserLabel(userName);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        service_button.setOnAction(event -> {
            try {
                service_button.getScene().getWindow().hide();
                FXMLLoader loader = systemHelper.openWindow("services.fxml", service_button.getScene().getWidth());
                ServicesController controller = loader.getController();
                controller.setUserName(userName);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    @FXML
    private void deleteRow() {
        AutoShops autoShop = auto_table.getSelectionModel().getSelectedItem();
        if(autoShop != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    autoShopsManager.deleteByID(autoShop.getShop_number());
                    auto_table.getItems().removeAll(auto_table.getSelectionModel().getSelectedItem());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            systemHelper.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    void changeCheck(AutoShops autoShop, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(autoShopsManager.update(autoShop));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                auto_table.setItems(autoShopsManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    void addAutoShop() {
        if(!adress_enter.getText().isEmpty() && !name_enter.getText().isEmpty()){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Автомастерская").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    autoShopsManager.add(new AutoShops(adress_enter.getText(), name_enter.getText()));
                    auto_table.setItems(autoShopsManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else {
            systemHelper.showErrorMessage("Ошибка", "Введите данные!");
        }
    }
}
