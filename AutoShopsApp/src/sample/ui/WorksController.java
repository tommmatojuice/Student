package sample.ui;

import com.jfoenix.controls.JFXButton;
import data.entity.Contracts;
import data.entity.Masters;
import data.entity.Services;
import data.entity.Works;
import data.manager.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

    @FXML
    private JFXButton delete_button;

    @FXML
    private ImageView shops_image;

    @FXML
    private ImageView masters_image;

    private final SystemHelper systemHelper = new SystemHelper();
    private DialogUtil dialogUtil = new DialogUtil();
    private CheckUtil checkUtil = new CheckUtil();
    private final WorksManager worksManager = new WorksManager();
    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private final MastersManager mastersManager = new MastersManager();
    private final ServicesManager servicesManager = new ServicesManager();
    private final CarsManager carsManager = new CarsManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final ContractManager contractManager = new ContractManager();
    private Contracts contract;
    private String userName;
    private int role;

    @FXML
    public void initialize(String userName, int role, Contracts contract) throws SQLException {
        this.contract = contract;
        this.userName = userName;
        this.role = role;
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        service_enter.setItems(servicesManager.getAll());
        master_enter.setItems(mastersManager.getByModel(carsManager.getById(contract.getStateNumber()).getModelId()));
//        if (!works_table.getItems().isEmpty())
//            shop_enter.setValue(autoShopsManager.getById(works_table.getItems().get(0).getShopId()));
//        if (shop_enter.getValue() != null)
//            master_enter.setItems(mastersManager.getByShop(shop_enter.getValue().getShop_number()));
        work_info.setText("Контракт: №" + contract.getId() + "\n" + "Автомобиль: " + contract.getStateNumber() + "\n" +
                "Клиент: " + customerManager.getById(carsManager.getById(contract.getStateNumber()).getCustomerId()).toString());
        user_label.setText(userName);
        cost_label.setText("Сумма: " + contractManager.getCost(this.contract.getId()).toString() + " руб.");
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

            service_enter.setVisible(false);
            f_completion_date_enter.setVisible(false);
            completion_date_enter.setVisible(false);
            receip_date_enter.setVisible(false);
            master_enter.setVisible(false);
            add_button.setVisible(false);
            delete_button.setVisible(false);

            shops_image.setImage(new Image("sample/baseline_library_books_white_48dp.png"));
            masters_image.setImage(new Image("sample/baseline_build_white_48dp.png"));
        }
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
            try {
                addWork();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Works work = works_table.getSelectionModel().getSelectedItem();
        if (work != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    worksManager.deleteById(work.getWorkId());
                    works_table.setItems(worksManager.getByContract(contract.getId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            dialogUtil.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    private void setTable() throws SQLException
    {
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

        TableColumn<Works, Date> receiptDateColumn = new TableColumn<>("Дата начала ремонта");
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));

        TableColumn<Works, Date> completionDateColumn = new TableColumn<>("Дата окончания ремонта");
        completionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));

        TableColumn<Works, Date> actualCompletionDateColumn = new TableColumn<>("Фактическая дата окончания ремонта");
        actualCompletionDateColumn.setCellValueFactory(new PropertyValueFactory<>("actualCompletionDate"));
        actualCompletionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

        actualCompletionDateColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, Date> event) ->{
            TablePosition<Works, Date> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setActualCompletionDate(event.getNewValue());
            changeCheck(work, event.getNewValue().toString());
        });

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
                                systemHelper.doubleClickOnConsumables(userName, role, contract, cell.getTableView().getItems().get(cell.getIndex()), shops_button);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                });
                return cell ;
            }
        });

        if(role == 0){
            serviceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(servicesManager.getAll()));
            masterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mastersManager.getByModel(carsManager.getById(contract.getStateNumber()).getModelId())));
            receiptDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));
            completionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getStringConverter()));

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
        }

        works_table.setItems(worksManager.getByContract(contract.getId()));
        works_table.getColumns().addAll(serviceColumn, masterColumn, receiptDateColumn, completionDateColumn,
                actualCompletionDateColumn, priceColumn, consumablesColumn);
        works_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        works_table.setEditable(true);
    }

    private void changeCheck(Works work, String message){
        System.out.println("role" + role);
        if(work.getMasterId() == role || role == 0){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    worksManager.update(work);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    works_table.setItems(worksManager.getByContract(contract.getId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Item change!");
            } else{
                try {
                    works_table.setItems(worksManager.getByContract(contract.getId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            dialogUtil.showErrorMessage("Ошибка", "У вас нет прав на изменение этого поля!");
            try {
                works_table.setItems(worksManager.getByContract(contract.getId()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addWork() throws SQLException {
        if(receip_date_enter.getValue() != null && completion_date_enter.getValue() != null
                && service_enter.getValue() != null && master_enter.getValue() != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Ремонтная работа").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    if(f_completion_date_enter.getValue() != null){
                        worksManager.add(new Works(service_enter.getValue().getSeviceId(), this.contract.getId(), master_enter.getValue().getMasterId(),
                                Date.valueOf(receip_date_enter.getValue()), Date.valueOf(completion_date_enter.getValue()), Date.valueOf(f_completion_date_enter.getValue())));
                    } else {
                        worksManager.add(new Works(service_enter.getValue().getSeviceId(), this.contract.getId(), master_enter.getValue().getMasterId(),
                                Date.valueOf(receip_date_enter.getValue()), Date.valueOf(completion_date_enter.getValue()), null));
                    }
                    works_table.setItems(worksManager.getByContract(contract.getId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
                cost_label.setText("Сумма: " + contractManager.getCost(this.contract.getId()).toString() + " руб.");
            }
        } else dialogUtil.showErrorMessage("Ошибка", "Введите данные!");
    }
}
