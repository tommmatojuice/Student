package sample.ui;

import com.jfoenix.controls.JFXButton;
import data.entity.Consumables;
import data.entity.ConsumablesUse;
import data.entity.Contracts;
import data.entity.Works;
import data.enumes.TypesEnum;
import data.manager.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import util.CheckUtil;
import util.DialogUtil;

import java.sql.SQLException;
import java.util.Optional;

public class ConsumablesUseController {

    @FXML
    private TableView<ConsumablesUse> consumables_table;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton delete_button;

    @FXML
    private ComboBox<TypesEnum> type_enter;

    @FXML
    private ComboBox<Consumables> name_enter;

    @FXML
    private TextField number_enter;

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private Label cons_info;

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
    private ImageView shops_image;

    @FXML
    private JFXButton back_button;

    @FXML
    private ImageView masters_image;

    private final SystemHelper systemHelper = new SystemHelper();
    private DialogUtil dialogUtil = new DialogUtil();
    private CheckUtil checkUtil = new CheckUtil();
    private final WorksManager worksManager = new WorksManager();
    private final ConsumablesManager consumablesManager = new ConsumablesManager();
    private final ConsumablesUseManager consumablesUseManager = new ConsumablesUseManager();
    private final MastersManager mastersManager = new MastersManager();
    private final ServicesManager servicesManager = new ServicesManager();
    private final CustomerManager customerManager = new CustomerManager();
    private final CarsManager carsManager = new CarsManager();
    private Contracts contract;
    private String userName;
    private Works work;
    private int role;

