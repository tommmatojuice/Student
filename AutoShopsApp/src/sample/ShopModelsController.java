package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.Optional;

public class ShopModelsController {

    @FXML private TableView<CarModel> models_table = new TableView<>();

    @FXML private JFXButton add_button;

    @FXML private ComboBox<CarModel> model_enter;

    @FXML private JFXButton out_button;

    @FXML private Label user_label;

    @FXML private Label shop_info;

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

    private SystemHelper systemHelper = new SystemHelper();
    private CarModelManager carModelManager = new CarModelManager();
    private AutoShops autoShop;
    private String userName;

    @FXML void initialize(String name, AutoShops autoShop) throws SQLException {
        this.autoShop = autoShop;
        this.userName = name;
        setTable();
        initButtons();
        initElements();
    }

    @FXML private void deleteRow(ActionEvent event) {
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

    private void initButtons(){
        add_button.setOnAction(event -> {
            addCarModel();
        });
    }

    private void initElements() throws SQLException {
        model_enter.setItems(carModelManager.getAll());
        shop_info.setText("Автомастреская: " + autoShop.getName() + "\n" + "Адрес: " + autoShop.getAddress());
        user_label.setText(userName);
        systemHelper.initMenu(userName, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void setTable() throws SQLException {
        TableColumn<CarModel, String> nameColumn = new TableColumn<>("Марка");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("brandName"));

        TableColumn<CarModel, String> modelColumn = new TableColumn<>("Модель");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("modelName"));

        TableColumn<CarModel, String> countryColumn = new TableColumn<>("Страна производтства");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("prodCountry"));

        models_table.setItems(carModelManager.getByShopId(autoShop.getShop_number()));
        models_table.getColumns().addAll(nameColumn, modelColumn, countryColumn);
        models_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        models_table.setEditable(true);
    }

    private void addCarModel(){
        if(model_enter.getItems() != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Клиент").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    System.out.println(this.autoShop.getShop_number());
                    System.out.println(model_enter.getValue().getModelId());
                    carModelManager.addShopHasModel(this.autoShop.getShop_number(), model_enter.getValue().getModelId());
                    models_table.setItems(carModelManager.getByShopId(autoShop.getShop_number()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Add new!");
            }
        } else systemHelper.showErrorMessage("Ошибка", "Введите данные!");
    }
}
