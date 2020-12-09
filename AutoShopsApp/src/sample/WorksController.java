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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.*;
import java.util.Optional;

public class WorksController
{
    @FXML
    private TableView<Works> works_table;

    @FXML
    private JFXButton add_button;

    @FXML
    private ComboBox<AutoShops> shop_enter;

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
    private Contracts contract;
    private String userName;

    @FXML
    void initialize(String userName, Contracts contract) throws SQLException {
        this.contract = contract;
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        shop_enter.setItems(autoShopsManager.getAll());
        if (!works_table.getItems().isEmpty())
            shop_enter.setValue(autoShopsManager.getById(works_table.getItems().get(0).getShopId()));
        if (shop_enter.getValue() != null)
            master_enter.setItems(mastersManager.getByShop(shop_enter.getValue().getShop_number()));
        work_info.setText("Контракт: №" + contract.getId());
        user_label.setText(userName);
        systemHelper.initMenu(userName, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);

        ChangeListener<AutoShops> changeListener = (observable, oldValue, newValue) -> {
            try {
                master_enter.setItems(mastersManager.getByShop(newValue.getShop_number()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        shop_enter.getSelectionModel().selectedItemProperty().addListener(changeListener);
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
        TableColumn<Works, AutoShops> shopColumn = new TableColumn<>("Автомастерская");
        shopColumn.setCellValueFactory(param -> {
            Works work = param.getValue();
            int shopId = work.getShopId();
            AutoShops autoShop = null;
            try {
                autoShop = autoShopsManager.getById(shopId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new SimpleObjectProperty<>(autoShop);
        });
        shopColumn.setCellFactory(ComboBoxTableCell.forTableColumn(autoShopsManager.getAll()));

        TableColumn<Works, Masters> masterColumn = new TableColumn<>("Мастер");
        masterColumn.setCellValueFactory(param -> {
            Works work = param.getValue();
            int masterId = work.getShopId();
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

        shopColumn.setOnEditCommit((TableColumn.CellEditEvent<Works, AutoShops> event) ->{
            TablePosition<Works, AutoShops> pos = event.getTablePosition();
            Works work = event.getTableView().getItems().get(pos.getRow());
            work.setShopId(event.getNewValue().getShop_number());
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
        works_table.getColumns().addAll(shopColumn, masterColumn, receiptDateColumn, completionDateColumn, actualCompletionDateColumn);
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
        System.out.println(shop_enter.getItems());
        if(!receip_date_enter.getValue().toString().isEmpty() && !completion_date_enter.getValue().toString().isEmpty()
                && shop_enter.getItems() != null && master_enter.getItems() != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Ремонтная работа").showAndWait();
            if (option.isPresent()) {
                try {
                    if(f_completion_date_enter.getValue() != null){
                        worksManager.add(new Works(shop_enter.getValue().getShop_number(), this.contract.getId(), master_enter.getValue().getMasterId(),
                                Date.valueOf(receip_date_enter.getValue()), Date.valueOf(completion_date_enter.getValue()), Date.valueOf(f_completion_date_enter.getValue())));
                    } else {
                        worksManager.add(new Works(shop_enter.getValue().getShop_number(), this.contract.getId(), master_enter.getValue().getMasterId(),
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
