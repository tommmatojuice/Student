package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.sql.*;
import java.util.Optional;

public class WorksController
{
    @FXML
    private TableView<Works> works_table;

    @FXML
    private JFXButton add_button;

    @FXML
    private ComboBox<Services> service_enter;

    @FXML
    private ComboBox<Masters> master_enter;

    @FXML
    private DatePicker receip_date_enter;

    @FXML
    private DatePicker completion_date_enter;

    @FXML
    private DatePicker f_completion_date_enter;

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private Label work_info;

    @FXML
    private Label cost_label;

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
    private Contracts contract;
    private String userName;

    @FXML
    void initialize(String userName, Contracts contract) throws SQLException {
        this.contract = contract;
        this.userName = userName;
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        service_enter.setItems(servicesManager.getAll());
//        if (!works_table.getItems().isEmpty())
//            shop_enter.setValue(autoShopsManager.getById(works_table.getItems().get(0).getShopId()));
//        if (shop_enter.getValue() != null)
//            master_enter.setItems(mastersManager.getByShop(shop_enter.getValue().getShop_number()));
        work_info.setText("Контракт: №" + contract.getId() + "\n" + "Автомобиль: " + contract.getStateNumber() + "\n" +
                "Клиент: " + customerManager.getById(carsManager.getById(contract.getStateNumber()).getCustomerId()).toString());
        user_label.setText(userName);
        cost_label.setText("Сумма: " + contractManager.getCost(this.contract.getId()).toString() + " руб.");
        systemHelper.initMenu(userName, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);

//        ChangeListener<AutoShops> changeListener = (observable, oldValue, newValue) -> {
//            try {
//                master_enter.setItems(mastersManager.getByShop(newValue.getShop_number()));
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        };
//        shop_enter.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addWork();
        });
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Works work = works_table.getSelectionModel().getSelectedItem();
        if (work != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.isPresent()) {
                try {
                    worksManager.deleteById(work.getWorkId());
                    works_table.setItems(worksManager.getAll());
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
//        TableColumn<Works, AutoShops> shopColumn = new TableColumn<>("Автомастерская");
//        shopColumn.setCellValueFactory(param -> {
//            Works work = param.getValue();
//            int shopId = work.getShopId();
//            AutoShops autoShop = null;
//            try {
//                autoShop = autoShopsManager.getById(shopId);
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//            return new SimpleObjectProperty<>(autoShop);
//        });
//        shopColumn.setCellFactory(ComboBoxTableCell.forTableColumn(autoShopsManager.getAll()));

        TableColumn<Works, Services> serviceColumn = new TableColumn<>("Тип ремонта");
        serviceColumn.setCellValueFactory(param -> {
            Works work = param.getValue();
            int serviceId = work.getServiceId();
            Services service = null;
            try {
                service = servicesManager.getById(serviceId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(service);
        });
        serviceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(servicesManager.getAll()));

        TableColumn<Works, Masters> masterColumn = new TableColumn<>("Мастер");
        masterColumn.setCellValueFactory(param -> {
            Works work = param.getValue();
            int masterId = work.getMasterId();
            Masters master = null;
            try {
                master = mastersManager.getById(masterId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(master);
        });
        masterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mastersManager.getAll()));

        TableColumn<Works, Date> receiptDateColumn = new TableColumn<>("Дата начала ремонта");
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));
        receiptDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        TableColumn<Works, Date> completionDateColumn = new TableColumn<>("Дата окончания ремонта");
        completionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        completionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        TableColumn<Works, Date> actualCompletionDateColumn = new TableColumn<>("Фактическая дата окончания ремонта");
        actualCompletionDateColumn.setCellValueFactory(new PropertyValueFactory<>("actualCompletionDate"));
        actualCompletionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        TableColumn<Works, Double> priceColumn = new TableColumn<>("Стоимость ремонта (руб.)");
        priceColumn.setCellValueFactory(param -> {
            Works work = param.getValue();
            int serviceId = work.getServiceId();
            Double price = null;
            try {
                price = servicesManager.getById(serviceId).getPrice();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(price);
        });

        TableColumn<Works, String> consumablesColumn = new TableColumn<>("Расходники");
        consumablesColumn.setCellValueFactory(new PropertyValueFactory<>("consumables"));
        consumablesColumn.setCellFactory(new Callback<TableColumn<Works, String>, TableCell<Works, String>>() {
            @Override
            public TableCell<Works, String> call(TableColumn<Works, String> param) {
                final TableCell<Works, String> cell = new TableCell<Works, String>() {
                    @Override
                    public void updateItem(String consumables, boolean empty) {
                        super.updateItem(consumables, empty);
                        if (empty) {
                            setText(null);
                        } else setText(consumables.toString());
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() > 1) {
                            try {
                                systemHelper.doubleClickOnConsumables(userName, contract, cell.getTableView().getItems().get(cell.getIndex()), shops_button);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                });
                return cell ;
            }
        });

        serviceColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Services> event) ->{
            TablePosition<Works, Services> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setServiceId(event.getNewValue().getSeviceId());
            changeCheck(work, event.getNewValue().toString());
        });

        masterColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Masters> event) ->{
            TablePosition<Works, Masters> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setMasterId(event.getNewValue().getMasterId());
            changeCheck(work, event.getNewValue().toString());
        });

        receiptDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Date> event) ->{
            TablePosition<Works, Date> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setReceiptDate(event.getNewValue());
            changeCheck(work, event.getNewValue().toString());
        });

        completionDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Date> event) ->{
            TablePosition<Works, Date> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setCompletionDate(event.getNewValue());
            changeCheck(work, event.getNewValue().toString());
        });

        actualCompletionDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Date> event) ->{
            TablePosition<Works, Date> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setActualCompletionDate(event.getNewValue());
            changeCheck(work, event.getNewValue().toString());
        });

        works_table.setItems(worksManager.getByContract(contract.getId()));
        works_table.getColumns().addAll(serviceColumn, masterColumn, receiptDateColumn, completionDateColumn,
                actualCompletionDateColumn, priceColumn, consumablesColumn);
        works_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        works_table.setEditable(true);
    }

    private void changeCheck(Works work, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.isPresent()) {
            try {
                worksManager.update(work);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                works_table.setItems(worksManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                works_table.setItems(worksManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addWork(){
        if(!receip_date_enter.getValue().toString().isEmpty() && !completion_date_enter.getValue().toString().isEmpty()
                && service_enter.getItems() != null && master_enter.getItems() != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Ремонтная работа").showAndWait();
            if (option.isPresent()) {
                try {
                    if(f_completion_date_enter.getValue() != null){
                        worksManager.add(new Works(service_enter.getValue().getSeviceId(), this.contract.getId(), master_enter.getValue().getMasterId(),
                                Date.valueOf(receip_date_enter.getValue()), Date.valueOf(completion_date_enter.getValue()), Date.valueOf(f_completion_date_enter.getValue())));
                    } else {
                        worksManager.add(new Works(service_enter.getValue().getSeviceId(), this.contract.getId(), master_enter.getValue().getMasterId(),
                                Date.valueOf(receip_date_enter.getValue()), Date.valueOf(completion_date_enter.getValue()), null));
                    }
                    works_table.setItems(worksManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }
}
