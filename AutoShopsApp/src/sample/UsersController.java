package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

public class UsersController {

    @FXML
    private TableView<Users> users_table;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private ComboBox<Masters> master_enter;

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private TextField search_enter;

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

    private final SystemHelper systemHelper = new SystemHelper();
    private final WorksManager worksManager = new WorksManager();
    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private final MastersManager mastersManager = new MastersManager();
    private final ServicesManager servicesManager = new ServicesManager();
    private final CarsManager carsManager = new CarsManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final ContractManager contractManager = new ContractManager();
    private final UsersManager usersManager = new UsersManager();
    private String userName;

    @FXML
    void initialize(String userName) throws SQLException {
        this.userName = userName;
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        master_enter.setItems(mastersManager.getWithoutAuth());
        user_label.setText(userName);
        systemHelper.initMenu(userName, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            try {
                addUser();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Users user = users_table.getSelectionModel().getSelectedItem();
        if (user != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    usersManager.deleteById(user.getId());
                    searchTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            systemHelper.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    private void setTable() throws SQLException
    {
        TableColumn<Users, String> loginColumn = new TableColumn<>("Логин");
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<Users, String> passColumn = new TableColumn<>("Пароль");
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Users, Masters> masterColumn = new TableColumn<>("Мастер");
        masterColumn.setCellValueFactory(param -> {
            Users user = param.getValue();
            int masterId = user.getMaster();
            Masters master = null;
            try {
                master = mastersManager.getById(masterId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(master);
        });
        masterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mastersManager.getWithoutAuth()));

        masterColumn.setOnEditCommit((TableColumn.CellEditEvent<Users, Masters> event) ->{
            TablePosition<Users, Masters> pos = event.getTablePosition();
            Users user = event.getTableView().getItems().get(pos.getRow());
            user.setMaster(event.getNewValue().getMasterId());
            changeCheck(user, event.getNewValue().toString());
        });

        searchTable();
//        users_table.setItems(usersManager.getAll());
        users_table.getColumns().addAll(masterColumn, loginColumn, passColumn);
        users_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        users_table.setEditable(true);
    }

    private void changeCheck(Users user, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                usersManager.update(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                searchTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                searchTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addUser() throws SQLException {
        if(master_enter.getValue() != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Пользователь").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    Users user = new Users(getRandomLoginOrPass(12), getRandomLoginOrPass(12), master_enter.getValue().getMasterId());
                    usersManager.add(user);
                    searchTable();
                    copyToClipboard(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
                master_enter.setItems(mastersManager.getWithoutAuth());
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }

    private String getRandomLoginOrPass(int length){
        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        StringBuilder lp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            lp.append(charset.charAt(pos));
        }
        return lp.toString();
    }

    private void copyToClipboard(Users user){
        Optional<ButtonType> option = systemHelper.showMyMessage("Добавлен пользователь", null, user.toString()).showAndWait();
        if (option.get() != null) {
            StringSelection stringSelection = new StringSelection(user.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    public void searchTable() throws SQLException {
        FilteredList<Users> filteredList = new FilteredList<>(usersManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                try {
                    if(mastersManager.getById(user.getMaster()).toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(user.getLogin().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(user.getPassword().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else return false;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return false;
            });
        });
        SortedList<Users> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(users_table.comparatorProperty());
        users_table.setItems(sortedList);
    }
}
