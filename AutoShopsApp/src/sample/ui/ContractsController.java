package sample.ui;

import com.jfoenix.controls.JFXButton;
import data.entity.Cars;
import data.entity.Contracts;
import data.entity.Customer;
import data.enumes.StatusEnum;
import data.manager.CarsManager;
import data.manager.ContractManager;
import data.manager.CustomerManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import util.CheckUtil;
import util.DialogUtil;

import java.io.IOException;
import java.sql.Connection;
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
    private JFXButton delete_button;

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
    private ImageView shops_image;

    @FXML
    private ImageView masters_image;

    private final SystemHelper systemHelper = new SystemHelper();
    private DialogUtil dialogUtil = new DialogUtil();
    private CheckUtil checkUtil = new CheckUtil();
    private final CarsManager carsManager = new CarsManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final ContractManager contractManager = new ContractManager();
    private String userName;
    private int role;

    @FXML
    public void initialize(String userName, int role) throws SQLException {
        this.userName = userName;
        this.role = role;
        setTable();
        initButtons();
        initElements();
        System.out.println("role "+role);
    }

    private void initElements() throws SQLException {
        status_enter.getItems().setAll(StatusEnum.values());
        auto_number_enter.setItems(carsManager.getAll());
        user_label.setText(userName);
        systemHelper.initMenu(userName, role, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
        if(role != 0){
            model_button.setVisible(false);
            cars_button.setVisible(false);
            client_button.setVisible(false);
            consum_button.setVisible(false);
            work_button.setVisible(false);
            cintract_button.setVisible(false);
            service_button.setVisible(false);
            math_button.setVisible(false);
            users_button.setVisible(false);

            shops_button.setText("Контракты");
            masters_button.setText("Ремонтные работы");

            close_data_enter.setVisible(false);
            auto_number_enter.setVisible(false);
            status_enter.setVisible(false);
            open_data_enter.setVisible(false);
            add_button.setVisible(false);
            delete_button.setVisible(false);

            shops_image.setImage(new Image("sample/baseline_library_books_white_48dp.png"));
            masters_image.setImage(new Image("sample/baseline_build_white_48dp.png"));
        }
    }

    @FXML
    private void deleteRow() {
        Contracts contract = contract_table.getSelectionModel().getSelectedItem();
        if (contract != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    contractManager.deleteById(contract.getId());
                    searchTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            dialogUtil.showErrorMessage("Ошибка", "Выберите запись!");
        }
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
                                systemHelper.doubleClickOnContract(userName, role, cell.getTableView().getItems().get(cell.getIndex()));
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

        TableColumn<Contracts, Date> closeDateColumn = new TableColumn<>("Дата закрытия контракта");
        closeDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateClose"));

        TableColumn<Contracts, StatusEnum> statusColumn = new TableColumn<>("Статус");
        statusColumn.setCellValueFactory(param -> {
            Contracts contract = param.getValue();
            String statusValue = contract.getStatusEnum().name();
            StatusEnum status = StatusEnum.valueOf(statusValue);
            return new SimpleObjectProperty<StatusEnum>(status);
        });

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

        if(role == 0){
            openDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));
            closeDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));
            statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(StatusEnum.values())));
            carColumn.setCellFactory(ComboBoxTableCell.forTableColumn(carsManager.getAll()));

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
        }

        searchTable();
        contract_table.getColumns().addAll(numberColumn, openDateColumn, closeDateColumn, statusColumn, carColumn, customerColumn);
        contract_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contract_table.setEditable(true);
    }

    private void changeCheck(Contracts contract, String message){
        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
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
        if(open_data_enter.getValue() != null && status_enter.getValue() != null && auto_number_enter.getValue() != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Контракт").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    Contracts contract;
                    if(close_data_enter.getValue() != null){
                        contract = contractManager.add(new Contracts(Date.valueOf(open_data_enter.getValue()), Date.valueOf(close_data_enter.getValue()),
                                status_enter.getValue(), auto_number_enter.getValue().getStateNumber()));
                    } else {
                        contract = contractManager.add(new Contracts(Date.valueOf(open_data_enter.getValue()), null,
                                status_enter.getValue(), auto_number_enter.getValue().getStateNumber()));
                    }
                    searchTable();
                    goToContract(contract);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else dialogUtil.showErrorMessage("Ошибка", "Введите данные!");
    }

    public void searchTable() throws SQLException {
        FilteredList<Contracts> filteredList;
        if (role != 0) {
            filteredList = new FilteredList<>(contractManager.getByMaster(role), b -> true);
        } else  filteredList = new FilteredList<>(contractManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(contract -> {
            try
            {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if(String.valueOf(contract.getId()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(contract.getDateOpen().toString().toLowerCase().contains(lowerCaseFilter)){
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

    public void goToContract(Contracts contract) throws SQLException {
        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить контракт", "Добавить ремонтные работы?", "Контракт: " +contract.getId()).showAndWait();
        if (option.get() == ButtonType.OK) {
            systemHelper.doubleClickOnContract(userName, role, contract);
        }
    }
}
