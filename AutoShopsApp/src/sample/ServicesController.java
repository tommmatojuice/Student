package sample;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.converter.DoubleStringConverter;
import org.w3c.dom.DOMImplementation;

public class ServicesController
{
    @FXML
    private TableView<Services> services_table = new TableView<>();

    @FXML
    private TextField adress_enter;

    @FXML
    private TextField name_enter;

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
    void initialize() throws SQLException {
        initButtons();
        setTable();
    }

    @FXML
    void deleteRow(ActionEvent event) {

    }

    public void setUserName(String name){
        userName = name;
        user_label.setText(name);
    }

    public void initButtons(){

    }

    public void setTable() throws SQLException
    {
        TableColumn<Services, String> typeColumn = new TableColumn<>("Тип ремонта");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell .forTableColumn());

        TableColumn<Services, Double> priceColumn = new TableColumn<>("Стоимость");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        services_table.setItems(servicesManager.getAll());
        services_table.getColumns().addAll(typeColumn, priceColumn);
        services_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        services_table.setEditable(true);

        typeColumn.setOnEditCommit((TableColumn.CellEditEvent<Services, String> event) -> {
            TablePosition<Services, String> pos = event.getTablePosition();
            Services services = event.getTableView().getItems().get(pos.getRow());
            services.setType(event.getNewValue());
            changeCheck(services, "Тип ремонта");
        });

        priceColumn.setOnEditCommit((TableColumn.CellEditEvent<Services, Double> event) ->{
            TablePosition<Services, Double> pos = event.getTablePosition();
            Services services = event.getTableView().getItems().get(pos.getRow());
            services.setPrice(Double.parseDouble(event.getNewValue().toString()));
            changeCheck(services, "Стоимость");
        });
    }

    void changeCheck(Services services, String message){
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





}
