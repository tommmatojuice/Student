package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Cars
{
    private SimpleStringProperty stateNumber;
    private Date yearOfIssue;
    private SimpleIntegerProperty dateSheetNumber;
    private SimpleIntegerProperty modelId;
    private SimpleIntegerProperty customerId;

    public Cars(String stateNumber, Date yearOfIssue, int dateSheetNumber, int modelId, int customerId) {
        this.stateNumber = new SimpleStringProperty(stateNumber);
        this.yearOfIssue = yearOfIssue;
        this.dateSheetNumber = new SimpleIntegerProperty(dateSheetNumber);
        this.modelId = new SimpleIntegerProperty(modelId);
        this.customerId = new SimpleIntegerProperty(customerId);
    }

    public String getStateNumber() {
        return stateNumber.get();
    }

    public SimpleStringProperty stateNumberProperty() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber.set(stateNumber);
    }

    public Date getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(Date yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public int getDateSheetNumber() {
        return dateSheetNumber.get();
    }

    public SimpleIntegerProperty dateSheetNumberProperty() {
        return dateSheetNumber;
    }

    public void setDateSheetNumber(int dateSheetNumber) {
        this.dateSheetNumber.set(dateSheetNumber);
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

    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    @Override
    public String toString() {
        return stateNumber.get();
    }
}
