package data.entity;

import data.manager.ConsumablesManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import data.enumes.TypesEnum;

import java.sql.SQLException;

public class ConsumablesUse
{
    ConsumablesManager consumablesManager = new ConsumablesManager();

    private SimpleIntegerProperty consumableUseId;
    private SimpleIntegerProperty workId;
    private SimpleIntegerProperty consumableId;
    private SimpleDoubleProperty number;
    private TypesEnum type;

    public ConsumablesUse(int consumableUseId, int workId, int consumableId, double number) throws SQLException {
        this.consumableUseId = new SimpleIntegerProperty(consumableUseId);
        this.workId = new SimpleIntegerProperty(workId);
        this.consumableId = new SimpleIntegerProperty(consumableId);
        this.number = new SimpleDoubleProperty(number);
        this.type = TypesEnum.valueOf(consumablesManager.getById(this.getConsumableId()).getConsumableType().name());
    }

    public ConsumablesUse(int workId, int consumableId, double number) throws SQLException {
        this(-1, workId, consumableId, number);
    }

    public int getConsumableUseId() {
        return consumableUseId.get();
    }

    public SimpleIntegerProperty consumableUseIdProperty() {
        return consumableUseId;
    }

    public void setConsumableUseId(int consumableUseId) {
        this.consumableUseId.set(consumableUseId);
    }

    public int getWorkId() {
        return workId.get();
    }

    public SimpleIntegerProperty workIdProperty() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId.set(workId);
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

    public double getNumber() {
        return number.get();
    }

    public SimpleDoubleProperty numberProperty() {
        return number;
    }

    public void setNumber(double number) {
        this.number.set(number);
    }

    public TypesEnum getType() {
        return type;
    }

    public void setType(TypesEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConsumablesUse{" +
                ", consumableUseId=" + consumableUseId +
                ", workId=" + workId +
                ", consumableId=" + consumableId +
                ", number=" + number +
                '}';
    }
}
