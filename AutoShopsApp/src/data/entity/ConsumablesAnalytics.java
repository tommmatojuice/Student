package data.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import data.enumes.TypesEnum;
import data.enumes.UnitsEnum;

public class ConsumablesAnalytics
{
    private SimpleStringProperty name;
    private TypesEnum type;
    private UnitsEnum unit;
    private SimpleDoubleProperty count;
    private SimpleDoubleProperty price;

    public ConsumablesAnalytics(String name, TypesEnum type, UnitsEnum unit,
                       double count, double price) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.unit = unit;
        this.count = new SimpleDoubleProperty(count);
        this.price = new SimpleDoubleProperty(price);
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

    public TypesEnum getType() {
        return type;
    }

    public void setType(TypesEnum type) {
        this.type = type;
    }

    public UnitsEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitsEnum unit) {
        this.unit = unit;
    }

    public double getCount() {
        return count.get();
    }

    public SimpleDoubleProperty countProperty() {
        return count;
    }

    public void setCount(double count) {
        this.count.set(count);
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
        return "ConsumablesAnalytics{" +
                "name=" + name +
                ", type=" + type +
                ", unit=" + unit +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
