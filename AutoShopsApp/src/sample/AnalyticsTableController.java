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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.sql.Date;
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

    @FXML
    private ImageView shops_image;

    @FXML
    private ImageView masters_image;

    private final SystemHelper systemHelper = new SystemHelper();
    private final AnalyticsManager analyticsManager = new AnalyticsManager();
    private final ServicesManager servicesManager = new ServicesManager();
    private final MastersManager mastersManager = new MastersManager();
    private final ContractManager contractManager = new ContractManager();
    private final WorksManager worksManager = new WorksManager();
    private int numberOfTable;
    private String userName;
    private int role;

    @FXML
    void initialize(int numberOfTable, String userName, int role) throws SQLException {
        this.numberOfTable = numberOfTable;
        this.userName = userName;
        this.role = role;
        setTable();
        initElements();
    }

    private void initElements() throws SQLException {
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

            shops_image.setImage(new Image("sample/baseline_library_books_white_48dp.png"));
            masters_image.setImage(new Image("sample/baseline_build_white_48dp.png"));
        }
    }

    private void setTable() throws SQLException {
        if (numberOfTable == 1){
            setWorksTable();
        } else if(numberOfTable == 2)
            setConsumablesTable();
        else setAllWorksTable();
    }

    private void setWorksTable() throws SQLException {
        TableColumn<WorksAnalytics, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<WorksAnalytics, Integer> countColumn = new TableColumn<>("Количество работ");
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

        TableColumn<ConsumablesAnalytics,Double> countColumn = new TableColumn<>("Использованное количество");
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

    private void setAllWorksTable() throws SQLException {
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

        TableColumn<Works, Integer> contractColumn = new TableColumn<>("Номер контракта");
        contractColumn.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
        contractColumn.setCellFactory(new Callback<TableColumn<Works, Integer>, TableCell<Works, Integer>>() {
            @Override
            public TableCell<Works, Integer> call(TableColumn<Works, Integer> param) {
                final TableCell<Works, Integer> cell = new TableCell<Works, Integer>() {
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
                                systemHelper.doubleClickOnContract(userName, role, contractManager.getById(cell.getTableView().getItems().get(cell.getIndex()).getContractNumber()), shops_button);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                });
                return cell ;
            }
        });

        searchAllWorksTable();
        main_table.getColumns().addAll(serviceColumn, masterColumn, receiptDateColumn, completionDateColumn,
                actualCompletionDateColumn, priceColumn, contractColumn);
        main_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        main_table.setEditable(true);
    }

    private void searchAllWorksTable() throws SQLException {
        FilteredList<Works> filteredList;
        if(role != 0){
            filteredList = new FilteredList<>(worksManager.getByMaster(role), b -> true);
        } else filteredList = new FilteredList<>(worksManager.getAll(), b -> true);

        search_enter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(work -> {
                try
                {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if(servicesManager.getById(work.getServiceId()).getType().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(mastersManager.getById(work.getMasterId()).toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(work.getReceiptDate()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(work.getCompletionDate()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(String.valueOf(work.getActualCompletionDate()).toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else return String.valueOf(work.getContractNumber()).toLowerCase().contains(lowerCaseFilter);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            });
        });
        SortedList<Works> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(main_table.comparatorProperty());
        main_table.setItems(sortedList);
    }
}