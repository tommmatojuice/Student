package sample;

import java.sql.SQLException;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

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
    private AnchorPane main_scene;

    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private final ObservableList<AutoShops> autoShopsList = FXCollections.observableArrayList();
    private SystemHelper helper = new SystemHelper();

    @FXML
    void initialize() throws SQLException {
        setTable();
        add_button.setOnAction(event -> {
            addAutoShop();
        });
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        if(event.isConsumed()){
            AutoShops autoShop = auto_table.getSelectionModel().getSelectedItem();
            Optional<ButtonType> option = helper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
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
            helper.showErrorMessage("Ошибка", "Выберите строку!");
        }

    }

    void setTable() throws SQLException {
        TableColumn<AutoShops, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setMinWidth(350);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<AutoShops, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setMinWidth(350);
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

    void changeCheck(AutoShops autoShop, String message){
        Optional<ButtonType> option = helper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
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
            Optional<ButtonType> option = helper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Автомастерская").showAndWait();
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
            helper.showErrorMessage("Ошибка", "Введите данные!");
        }

    }
}
