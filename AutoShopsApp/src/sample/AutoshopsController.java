package sample;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class AutoshopsController
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<AutoShops> auto_table= new TableView<>();

    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private final ObservableList<AutoShops> autoShopsList = FXCollections.observableArrayList();
    private SystemHelper helper = new SystemHelper();

    @FXML
    void initialize() throws SQLException {
//        JFXTreeTableColumn<AutoShops, String> addressColumn = new JFXTreeTableColumn("Адрес");
//        addressColumn.setMinWidth(700);
//        addressColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<AutoShops, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<AutoShops, String> param) {
//                return param.getValue().getValue().getAddress();
//            }
//        });
//
//        JFXTreeTableColumn<AutoShops, String> nameColumn = new JFXTreeTableColumn("Название");
//        nameColumn.setMinWidth(700);
//        nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<AutoShops, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<AutoShops, String> param) {
//                return param.getValue().getValue().getName();
//            }
//        });
//        System.out.println(autoShopsManager.getAll());
//
//        TreeItem<AutoShops> root = new RecursiveTreeItem<AutoShops>(FXCollections.observableArrayList(autoShopsManager.getAll()), RecursiveTreeObject::getChildren);
//        auto_table.getColumns().setAll(addressColumn, nameColumn);
//        auto_table.setRoot(root);
//        auto_table.setShowRoot(false);

        TableColumn<AutoShops, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setMinWidth(350);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<AutoShops, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setMinWidth(350);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        auto_table.setItems(autoShopsManager.getAll());
        auto_table.getColumns().addAll(addressColumn, nameColumn);
        auto_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        auto_table.setEditable(true);

        addressColumn.setCellFactory(TextFieldTableCell. forTableColumn());
        addressColumn.setOnEditCommit((TableColumn.CellEditEvent<AutoShops, String> event) -> {
            TablePosition<AutoShops, String> pos = event.getTablePosition();
            AutoShops autoShop = event.getTableView().getItems().get(pos.getRow());
            autoShop.setAddress(event.getNewValue());

            Optional<ButtonType> option = helper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", "Адресс").showAndWait();
            if (option.get() == ButtonType.OK) {
                try {
                    System.out.println(autoShopsManager.update(autoShop));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("Item change!");
            } else{
                try {
                    auto_table.setItems(autoShopsManager.getAll());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }



}
