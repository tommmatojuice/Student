package sample;

import com.jfoenix.controls.JFXButton;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerController {

    @FXML
    private TableView<Customer> customers_table = new TableView<>();

    @FXML
    private TextField name_enter;

    @FXML
    private TextField address_enter;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private TextField phone_enter;

    @FXML
    private TextField passport_enter;

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
    private TextField search_enter;

    private SystemHelper systemHelper = new SystemHelper();
    private CustomerManager customerManager = new CustomerManager();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
    }

    @FXML
    private void deleteRow() {
        Customer customer = customers_table.getSelectionModel().getSelectedItem();
        if (customer != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    customerManager.deleteById(customer.getId());
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

    public void setUserName(String name){
        userName = name;
        user_label.setText(name);
        systemHelper.initMenu(name, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            try {
                addCustomer();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private void setTable() throws SQLException {
        TableColumn<Customer, String> nameColumn = new TableColumn<>("ФИО");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Customer, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Customer, String> phoneColumn = new TableColumn<>("Телефон");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Customer, String> passportColumn = new TableColumn<>("Серия и номер паспорта");
        passportColumn.setCellValueFactory(new PropertyValueFactory<>("passport"));
        passportColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        searchTable();
        customers_table.getColumns().addAll(nameColumn, addressColumn, phoneColumn, passportColumn);
        customers_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        customers_table.setEditable(true);

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Customer, String> event) -> {
            TablePosition<Customer, String> pos = event.getTablePosition();
            Customer customer = event.getTableView().getItems().get(pos.getRow());
            if(systemHelper.fullNameCheck(event.getNewValue())){
                customer.setFullName(event.getNewValue());
                changeCheck(customer, event.getNewValue());
            } else {
                systemHelper.showErrorMessage("Ошибка", "ФИО введены неверно!");
                try {
                    customers_table.setItems(customerManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        addressColumn.setOnEditCommit((TableColumn.CellEditEvent<Customer, String> event) ->{
            TablePosition<Customer, String> pos = event.getTablePosition();
            Customer customer = event.getTableView().getItems().get(pos.getRow());
            customer.setAddress(event.getNewValue());
            changeCheck(customer, event.getNewValue());
        });

        phoneColumn.setOnEditCommit((TableColumn.CellEditEvent<Customer, String> event) ->{
            TablePosition<Customer, String> pos = event.getTablePosition();
            Customer customer = event.getTableView().getItems().get(pos.getRow());
            if (systemHelper.phoneCheck(event.getNewValue())){
                try {
                    if(!customerManager.getByPhone(event.getNewValue())){
                        customer.setPhone(event.getNewValue());
                        changeCheck(customer, event.getNewValue());
                    } else systemHelper.showErrorMessage("Ошибка", "Клиент с таким номером телефона уже существует!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                systemHelper.showErrorMessage("Ошибка", "Неверный формат телефона!");
                try {
                    customers_table.setItems(customerManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        passportColumn.setOnEditCommit((TableColumn.CellEditEvent<Customer, String> event) ->{
            TablePosition<Customer, String> pos = event.getTablePosition();
            Customer customer = event.getTableView().getItems().get(pos.getRow());
            if (systemHelper.passportCheck(event.getNewValue())){
                customer.setPassport(event.getNewValue());
                changeCheck(customer, event.getNewValue());
            } else {
                systemHelper.showErrorMessage("Ошибка", "Неверные серия и номер паспорта!");
                try {
                    customers_table.setItems(customerManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        });
    }

    private void changeCheck(Customer customer, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(customerManager.update(customer));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                customers_table.setItems(customerManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addCustomer() throws SQLException {
        if(!name_enter.getText().isEmpty() && !address_enter.getText().isEmpty() && !phone_enter.getText().isEmpty() && !passport_enter.getText().isEmpty()){
            if (systemHelper.phoneCheck(phone_enter.getText())){
                if (systemHelper.passportCheck(passport_enter.getText())){
                    if(systemHelper.fullNameCheck(name_enter.getText())){
                        if(!customerManager.getByPhone(phone_enter.getText())){
                            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Клиент").showAndWait();
                            if (option.get() == ButtonType.OK) {
                                try {
                                    Customer customer = customerManager.add(new Customer(name_enter.getText(), address_enter.getText(), phone_enter.getText(), passport_enter.getText()));
                                    customers_table.setItems(customerManager.getAll());
                                    goToCars(customer);
                                } catch (SQLException | IOException throwables) {
                                    throwables.printStackTrace();
                                }
                                System.out.println("Add new!");
                            }
                        } else systemHelper.showErrorMessage("Ошибка", "Клиент с таким номером телефона уже существует!");
                    } else systemHelper.showErrorMessage("Ошибка", "ФИО введены неверно!");
                } else systemHelper.showErrorMessage("Ошибка", "Серия и номер паспарта должно состоять из 10 символов!");
            } else systemHelper.showErrorMessage("Ошибка", "Неверный формат телефона!");
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }

    public void searchTable() throws SQLException {
        FilteredList<Customer> filteredList = new FilteredList<>(customerManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if(customer.getFullName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(customer.getAddress().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(customer.getPassport().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else return false;
            });
        });
        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customers_table.comparatorProperty());
        customers_table.setItems(sortedList);
    }

    public void goToCars(Customer customer) throws IOException, SQLException {
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить автомобиль", "Добавить автомобить клиента?", customer.toString()).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = systemHelper.showScene("cars.fxml");
                CarsController controller = loader.getController();
                controller.setUserName(userName);
                controller.setCustomer(customer);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}