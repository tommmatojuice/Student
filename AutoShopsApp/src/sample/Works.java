package sample;

import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.sql.SQLException;

public class Works
{
    private ConsumablesManager consumablesManager = new ConsumablesManager();

    private SimpleIntegerProperty workId;
    private SimpleIntegerProperty serviceId;
    private SimpleIntegerProperty contractNumber;
    private SimpleIntegerProperty masterId;
    private Date receiptDate;
    private Date completionDate;
    private Date actualCompletionDate;
    private String consumables = "";

    public Works(int workId, int serviceId, int contractNumber,
                 int masterId, Date receiptDate, Date completionDate, Date actualCompletionDate) {
        this.workId = new SimpleIntegerProperty(workId);
        this.serviceId = new SimpleIntegerProperty(serviceId);
        this.contractNumber = new SimpleIntegerProperty(contractNumber);
        this.masterId = new SimpleIntegerProperty(masterId);
        this.receiptDate = receiptDate;
        this.completionDate = completionDate;
        this.actualCompletionDate = actualCompletionDate;

        try {
            for(String consumableName: consumablesManager.getByWorkId(getWorkId())){
                this.consumables+=consumableName+", ";
            }
            if(!this.consumables.isEmpty())
                this.consumables = this.consumables.substring(0, this.consumables.length()-2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Works(int shopId, int contractNumber,
                 int masterId, Date receiptDate, Date completionDate, Date actualCompletionDate) {
        this(-1, shopId, contractNumber, masterId, receiptDate, completionDate, actualCompletionDate);
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

    public int getServiceId() {
        return serviceId.get();
    }

    public SimpleIntegerProperty serviceIdProperty() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId.set(serviceId);
    }

    public int getContractNumber() {
        return contractNumber.get();
    }

    public SimpleIntegerProperty contractNumberProperty() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber.set(contractNumber);
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Date getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(Date actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public String getConsumables() {
        return consumables;
    }

    @Override
    public String toString() {
        return "Works{" +
                "workId=" + workId +
                ", shopId=" + serviceId +
                ", contractNumber=" + contractNumber +
                ", masterId=" + masterId +
                ", receiptDate=" + receiptDate +
                ", completionDate=" + completionDate +
                ", fCompletionDate=" + actualCompletionDate +
                '}';
    }
}
