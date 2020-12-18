package data.entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Services
{
    private SimpleIntegerProperty seviceId;
    private SimpleStringProperty type;
    private SimpleDoubleProperty price;

    public Services(int seviceId, String type, Double price) {
        this.seviceId = new SimpleIntegerProperty(seviceId);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price);
    }

    public Services(String type, Double price) {
        this(-1, type, price);
    }

    public int getSeviceId() {
        return seviceId.get();
    }

    public SimpleIntegerProperty seviceIdProperty() {
        return seviceId;
    }

    public void setSeviceId(int seviceId) {
        this.seviceId.set(seviceId);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    @Override
    public String toString() {
        return type.get();
    }
}
