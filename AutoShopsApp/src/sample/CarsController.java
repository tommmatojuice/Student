package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.security.cert.Extension;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class CarsController {

    @FXML
    private TableView<Cars> cars_table = new TableView<>();

    @FXML
    private TextField state_number_enter;

    @FXML
    private JFXButton add_button;

    @FXML
    private TextField date_sheet_number_enter;

    @FXML
    private ComboBox<CarModel> model_enter;

    @FXML
    private ComboBox<Customer> customer_enter;

    @FXML
    private DatePicker year_of_issur_enter;

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

    private SystemHelper systemHelper = new SystemHelper();
    private CarsManager carsManager = new CarsManager();
    private CustomerManager customerManager = new CustomerManager();
    private CarModelManager carModelManager = new CarModelManager();
    private String userName;
    private Customer customer;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        model_enter.setItems(carModelManager.getAll());
        customer_enter.setItems(customerManager.getAll());
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Cars car = cars_table.getSelectionModel().getSelectedItem();
        if (car != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    carsManager.deleteById(car.getStateNumber());
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
        systemHelper.initMenu(name, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    public void setCustomer(Customer customer) throws SQLException {
        customer_enter.setValue(customer);
        this.customer = customer;
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addСar();
        });
    }

    private void setTable() throws SQLException
    {
        TableColumn<Cars, String> stateColumn = new TableColumn<>("Госномер");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("stateNumber"));

        TableColumn<Cars, Date> yearColumn = new TableColumn<>("Год выпуска");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfIssue"));
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
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
        }));

        TableColumn<Cars,Integer> dateSheetNumberColumn = new TableColumn<>("Номер техпаспорта");
        dateSheetNumberColumn.setCellValueFactory(new PropertyValueFactory<>("dateSheetNumber"));
        dateSheetNumberColumn.setCellFactory(TextFieldTableCell .forTableColumn(new IntegerStringConverter()));

        TableColumn<Cars, CarModel> modelColumn = new TableColumn<>("Модель");
        modelColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<CarModel> call(TableColumn.CellDataFeatures<Cars, CarModel> param) {
                Cars car = param.getValue();
                int modelId = car.getModelId();
                CarModel carModel = null;
                try {
                    carModel = carModelManager.getById(modelId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return new SimpleObjectProperty<>(carModel);
            }
        });
        modelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(carModelManager.getAll()));

        TableColumn<Cars, Customer> customerColumn = new TableColumn<>("Владелец");
        customerColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<Customer> call(TableColumn.CellDataFeatures<Cars, Customer> param) {
                Cars car = param.getValue();
                int customerId = car.getCustomerId();
                Customer customer = null;
                try {
                    customer = customerManager.getById(customerId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return new SimpleObjectProperty<>(customer);
            }
        });
        customerColumn.setCellFactory(ComboBoxTableCell.forTableColumn(customerManager.getAll()));

        yearColumn.setOnEditCommit((TableColumn.CellEditEvent<Cars, Date> event) ->{
            TablePosition<Cars, Date> pos = event.getTablePosition();
            Cars car = event.getTableView().getItems().get(pos.getRow());
            car.setYearOfIssue(event.getNewValue());
            changeCheck(car, "Год выпуска");
        });

        dateSheetNumberColumn.setOnEditCommit((TableColumn.CellEditEvent<Cars, Integer> event) ->{
            TablePosition<Cars, Integer> pos = event.getTablePosition();
            Cars car = event.getTableView().getItems().get(pos.getRow());
            car.setDateSheetNumber(event.getNewValue());
            changeCheck(car, "Номер техпаспорта");
        });

        modelColumn.setOnEditCommit((TableColumn.CellEditEvent<Cars, CarModel> event) -> {
            TablePosition<Cars, CarModel> pos = event.getTablePosition();
            Cars car = event.getTableView().getItems().get(pos.getRow());
            car.setModelId(event.getNewValue().getModelId());
            changeCheck(car, "Марка");
        });

        customerColumn.setOnEditCommit((TableColumn.CellEditEvent<Cars, Customer> event) -> {
            TablePosition<Cars, Customer> pos = event.getTablePosition();
            Cars car = event.getTableView().getItems().get(pos.getRow());
            car.setCustomerId(event.getNewValue().getId());
            changeCheck(car, "Владелец");
        });

        searchTable();
        cars_table.getColumns().addAll(stateColumn, yearColumn, dateSheetNumberColumn, modelColumn, customerColumn);
        cars_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cars_table.setEditable(true);
    }

    private void changeCheck(Cars car, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(carsManager.update(car));
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

    private void addСar(){
        if(!state_number_enter.getText().isEmpty() && !year_of_issur_enter.getValue().toString().isEmpty()
                && !date_sheet_number_enter.getText().isEmpty() && model_enter.getItems() != null && customer_enter.getItems() != null){
            if (systemHelper.stateNumberCheck(state_number_enter.getText())){
                if (date_sheet_number_enter.getText().length() == 6){
                    Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Клиент").showAndWait();
                    if (option.get() == ButtonType.OK) {
                        try {
                            if (this.customer == null){
                                carsManager.add(new Cars(state_number_enter.getText(), Date.valueOf(year_of_issur_enter.getValue()),
                                        Integer.parseInt(date_sheet_number_enter.getText()), model_enter.getValue().getModelId(), customer_enter.getValue().getId()));
                            } else {
                                System.out.println(customer.getId());
                                carsManager.add(new Cars(state_number_enter.getText(), Date.valueOf(year_of_issur_enter.getValue()),
                                        Integer.parseInt(date_sheet_number_enter.getText()), model_enter.getValue().getModelId(), this.customer.getId()));
                            }
                            searchTable();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        System.out.println("Add new!");
                    }
                } else systemHelper.showErrorMessage("Ошибка", "Неверно введен номер техпаспорта!");
            } else systemHelper.showErrorMessage("Ошибка", "Неверно введен госномер автомобиля!");
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }

    public void searchTable() throws SQLException {
        FilteredList<Cars> filteredList = new FilteredList<>(carsManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(car -> {
                try
                {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if(car.getStateNumber().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(car.getStateNumber().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(car.getDateSheetNumber()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(carModelManager.getById(car.getModelId()).toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(customerManager.getById(car.getCustomerId()).toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else return false;
                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            });
        });
        SortedList<Cars> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(cars_table.comparatorProperty());
        cars_table.setItems(sortedList);
    }
}