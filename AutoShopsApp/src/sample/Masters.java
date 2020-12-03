package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Masters
{
    private SimpleIntegerProperty masterId;
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleIntegerProperty autoShopId;

    public Masters(int masterId, String name, String phone, int autoShopId){
        this.masterId = new SimpleIntegerProperty(masterId);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.autoShopId = new SimpleIntegerProperty(autoShopId);
    }

    public Masters(String name, String phone, int autoShopId) {
        this(-1, name, phone, autoShopId);
    }

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

    public int getAutoShopId() {
        return autoShopId.get();
    }

    public SimpleIntegerProperty autoShopIdProperty() {
        return autoShopId;
    }

    public void setAutoShopId(int autoShopId) {
        this.autoShopId.set(autoShopId);
    }

    @Override
    public String toString() {
        return "Masters{" +
                ", masterId=" + masterId +
                ", name=" + name +
                ", phone=" + phone +
                ", autoShopId=" + autoShopId +
                '}';
    }
}