    @FXML
    public void initialize(String userName, int role, Contracts contract, Works work) throws SQLException {
        this.contract = contract;
        this.userName = userName;
        this.work = work;
        this.role = role;
        setTable();
        initButtons();
        initElements();
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        ConsumablesUse consumablesUse = consumables_table.getSelectionModel().getSelectedItem();
        if (consumablesUse != null){
            Optional<ButtonType> option = dialogUtil.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    consumablesUseManager.deleteByID(consumablesUse.getConsumableUseId());
                    consumables_table.setItems(consumablesUseManager.getByWorkId(this.work.getWorkId()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("DELETE!");
            }
        } else {
            dialogUtil.showErrorMessage("Ошибка", "Выберите запись!");
        }
    }

    private void initElements() throws SQLException {
        type_enter.getItems().setAll(TypesEnum.values());
        if (type_enter.getValue() != null)
            name_enter.setItems(consumablesManager.getByType(type_enter.getValue()));

        ChangeListener<TypesEnum> changeListener = (observable, oldValue, newValue) -> {
            try {
                name_enter.setItems(consumablesManager.getByType(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        type_enter.getSelectionModel().selectedItemProperty().addListener(changeListener);

        cons_info.setText("Контракт: №" + contract.getId() + "\n" +"Автомобиль: " +  contract.getStateNumber() + "\n" +
                "Клиент: "+ customerManager.getById(carsManager.getById(contract.getStateNumber()).getCustomerId()).toString());
        cost_label.setText("Тип ремонта: " + servicesManager.getById(work.getServiceId()).getType());
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

            if(work.getMasterId() != role){
                name_enter.setVisible(false);
                type_enter.setVisible(false);
                number_enter.setVisible(false);
                add_button.setVisible(false);
                delete_button.setVisible(false);
            }
        }
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addConsumableUse();
        });

        back_button.setOnAction(event -> {
            try {
                systemHelper.doubleClickOnContract(userName, role, contract);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private void setTable() throws SQLException
    {
        TableColumn<ConsumablesUse, TypesEnum> typeColumn = new TableColumn<>("Тип");
//        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsumablesUse, TypesEnum>, ObservableValue<TypesEnum>>() {
//            @Override
//            public ObservableValue<TypesEnum> call(TableColumn.CellDataFeatures<ConsumablesUse, TypesEnum> param) {
//                ConsumablesUse consumablesUse = param.getValue();
//                String typeValue = null;
//                try {
//                    typeValue = consumablesManager.getById(consumablesUse.getConsumableId()).getConsumableType().name();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//                TypesEnum type = TypesEnum.valueOf(typeValue);
//                return new SimpleObjectProperty<TypesEnum>(type);
//            }
//        });
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<ConsumablesUse, Consumables> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConsumablesUse, Consumables>, ObservableValue<Consumables>>() {
            @Override
            public ObservableValue<Consumables> call(TableColumn.CellDataFeatures<ConsumablesUse, Consumables> param) {
                ConsumablesUse consumablesUse = param.getValue();
                int consumableValue = consumablesUse.getConsumableId();
                Consumables consumables = null;
                try {
                    consumables = consumablesManager.getById(consumableValue);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                return new SimpleObjectProperty<Consumables>(consumables);
            }
        });

//        TableView.TableViewSelectionModel<ConsumablesUse> selectionModel = consumables_table.getSelectionModel();
//        selectionModel.selectedItemProperty().addListener(new ChangeListener<ConsumablesUse>(){
//            @Override
//            public void changed(ObservableValue<? extends ConsumablesUse> observable, ConsumablesUse oldValue, ConsumablesUse newValue) {
//                if(newValue != null){
//                    try {
//                        nameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(consumablesManager.getByType(typeColumn.getCellData(consumables_table.getSelectionModel().getSelectedIndex()))));
////                        nameColumn.setCellFactory(consumablesManager.getByType(typeColumn.getCellData(consumables_table.getSelectionModel().getSelectedIndex())));
//                        System.out.println(consumablesManager.getByType(typeColumn.getCellData(consumables_table.getSelectionModel().getSelectedIndex())));
//                        System.out.println(typeColumn.getCellData(consumables_table.getSelectionModel().getSelectedIndex()));
//                    } catch (SQLException throwables) {
//                        throwables.printStackTrace();
//                    }
//                }
//            }
//        });

        TableColumn<ConsumablesUse, Double> numberColumn = new TableColumn<>("Количество");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        if(role == work.getMasterId() || role==0){
            typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(TypesEnum.values())));
            numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(systemHelper.getDoubleConverter("Неверно введено количество расходника!")));

            typeColumn.setOnEditCommit((TableColumn.CellEditEvent<ConsumablesUse, TypesEnum> event) ->{
                TablePosition<ConsumablesUse, TypesEnum> pos = event.getTablePosition();
                ConsumablesUse consumablesUse = event.getTableView().getItems().get(pos.getRow());
                consumablesUse.setType(event.getNewValue());
                try {
                    nameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(consumablesManager.getByType(typeColumn.getCellData(consumables_table.getSelectionModel().getSelectedIndex()))));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

//            changeCheck(consumablesUse, event.getNewValue().toString());
            });

            nameColumn.setOnEditCommit((TableColumn.CellEditEvent<ConsumablesUse, Consumables> event) ->{
                TablePosition<ConsumablesUse, Consumables> pos = event.getTablePosition();
                ConsumablesUse consumablesUse = event.getTableView().getItems().get(pos.getRow());
                consumablesUse.setConsumableId(event.getNewValue().getConsumableId());
                changeCheck(consumablesUse, event.getNewValue().toString());
            });

            numberColumn.setOnEditCommit((TableColumn.CellEditEvent<ConsumablesUse, Double> event) ->{
                TablePosition<ConsumablesUse, Double> pos = event.getTablePosition();
                ConsumablesUse consumablesUse = event.getTableView().getItems().get(pos.getRow());
                consumablesUse.setNumber(event.getNewValue());
                changeCheck(consumablesUse, event.getNewValue().toString());
            });
        }

        consumables_table.setItems(consumablesUseManager.getByWorkId(this.work.getWorkId()));
        consumables_table.getColumns().addAll(typeColumn, nameColumn, numberColumn);
        consumables_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        consumables_table.setEditable(true);
    }

    private void changeCheck(ConsumablesUse consumablesUse, String message){
        Optional<ButtonType> option = dialogUtil.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                consumablesUseManager.update(consumablesUse);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                consumables_table.setItems(consumablesUseManager.getByWorkId(this.work.getWorkId()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                consumables_table.setItems(consumablesUseManager.getByWorkId(this.work.getWorkId()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addConsumableUse(){
        if(!number_enter.getText().isEmpty() && name_enter.getValue() != null && type_enter.getValue() != null){
            if(checkUtil.countCheck(number_enter.getText())){
                Optional<ButtonType> option = dialogUtil.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Расходник").showAndWait();
                if (option.get() == ButtonType.OK) {
                    try {
                        consumablesUseManager.add(new ConsumablesUse(this.work.getWorkId(), name_enter.getValue().getConsumableId(), Double.parseDouble(number_enter.getText())));
                        consumables_table.setItems(consumablesUseManager.getByWorkId(this.work.getWorkId()));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    System.out.println("Add new!");
                }
            } else  dialogUtil.showErrorMessage("Ошибка", "Неверно введено количество расходника!");
        } else dialogUtil.showErrorMessage("Ошибка", "Введите данные!");
    }
}
