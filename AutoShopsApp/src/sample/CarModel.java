package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CarModel
{
    private SimpleIntegerProperty modelId;
    private SimpleStringProperty name;
    private SimpleDoubleProperty engineCapacity;
    private SimpleDoubleProperty enginePower;

    public CarModel(int modelId, String name, double engineCapacity, double enginePower) {
        this.modelId = new SimpleIntegerProperty(modelId);
        this.name = new SimpleStringProperty(name);
        this.engineCapacity = new SimpleDoubleProperty(engineCapacity);
        this.enginePower = new SimpleDoubleProperty(enginePower);
    }

    public CarModel(String name, double engineCapacity, double enginePower) {
        this(-1, name, engineCapacity, enginePower);
    }

    public int getModelId() {
        return modelId.get();
    }

    public SimpleIntegerProperty modelIdProperty() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId.set(modelId);
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

    public double getEngineCapacity() {
        return engineCapacity.get();
    }

    public SimpleDoubleProperty engineCapacityProperty() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity.set(engineCapacity);
    }

    public double getEnginePower() {
        return enginePower.get();
    }

    public SimpleDoubleProperty enginePowerProperty() {
        return enginePower;
    }

    public void setEnginePower(double enginePower) {
        this.enginePower.set(enginePower);
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "modelId=" + modelId +
                ", name=" + name +
                ", engineCapacity=" + engineCapacity +
                ", enginePower=" + enginePower +
                '}';
    }
}
