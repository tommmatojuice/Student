package sample;

import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;

public class Works
{
    private SimpleIntegerProperty workId;
    private SimpleIntegerProperty shopId;
    private SimpleIntegerProperty contractNumber;
    private SimpleIntegerProperty masterId;
    private Date receiptDate;
    private Date completionDate;
    private Date actualCompletionDate;

    public Works(int workId, int shopId, int contractNumber,
                 int masterId, Date receiptDate, Date completionDate, Date actualCompletionDate) {
        this.workId = new SimpleIntegerProperty(workId);
        this.shopId = new SimpleIntegerProperty(shopId);
        this.contractNumber = new SimpleIntegerProperty(contractNumber);
        this.masterId = new SimpleIntegerProperty(masterId);
        this.receiptDate = receiptDate;
        this.completionDate = completionDate;
        this.actualCompletionDate = actualCompletionDate;
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

    public int getShopId() {
        return shopId.get();
    }

    public SimpleIntegerProperty shopIdProperty() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId.set(shopId);
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

    @Override
    public String toString() {
        return "Works{" +
                "workId=" + workId +
                ", shopId=" + shopId +
                ", contractNumber=" + contractNumber +
                ", masterId=" + masterId +
                ", receiptDate=" + receiptDate +
                ", completionDate=" + completionDate +
                ", fCompletionDate=" + actualCompletionDate +
                '}';
    }
}
