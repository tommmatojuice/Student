package sample;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.validation.DoubleValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.w3c.dom.DOMImplementation;

public class ServicesController
{
    @FXML
    private TableView<Services> services_table = new TableView<>();

    @FXML
    private TextField type_enter;

    @FXML
    private TextField price_enter;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton delete_button;

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
    private ServicesManager servicesManager = new ServicesManager();
    private String userName;

    @FXML
    private void initialize() throws SQLException {
        initButtons();
        setTable();
    }

    @FXML
    private void deleteRow(ActionEvent event) {
        Services service = services_table.getSelectionModel().getSelectedItem();
        if (service != null){
            Optional<ButtonType> option = systemHelper.showConfirmMessage("Удалить запись", "Вы действительно хотите удалить запись?", null).showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    System.out.println(service.getSeviceId());
                    servicesManager.deleteById(service.getSeviceId());
                    services_table.getItems().removeAll(services_table.getSelectionModel().getSelectedItem());
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
        systemHelper.initMenu(name, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void initButtons(){
        add_button.setOnAction(event -> {
            addService();
        });

//        systemHelper.initMenu(userName, out_button, shops_button, masters_button, model_button, cars_button, client_button,
//                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }

    private void setTable() throws SQLException {
        TableColumn<Services, String> typeColumn = new TableColumn<>("Тип ремонта");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell .forTableColumn());

        TableColumn<Services, Double> priceColumn = new TableColumn<>("Стоимость");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell .forTableColumn(systemHelper.getDoubleConverter("Неверно введена стоимость услуги!")));

        services_table.setItems(servicesManager.getAll());
        services_table.getColumns().addAll(typeColumn, priceColumn);
        services_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        services_table.setEditable(true);

        typeColumn.setOnEditCommit((TableColumn.CellEditEvent<Services, String> event) -> {
            TablePosition<Services, String> pos = event.getTablePosition();
            Services services = event.getTableView().getItems().get(pos.getRow());
            services.setType(event.getNewValue());
            changeCheck(services, event.getNewValue());
        });

        try{
            priceColumn.setOnEditCommit((TableColumn.CellEditEvent<Services, Double> event) ->{
                TablePosition<Services, Double> pos = event.getTablePosition();
                Services services = event.getTableView().getItems().get(pos.getRow());
                services.setPrice(event.getNewValue());
                changeCheck(services, event.getNewValue().toString());
            });
        } catch (NumberFormatException e){
            systemHelper.showErrorMessage("Ошибка", "Неверно введена стоимость!");
        }
    }

    private void changeCheck(Services services, String message){
        Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", message).showAndWait();
        if (option.get() == ButtonType.OK) {
            try {
                System.out.println(servicesManager.update(services));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Item change!");
        } else{
            try {
                services_table.setItems(servicesManager.getAll());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void addService(){
        if(!type_enter.getText().isEmpty() && !price_enter.getText().isEmpty()){
            if (systemHelper.priceCheck(price_enter.getText())){
                Optional<ButtonType> option = systemHelper.showConfirmMessage("Добавить запись", "Вы действительно хотите добавить запись?", "Услуга").showAndWait();
                if (option.get() == ButtonType.OK) {
                    try {
                        servicesManager.add(new Services(type_enter.getText(), Double.valueOf(price_enter.getText())));
                        services_table.setItems(servicesManager.getAll());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    System.out.println("Add new!");
                }
            } else {
                systemHelper.showErrorMessage("Ошибка", "Неверно введена стоимость!");
            }
        } else {
            systemHelper.showErrorMessage("Ошибка", "Введите данные!");
        }
    }
}
