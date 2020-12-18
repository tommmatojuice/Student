package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;
import java.util.Optional;

public class ConsumablesController {

    @FXML private TableView<Consumables> consumables_table;

    @FXML private TextField name_enter;

    @FXML private JFXButton add_button;

    @FXML private JFXButton delete_button;

    @FXML private TextField price_enter;

    @FXML private TextField producer_enter;

    @FXML private ComboBox<TypesEnum> type_enter;

    @FXML private ComboBox<UnitsEnum> unit_enter;

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
        type_enter.getItems().setAll(TypesEnum.values());
        unit_enter.getItems().setAll(UnitsEnum.values());
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
        systemHelper.initMenu(name, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
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

        TableColumn<Consumables, TypesEnum> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consumables, TypesEnum>, ObservableValue<TypesEnum>>() {
            @Override
            public ObservableValue<TypesEnum> call(TableColumn.CellDataFeatures<Consumables, TypesEnum> param) {
                Consumables consumable = param.getValue();
                String typeValue = consumable.getConsumableType().name();
                TypesEnum type = TypesEnum.valueOf(typeValue);
                return new SimpleObjectProperty<TypesEnum>(type);
            }
        });
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(TypesEnum.values())));

        TableColumn<Consumables, UnitsEnum> unitColumn = new TableColumn<>("Единица измерения");
        unitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Consumables, UnitsEnum>, ObservableValue<UnitsEnum>>() {
            @Override
            public ObservableValue<UnitsEnum> call(TableColumn.CellDataFeatures<Consumables, UnitsEnum> param) {
                Consumables consumable = param.getValue();
                String unitValue = consumable.getConsumableUnit().name();
                UnitsEnum unit = UnitsEnum.valueOf(unitValue);
                return new SimpleObjectProperty<UnitsEnum>(unit);
            }
        });
        unitColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(UnitsEnum.values())));

        TableColumn<Consumables,Double> priceColumn = new TableColumn<>("Стоимость");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("consumablePrice"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getDoubleConverter("Неверно введена стоимость!")));

        TableColumn<Consumables,String> producerColumn = new TableColumn<>("Производитель");
        producerColumn.setCellValueFactory(new PropertyValueFactory<>("consumableProducer"));
        producerColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, String> event) ->{
            TablePosition<Consumables, String> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableName(event.getNewValue());
            changeCheck(consumable, event.getNewValue());
        });

        typeColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, TypesEnum> event) ->{
            TablePosition<Consumables, TypesEnum> pos = event.getTablePosition();
            Consumables consumable = event.getTableView().getItems().get(pos.getRow());
            consumable.setConsumableType(event.getNewValue());
            changeCheck(consumable, event.getNewValue().toString());
        });

        unitColumn.setOnEditCommit((TableColumn.CellEditEvent<Consumables, UnitsEnum> event) ->{
            TablePosition<Consumables, UnitsEnum> pos = event.getTablePosition();
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
            if(systemHelper.priceCheck(price_enter.getText())){
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
            } else systemHelper.showErrorMessage("Ошибка", "Неверно введена стоимость!");
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