package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Consumables
{
    private SimpleIntegerProperty consumableId;
    private SimpleStringProperty consumableName;
    private Types consumableType;
    private Units consumableUnit;
    private SimpleDoubleProperty consumablePrice;
    private SimpleStringProperty consumableProducer;

    public Consumables(int consumableId, String consumableName, Types consumableType, Units consumableUnit,
                              double consumablePrice, String consumableProducer) {
        this.consumableId = new SimpleIntegerProperty(consumableId);
        this.consumableName = new SimpleStringProperty(consumableName);
        this.consumableType = consumableType;
        this.consumableUnit = consumableUnit;
        this.consumablePrice = new SimpleDoubleProperty(consumablePrice);
        this.consumableProducer = new SimpleStringProperty(consumableProducer);
    }

    public Consumables(String consumableName, Types consumableType, Units consumableUnit,
                              double consumablePrice, String consumableProducer) {
        this(-1, consumableName, consumableType, consumableUnit, consumablePrice, consumableProducer);
    }

    public int getConsumableId() {
        return consumableId.get();
    }

    public SimpleIntegerProperty consumableIdProperty() {
        return consumableId;
    }

    public void setConsumableId(int consumableId) {
        this.consumableId.set(consumableId);
    }

    public String getConsumableName() {
        return consumableName.get();
    }

    public SimpleStringProperty consumableNameProperty() {
        return consumableName;
    }

    public void setConsumableName(String consumableName) {
        this.consumableName.set(consumableName);
    }

    public Types getConsumableType() {
        return consumableType;
    }

    public void setConsumableType(Types consumableType) {
        this.consumableType = consumableType;
    }

    public Units getConsumableUnit() {
        return consumableUnit;
    }

    public void setConsumableUnit(Units consumableUnit) {
        this.consumableUnit = consumableUnit;
    }

    public double getConsumablePrice() {
        return consumablePrice.get();
    }

    public SimpleDoubleProperty consumablePriceProperty() {
        return consumablePrice;
    }

    public void setConsumablePrice(double consumablePrice) {
        this.consumablePrice.set(consumablePrice);
    }

    public String getConsumableProducer() {
        return consumableProducer.get();
    }

    public SimpleStringProperty consumableProducerProperty() {
        return consumableProducer;
    }

    public void setConsumableProducer(String consumableProducer) {
        this.consumableProducer.set(consumableProducer);
    }

    @Override
    public String toString() {
        return "ConsumablesManager{" +
                "consumableId=" + consumableId +
                ", consumableName=" + consumableName +
                ", consumableType=" + consumableType +
                ", consumableUnit=" + consumableUnit +
                ", consumablePrice=" + consumablePrice +
                ", consumableProducer=" + consumableProducer +
                '}';
    }
}
