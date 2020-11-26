package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static javafx.scene.input.KeyCode.T;

public class Masters
{
    private final AutoShopsManager autoShopsManager = new AutoShopsManager();
    private final MastersManager mastersManager = new MastersManager();
    private SystemHelper systemHelper = new SystemHelper();

    private SimpleIntegerProperty masterId;
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private AutoShops autoShop;
    private ComboBox<AutoShops> autoShops;

    public Masters(int masterId, String name, String phone, int autoShop) throws SQLException {
        this.masterId = new SimpleIntegerProperty(masterId);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.autoShops = new ComboBox<>(autoShopsManager.getAll());

        for(AutoShops a: autoShops.getItems()){
            if (a.getShop_number() == autoShop)
                this.autoShop = a;
        }
        autoShops.setValue(this.autoShop);
        autoShops.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public Masters(String name, String phone, int autoShop) throws SQLException {
        this(-1, name, phone, autoShop);
    }

    ChangeListener<AutoShops> changeListener = new ChangeListener<AutoShops>() {
        @Override
        public void changed(ObservableValue<? extends AutoShops> observable, AutoShops oldValue, AutoShops newValue) {
            if (newValue != null && newValue != autoShop) {
                Optional<ButtonType> option = systemHelper.showConfirmMessage("Изменить поле", "Вы действительно хотите изменить поле?", "Автомастерская").showAndWait();
                if (option.get() == ButtonType.OK) {
                    try {
                        autoShop = newValue;
                        mastersManager.update(new Masters(getMasterId(), getName(), getPhone(), getAutoShop().getShop_number()));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    System.out.println("Item change!");
                } else {
                    autoShops.setValue(autoShop);
                }
            }
        }
    };

    public int getMasterId() {
        return masterId.get();
    }

    public SimpleIntegerProperty masterIdProperty() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId.set(masterId);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public AutoShops getAutoShop() {
        return autoShop;
    }

    public void setAutoShop(AutoShops autoShop) {
        this.autoShop = autoShop;
    }

    public ComboBox<AutoShops> getAutoShops() {
        return autoShops;
    }

    public void setAutoShops(ComboBox<AutoShops> autoShops) {
        this.autoShops = autoShops;
    }

    @Override
    public String toString() {
        return "Masters{" +
                ", masterId=" + masterId +
                ", name=" + name +
                ", phone=" + phone +
                ", autoShop=" + autoShop +
                ", autoShops=" + autoShops +
                '}';
    }
}
