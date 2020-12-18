package sample.ui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class AnalyticsPageController {

    @FXML
    private JFXButton out_button;

    @FXML
    private Label user_label;

    @FXML
    private JFXButton works_button;

    @FXML
    private JFXButton consumables_button;

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
    private String userName;

    @FXML
    public void initialize(String userName) {
        this.userName = userName;
        initButtons();
        setUserName();
    }

    private void initButtons() {
        works_button.setOnAction(event -> {
            try {
                FXMLLoader loader = systemHelper.showScene("analyticsTable.fxml");
                AnalyticsTableController controller = loader.getController();
                controller.initialize(1, userName, 0);
            } catch (IOException | SQLException exception) {
                exception.printStackTrace();
            }
        });

        consumables_button.setOnAction(event -> {
            try {
                FXMLLoader loader = systemHelper.showScene("analyticsTable.fxml");
                AnalyticsTableController controller = loader.getController();
                controller.initialize(2, userName, 0);
            } catch (IOException | SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setUserName(){
        user_label.setText(userName);
        systemHelper.initMenu(userName, 0, out_button, shops_button, masters_button, model_button, cars_button, client_button,
                consum_button, work_button, cintract_button, service_button, math_button, users_button);
    }
}
