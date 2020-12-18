package data.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CarModel
{
    private SimpleIntegerProperty modelId;
    private SimpleStringProperty brandName;
    private SimpleStringProperty modelName;
    private SimpleStringProperty prodCountry;

    public CarModel(int modelId, String brandName, String modelName, String prodCountry) {
        this.modelId = new SimpleIntegerProperty(modelId);
        this.brandName = new SimpleStringProperty(brandName);
        this.modelName = new SimpleStringProperty(modelName);
        this.prodCountry = new SimpleStringProperty(prodCountry);
    }

    public CarModel(String brandName, String modelName, String prodCountry) {
        this(-1, brandName, modelName, prodCountry);
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

    public String getBrandName() {
        return brandName.get();
    }

    public SimpleStringProperty brandNameProperty() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName.set(brandName);
    }

    public String getModelName() {
        return modelName.get();
    }

    public SimpleStringProperty modelNameProperty() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName.set(modelName);
    }

    public String getProdCountry() {
        return prodCountry.get();
    }

    public SimpleStringProperty prodCountryProperty() {
        return prodCountry;
    }

    public void setProdCountry(String prodCountry) {
        this.prodCountry.set(prodCountry);
    }

    @Override
    public String toString() {
        return brandName.get() + " (" + modelName.get() + ")";
    }
}
