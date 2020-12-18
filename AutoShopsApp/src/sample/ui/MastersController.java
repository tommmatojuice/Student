package sample.ui;

import com.jfoenix.controls.JFXButton;
import data.entity.AutoShops;
import data.entity.Masters;
import data.manager.AutoShopsManager;
import data.manager.MastersManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import util.CheckUtil;
import util.DialogUtil;

public class MastersController {
    @FXML
    private TableView<Masters> masters_table = new TableView<>();

    @FXML
    private ComboBox<AutoShops> shop_enter = new ComboBox<>();

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private TextField name_enter;

    @FXML
    private TextField phone_enter;

    @FXML
    private JFXButton add_button;


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

    private final MastersManager mastersManager = new MastersManager();
    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private SystemHelper systemHelper = new SystemHelper();
    private DialogUtil dialogUtil = new DialogUtil();
    private CheckUtil checkUtil = new CheckUtil();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
        initElements();
    }

    public void initButtons(){
        add_button.setOnAction(event -> {
            try {
                addMaster();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void initElements() throws SQLException {
        shop_enter.setItems(autoShopsManager.getAll());
    }

    public void setUserLabel(String name){
        userName = name;
        user_label.setText(name);
        systemHelper.initMenu(name, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    void setTable() throws SQLException {
        TableColumn<Masters, String> nameColumn = new TableColumn<>("ФИО");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<Masters, String> phoneColumn = new TableColumn<>("Телефон");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<Masters, AutoShops> shopColumn = new TableColumn<>("Автомастерская");
        shopColumn.setCellValueFactory(new Callback<>() {
            public ObservableValue<AutoShops> call(TableColumn.CellDataFeatures<Masters, AutoShops> param) {
                Masters master = param.getValue();
                int shopId = master.getAutoShopId();
                AutoShops autoShop = null;
                try {
                    autoShop = autoShopsManager.getById(shopId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return new SimpleObjectProperty<>(autoShop);
            }
        });
        shopColumn.setCellFactory(ComboBoxTableCell.forTableColumn(autoShopsManager.getAll()));

        masters_table.setItems(mastersManager.getAll());
        masters_table.getColumns().addAll(nameColumn, phoneColumn, shopColumn);
        masters_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        masters_table.setEditable(true);

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Masters, String> event) -> {
            TablePosition<Masters, String> pos = event.getTablePosition();
            Masters masters = event.getTableView().getItems().get(pos.getRow());
            if(checkUtil.fullNameCheck(event.getNewValue())){
                masters.setName(event.getNewValue());
                changeCheck(masters, event.getNewValue());
            } else {
                dialogUtil.showErrorMessage("Ошибка", "Неверные ФИО!");
                try {
                    masters_table.setItems(mastersManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        phoneColumn.setOnEditCommit((TableColumn.CellEditEvent<Masters, String> event) -> {
            TablePosition<Masters, String> pos = event.getTablePosition();
            Masters masters = event.getTableView().getItems().get(pos.getRow());
            if(checkUtil.phoneCheck(event.getNewValue())){
                try {
                    if(!mastersManager.getByPhone(phone_enter.getText())){
                        masters.setPhone(event.getNewValue());
                        changeCheck(masters, event.getNewValue());
                    } else dialogUtil.showErrorMessage("Ошибка", "Мастер с таким номером телефона уже существует!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                dialogUtil.showErrorMessage("Ошибка", "Неверный формат телефона!");
                try {
                    masters_table.setItems(mastersManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        shopColumn.setOnEditCommit((TableColumn.CellEditEvent<Masters, AutoShops> event) -> {
            TablePosition<Masters, AutoShops> pos = event.getTablePosition();
            Masters masters = event.getTableView().getItems().get(pos.getRow());
            masters.setAutoShopId(event.getNewValue().getShop_number());
            changeCheck(masters, event.getNewValue().toString());
        });
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Masters masters = masters_table.getSelectionModel().getSelectedItem();
        if (masters != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    mastersManager.deleteByID(masters.getMasterId());
                    masters_table.getItems().removeAll(masters_table.getSelectionModel().getSelectedItem());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            dialogUtil.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    void changeCheck(Masters masters, String message){
        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(mastersManager.update(masters));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                masters_table.setItems(mastersManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    void addMaster() throws SQLException {
        if(!name_enter.getText().isEmpty() && !phone_enter.getText().isEmpty() && shop_enter.getValue() != null){
            if (checkUtil.phoneCheck(phone_enter.getText())){
                if(!mastersManager.getByPhone(phone_enter.getText())){
                    if(checkUtil.fullNameCheck(name_enter.getText())){
                        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Мастер").showAndWait();
                        if (option.get() == ButtonType.OK) {
                            try {
                                mastersManager.add(new Masters(name_enter.getText(), phone_enter.getText(), shop_enter.getValue().getShop_number()));
                                masters_table.setItems(mastersManager.getAll());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("Add new!");
                        }
                    } else dialogUtil.showErrorMessage("Ошибка", "Неверные ФИО!");
                } else dialogUtil.showErrorMessage("Ошибка", "Мастер с таким номером телефона уже существует!");
            } else dialogUtil.showErrorMessage("Ошибка", "Неверный формат телефона!");
        } else {
            dialogUtil.showErrorMessage("Ошибка", "Введите данные!");
        }
    }
}
