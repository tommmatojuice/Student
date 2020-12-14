package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;

public class AnalyticsTableController {

    @FXML
    private TableView main_table = new TableView();

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
    private final AnalyticsManager analyticsManager = new AnalyticsManager();
    private int numberOfTable;
    private String userName;

    @FXML
    void initialize(int numberOfTable, String userName) throws SQLException {
        this.numberOfTable = numberOfTable;
        this.userName = userName;
        setTable();
        initElements();
    }

    private void initElements() throws SQLException {
        user_label.setText(userName);
        systemHelper.initMenu(userName, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void setTable() throws SQLException {
        if (numberOfTable == 1){
            setWorksTable();
        } else setConsumablesTable();
    }

    private void setWorksTable() throws SQLException {
        TableColumn<WorksAnalytics, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<WorksAnalytics, Integer> countColumn = new TableColumn<>("Количество");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<WorksAnalytics, Double> priceColumn = new TableColumn<>("Доход (руб.)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("income"));

        searchWorkTable();
        System.out.println(analyticsManager.getWorks());
        main_table.getColumns().addAll(nameColumn, countColumn, priceColumn);
        main_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void searchWorkTable() throws SQLException {
        FilteredList<WorksAnalytics> filteredList = new FilteredList<>(analyticsManager.getWorks(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(work -> {
                try
                {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if(work.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(work.getCount()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else return String.valueOf(work.getIncome()).toLowerCase().contains(lowerCaseFilter);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            });
        });
        SortedList<WorksAnalytics> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(main_table.comparatorProperty());
        main_table.setItems(sortedList);
    }

    private void setConsumablesTable() throws SQLException {
        TableColumn<ConsumablesAnalytics, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ConsumablesAnalytics, TypesEnum> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsumablesAnalytics, TypesEnum>, ObservableValue<TypesEnum>>() {
            @Override
            public ObservableValue<TypesEnum> call(TableColumn.CellDataFeatures<ConsumablesAnalytics, TypesEnum> param) {
                ConsumablesAnalytics consumablesAnalytics = param.getValue();
                String typeValue = consumablesAnalytics.getType().name();
                TypesEnum type = TypesEnum.valueOf(typeValue);
                return new SimpleObjectProperty<TypesEnum>(type);
            }
        });

        TableColumn<ConsumablesAnalytics, UnitsEnum> unitColumn = new TableColumn<>("Единица измерения");
        unitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsumablesAnalytics, UnitsEnum>, ObservableValue<UnitsEnum>>() {
            @Override
            public ObservableValue<UnitsEnum> call(TableColumn.CellDataFeatures<ConsumablesAnalytics, UnitsEnum> param) {
                ConsumablesAnalytics consumablesAnalytics = param.getValue();
                String unitValue = consumablesAnalytics.getUnit().name();
                UnitsEnum unit = UnitsEnum.valueOf(unitValue);
                return new SimpleObjectProperty<UnitsEnum>(unit);
            }
        });

        TableColumn<ConsumablesAnalytics,Double> countColumn = new TableColumn<>("Количество");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<ConsumablesAnalytics,Double> priceColumn = new TableColumn<>("Расходы (руб.)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        searchConsumableTable();
        main_table.getColumns().addAll(nameColumn, typeColumn, unitColumn, countColumn, priceColumn);
        main_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void searchConsumableTable() throws SQLException {
        FilteredList<ConsumablesAnalytics> filteredList = new FilteredList<>(analyticsManager.getConsumables(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(consumable -> {
                try
                {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if(consumable.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(consumable.getCount()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(consumable.getPrice()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(consumable.getType().toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else return consumable.getUnit().toString().toLowerCase().contains(lowerCaseFilter);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            });
        });
        SortedList<ConsumablesAnalytics> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(main_table.comparatorProperty());
        main_table.setItems(sortedList);
    }
}