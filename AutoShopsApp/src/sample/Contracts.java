package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.CDATASection;

import java.sql.Date;

public class Contracts
{
    private SimpleIntegerProperty id;
    private Date dateOpen;
    private Date dateClose;
    private StatusEnum statusEnum;
    private SimpleStringProperty stateNumber;

    public Contracts(int id, Date dateOpen, Date dateClose, StatusEnum statusEnum, String stateNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.statusEnum = statusEnum;
        this.stateNumber = new SimpleStringProperty(stateNumber);
    }

    public Contracts(Date dateOpen, Date dateClose, StatusEnum statusEnum, String stateNumber) {
        this(-1, dateOpen, dateClose, statusEnum, stateNumber);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Date getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Date dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
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

    @Override
    public String toString() {
        return "Contracts{" +
                "id=" + id +
                ", dateOpen=" + dateOpen +
                ", dateClose=" + dateClose +
                ", statusEnum=" + statusEnum +
                ", stateNumber=" + stateNumber +
                '}';
    }
}
