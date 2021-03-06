package sample.ui;

import java.sql.SQLException;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import data.entity.AutoShops;
import data.manager.AutoShopsManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import util.CheckUtil;
import util.DialogUtil;

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

    @FXML
    private ImageView contracts_image;

    @FXML
    private ImageView works_image;

    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private SystemHelper systemHelper = new SystemHelper();
    private DialogUtil dialogUtil = new DialogUtil();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
    }

    public void setUserLabel(String name){
        userName = name;
        user_label.setText(name);
        systemHelper.initMenu(name, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    void setTable() throws SQLException {
        TableColumn<AutoShops, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<AutoShops, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell. forTableColumn());

        TableColumn<AutoShops, String> modelsColumn = new TableColumn<>("Обслуживаемые марки");
        modelsColumn.setCellValueFactory(new PropertyValueFactory<>("models"));

        auto_table.setItems(autoShopsManager.getAll());
        auto_table.getColumns().addAll(addressColumn, nameColumn, modelsColumn);
        auto_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        auto_table.setEditable(true);

        addressColumn.setOnEditCommit((TableColumn.CellEditEvent<AutoShops, String> event) -> {
            TablePosition<AutoShops, String> pos = event.getTablePosition();
            AutoShops autoShop = event.getTableView().getItems().get(pos.getRow());
            autoShop.setAddress(event.getNewValue());
            changeCheck(autoShop, event.getNewValue());
        });

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<AutoShops, String> event) -> {
            TablePosition<AutoShops, String> pos = event.getTablePosition();
            AutoShops autoShop = event.getTableView().getItems().get(pos.getRow());
            autoShop.setName(event.getNewValue());
            changeCheck(autoShop, event.getNewValue());
        });

        modelsColumn.setCellFactory(new Callback<TableColumn<AutoShops, String>, TableCell<AutoShops, String>>() {
            @Override
            public TableCell<AutoShops, String> call(TableColumn<AutoShops, String> col) {
                final TableCell<AutoShops, String> cell = new TableCell<AutoShops, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else setText(firstName);
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() > 1) {
                            try {
                                systemHelper.doubleClickOnModels(userName, cell.getTableView().getItems().get(cell.getIndex()), shops_button);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                });
                return cell ;
            }
        });
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addAutoShop();
        });
    }

    @FXML
    private void deleteRow() {
        AutoShops autoShop = auto_table.getSelectionModel().getSelectedItem();
        if(autoShop != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
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
            dialogUtil.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    void changeCheck(AutoShops autoShop, String message){
        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
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
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Автомастерская").showAndWait();
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
            dialogUtil.showErrorMessage("Ошибка", "Введите данные!");
        }
    }
}
