package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

public class ContractsController
{
    @FXML
    private TableView<Contracts> contract_table = new TableView<>();

    @FXML
    private JFXButton add_button;

    @FXML
    private DatePicker open_data_enter;

    @FXML
    private DatePicker close_data_enter;

    @FXML
    private ComboBox<StatusEnum> status_enter;

    @FXML
    private ComboBox<Cars> auto_number_enter;

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
    private final CarsManager carsManager = new CarsManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final ContractManager contractManager = new ContractManager();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        status_enter.getItems().setAll(StatusEnum.values());
        auto_number_enter.setItems(carsManager.getAll());
    }

    @FXML
    private void deleteRow() {
        Contracts contract = contract_table.getSelectionModel().getSelectedItem();
        if (contract != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.isPresent()) {
                try {
                    contractManager.deleteById(contract.getId());
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
        this.userName = name;
        user_label.setText(name);
        systemHelper.initMenu(name, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addContract();
        });
    }

    private void setTable() throws SQLException
    {
        TableColumn<Contracts,Integer> numberColumn = new TableColumn<>("Номер контракта");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numberColumn.setCellFactory(new Callback<TableColumn<Contracts, Integer>, TableCell<Contracts, Integer>>() {
            @Override
            public TableCell<Contracts, Integer> call(TableColumn<Contracts, Integer> col) {
                final TableCell<Contracts, Integer> cell = new TableCell<Contracts, Integer>() {
                    @Override
                    public void updateItem(Integer number, boolean empty) {
                        super.updateItem(number, empty);
                        if (empty) {
                            setText(null);
                        } else setText(number.toString());
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() > 1) {
                            try {
                                systemHelper.doubleClickOnContract(userName, cell.getTableView().getItems().get(cell.getIndex()), shops_button);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                });
                return cell ;
            }
        });

        TableColumn<Contracts, Date> openDateColumn = new TableColumn<>("Дата открытия контракта");
        openDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOpen"));
        openDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        TableColumn<Contracts, Date> closeDateColumn = new TableColumn<>("Дата закрытия контракта");
        closeDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateClose"));
        closeDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        TableColumn<Contracts, StatusEnum> statusColumn = new TableColumn<>("Статус");
        statusColumn.setCellValueFactory(param -> {
            Contracts contract = param.getValue();
            String statusValue = contract.getStatusEnum().name();
            StatusEnum status = StatusEnum.valueOf(statusValue);
            return new SimpleObjectProperty<StatusEnum>(status);
        });
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(StatusEnum.values())));

        TableColumn<Contracts, Cars> carColumn = new TableColumn<>("Госномер автомобиля");
        carColumn.setCellValueFactory(param -> {
            Contracts contract = param.getValue();
            String carId = contract.getStateNumber();
            Cars car = null;
            try {
                car = carsManager.getById(carId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(car);
        });
        carColumn.setCellFactory(ComboBoxTableCell.forTableColumn(carsManager.getAll()));

        TableColumn<Contracts, Customer> customerColumn = new TableColumn<>("Клиент");
        customerColumn.setCellValueFactory(param -> {
            Contracts contract = param.getValue();
            int customerId = 0;
            try {
                customerId = carsManager.getById(contract.getStateNumber()).getCustomerId();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Customer customer = null;
            try {
                customer = customerManager.getById(customerId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(customer);
        });

        openDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Contracts, Date> event) ->{
            TablePosition<Contracts, Date> pos = event.getTablePosition();
            Contracts contract = event.getTableView().getItems().get(pos.getRow());
            contract.setDateOpen(event.getNewValue());
            changeCheck(contract, event.getNewValue().toString());
        });

        closeDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Contracts, Date> event) ->{
            TablePosition<Contracts, Date> pos = event.getTablePosition();
            Contracts contract = event.getTableView().getItems().get(pos.getRow());
            contract.setDateClose(event.getNewValue());
            changeCheck(contract, event.getNewValue().toString());
        });

        statusColumn.setOnEditCommit((TableColumn.CellEditEvent<Contracts, StatusEnum> event) ->{
            TablePosition<Contracts, StatusEnum> pos = event.getTablePosition();
            Contracts contract = event.getTableView().getItems().get(pos.getRow());
            contract.setStatusEnum(event.getNewValue());
            changeCheck(contract, event.getNewValue().toString());
        });

        carColumn.setOnEditCommit((TableColumn.CellEditEvent<Contracts, Cars> event) ->{
            TablePosition<Contracts, Cars> pos = event.getTablePosition();
            Contracts contract = event.getTableView().getItems().get(pos.getRow());
            contract.setStateNumber(event.getNewValue().getStateNumber());
            changeCheck(contract, event.getNewValue().getStateNumber());
        });

        searchTable();
        contract_table.getColumns().addAll(numberColumn, openDateColumn, closeDateColumn, statusColumn, carColumn, customerColumn);
        contract_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contract_table.setEditable(true);
    }

    private void changeCheck(Contracts contract, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.isPresent()) {
            try {
                contractManager.update(contract);
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

    private void addContract(){
        if(!open_data_enter.getValue().toString().isEmpty() && status_enter.getItems() != null && auto_number_enter.getItems() != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Контракт").showAndWait();
            if (option.isPresent()) {
                try {
                    if(close_data_enter.getValue() != null){
                        System.out.println(1);
                        contractManager.add(new Contracts(Date.valueOf(open_data_enter.getValue()), Date.valueOf(close_data_enter.getValue()),
                                status_enter.getValue(), auto_number_enter.getValue().getStateNumber()));
                    } else {
                        contractManager.add(new Contracts(Date.valueOf(open_data_enter.getValue()), null,
                                status_enter.getValue(), auto_number_enter.getValue().getStateNumber()));
                    }
                    searchTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }

    public void searchTable() throws SQLException {
        FilteredList<Contracts> filteredList = new FilteredList<>(contractManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(contract -> {
            try
            {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if(contract.getDateOpen().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(contract.getDateClose().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(contract.getStatusEnum().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(contract.getStateNumber().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else return customerManager.getById(carsManager.getById(contract.getStateNumber()).getCustomerId()).toString().toLowerCase().contains(lowerCaseFilter);
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }));
        SortedList<Contracts> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(contract_table.comparatorProperty());
        contract_table.setItems(sortedList);
    }
}
