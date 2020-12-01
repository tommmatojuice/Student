package sample;

import com.jfoenix.controls.JFXButton;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.Optional;

public class CarModelsController {

    @FXML
    private TableView<CarModel> models_table = new TableView<>();

    @FXML
    private TextField model_enter;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

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

    private SystemHelper systemHelper = new SystemHelper();
    private CarModelManager carModelManager = new CarModelManager();
    private String userName;

    @FXML
    void initialize() throws SQLException {
        setTable();
        initButtons();
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        CarModel carModel = models_table.getSelectionModel().getSelectedItem();
        if (carModel != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    carModelManager.deleteById(carModel.getModelId());
                    models_table.getItems().removeAll(models_table.getSelectionModel().getSelectedItem());
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
        userName = name;
        user_label.setText(name);
        systemHelper.initMenu(name, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addCarModel();
        });
    }

    private void setTable() throws SQLException {
        TableColumn<CarModel, String> nameColumn = new TableColumn<>("Название марки");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        models_table.setItems(carModelManager.getAll());
        models_table.getColumns().add(nameColumn);
        models_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        models_table.setEditable(true);

        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<CarModel, String> event) -> {
            TablePosition<CarModel, String> pos = event.getTablePosition();
            CarModel carModel = event.getTableView().getItems().get(pos.getRow());
            carModel.setName(event.getNewValue());
            changeCheck(carModel, "Название марки");
        });
    }

    private void changeCheck(CarModel carModel, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(carModelManager.update(carModel));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                models_table.setItems(carModelManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addCarModel(){
        if(!model_enter.getText().isEmpty()){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Клиент").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    carModelManager.add(new CarModel(model_enter.getText(), 0 , 0));
                    models_table.setItems(carModelManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }
}