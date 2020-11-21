package sample;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AutoShops extends RecursiveTreeObject<AutoShops> {
    private SimpleIntegerProperty shop_number;
    private SimpleStringProperty address;
    private SimpleStringProperty name;

    public AutoShops(int shop_number, String address, String name) {
        this.shop_number = new SimpleIntegerProperty(shop_number);
        this.address = new SimpleStringProperty(address);
        this.name = new SimpleStringProperty(name);
    }

    public AutoShops(String address, String name) {
        this(-1, address, name);
    }

    public int getShop_number() {
        return shop_number.get();
    }

    public SimpleIntegerProperty shop_numberProperty() {
        return shop_number;
    }

    public void setShop_number(int shop_number) {
        this.shop_number.set(shop_number);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
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

    @Override
    public String toString() {
        return "AutoShops{" +
                "shop_number=" + shop_number +
                ", address=" + address +
                ", name=" + name +
                '}';
    }
}
