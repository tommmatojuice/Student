package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ConsumablesController {

    @FXML private TableView<Consumables> consumables_table;

    @FXML private TextField name_enter;

    @FXML private JFXButton add_button;

    @FXML private JFXButton delete_button;

    @FXML private TextField price_enter;

    @FXML private TextField producer_enter;

    @FXML private ComboBox<Types> type_enter;

    @FXML private ComboBox<Units> unit_enter;

    @FXML private JFXButton out_button;

    @FXML private Label user_label;

    @FXML private JFXButton shops_button;

    @FXML private JFXButton masters_button;

    @FXML private JFXButton model_button;

    @FXML private JFXButton cars_button;

    @FXML private JFXButton client_button;

    @FXML private JFXButton consum_button;

    @FXML private JFXButton work_button;

    @FXML private JFXButton cintract_button;

    @FXML private JFXButton service_button;

    @FXML private JFXButton math_button;

    @FXML private JFXButton users_button;

    private final SystemHelper systemHelper = new SystemHelper();
    private final ConsumablesManager consumablesManager = new ConsumablesManager();

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
        initElements();
    }

    private void initElements() throws SQLException {
        type_enter.getItems().setAll(Types.values());
        unit_enter.getItems().setAll(Units.values());
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Consumables consumable = consumables_table.getSelectionModel().getSelectedItem();
        if (consumable != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    consumablesManager.deleteByID(consumable.getConsumableId());
                    consumables_table.setItems(consumablesManager.getAll());
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
        user_label.setText(name);
        systemHelper.initMenu(name, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addСonsumable();
        });
    }

    private void setTable() throws SQLException
    {
        TableColumn<Consumables, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("consumableName"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Consumables,Types> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consumables, Types>, ObservableValue<Types>>() {
            @Override
            public ObservableValue<Types> call(TableColumn.CellDataFeatures<Consumables, Types> param) {
                Consumables consumable = param.getValue();
                String typeValue = consumable.getConsumableType().name();
                Types type = Types.valueOf(typeValue);
                return new SimpleObjectProperty<Types>(type);
            }
        });
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Types.values())));

        TableColumn<Consumables,Units> unitColumn = new TableColumn<>("Единица измерения");
        unitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consumables, Units>, ObservableValue<Units>>() {
            @Override
            public ObservableValue<Units> call(TableColumn.CellDataFeatures<Consumables, Units> param) {
                Consumables consumable = param.getValue();
                String unitValue = consumable.getConsumableUnit().name();
                Units unit = Units.valueOf(unitValue);
                return new SimpleObjectProperty<Units>(unit);
            }
        });
        unitColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Units.values())));

        TableColumn<Consumables,Double> priceColumn = new TableColumn<>("Стоимость");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("consumablePrice"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        TableColumn<Consumables,String> producerColumn = new TableColumn<>("Производитель");
        producerColumn.setCellValueFactory(new PropertyValueFactory<>("consumableProducer"));
        producerColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, String> event) ->{
            TablePosition<Consumables, String> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableName(event.getNewValue());
            changeCheck(consumable, event.getNewValue());
        });

        typeColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, Types> event) ->{
            TablePosition<Consumables, Types> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableType(event.getNewValue());
            changeCheck(consumable, event.getNewValue().toString());
        });

        unitColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, Units> event) ->{
            TablePosition<Consumables, Units> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableUnit(event.getNewValue());
            changeCheck(consumable, event.getNewValue().toString());
        });

        priceColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, Double> event) ->{
            TablePosition<Consumables, Double> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumablePrice(event.getNewValue());
            changeCheck(consumable, event.getNewValue().toString());
        });

        producerColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, String> event) ->{
            TablePosition<Consumables, String> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableProducer(event.getNewValue());
            changeCheck(consumable, event.getNewValue());
        });

//        searchTable();
        consumables_table.setItems(consumablesManager.getAll());
        consumables_table.getColumns().addAll(nameColumn, typeColumn, unitColumn, priceColumn, producerColumn);
        consumables_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        consumables_table.setEditable(true);
    }

    private void changeCheck(Consumables consumable, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(consumablesManager.update(consumable));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                consumables_table.setItems(consumablesManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addСonsumable(){
        if(!name_enter.getText().isEmpty() && type_enter.getItems() != null
                && unit_enter.getItems() != null && !price_enter.getText().isEmpty() && !producer_enter.getText().isEmpty()){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Расходник").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    consumablesManager.add(new Consumables(name_enter.getText(), type_enter.getValue(), unit_enter.getValue(),
                            Double.parseDouble(price_enter.getText()), producer_enter.getText()));
                    consumables_table.setItems(consumablesManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }

//    public void searchTable() throws SQLException {
//        FilteredList<Cars> filteredList = new FilteredList<>(carsManager.getAll(), b -> true);
//
//        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredList.setPredicate(car -> {
//                try
//                {
//                    if(newValue == null || newValue.isEmpty()){
//                        return true;
//                    }
//
//                    String lowerCaseFilter = newValue.toLowerCase();
//                    if(car.getStateNumber().toLowerCase().contains(lowerCaseFilter)){
//                        return true;
//                    } else if(car.getStateNumber().toLowerCase().contains(lowerCaseFilter)){
//                        return true;
//                    } else if(String.valueOf(car.getDateSheetNumber()).toLowerCase().contains(lowerCaseFilter)){
//                        return true;
//                    } else if(carModelManager.getById(car.getModelId()).toString().toLowerCase().contains(lowerCaseFilter)){
//                        return true;
//                    } else if(customerManager.getById(car.getCustomerId()).toString().toLowerCase().contains(lowerCaseFilter)){
//                        return true;
//                    } else return false;
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                return false;
//            });
//        });
//        SortedList<Cars> sortedList = new SortedList<>(filteredList);
//        sortedList.comparatorProperty().bind(cars_table.comparatorProperty());
//        cars_table.setItems(sortedList);
//    }

}