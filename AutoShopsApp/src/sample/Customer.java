package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SingleSelectionModel;

public class Customer extends SingleSelectionModel<Customer> {
    private SimpleIntegerProperty id;
    private SimpleStringProperty fullName;
    private SimpleStringProperty address;
    private SimpleStringProperty phone;
    private SimpleStringProperty passport;

    public Customer(int id, String fullName, String address, String phone, String passport) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.passport = new SimpleStringProperty(passport);
    }

    public Customer(String fullName, String address, String phone, String  passport) {
        this(-1, fullName, address, phone, passport);
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

    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPassport() {
        return passport.get();
    }

    public SimpleStringProperty passportProperty() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport.set(passport);
    }

    @Override
    public String toString() {
        return fullName.get() + " (" + passport.get() + ")";
    }

    @Override
    protected Customer getModelItem(int index) {
        return null;
    }

    @Override
    protected int getItemCount() {
        return 0;
    }
}
