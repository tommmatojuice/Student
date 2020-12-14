package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;

public class Users
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleIntegerProperty master;

    private MastersManager mastersManager = new MastersManager();

    public Users(int id, String login, String password, int master) {
        this.id = new SimpleIntegerProperty(id);
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.master = new SimpleIntegerProperty(master);
    }

    public Users(String login, String password, int master) {
        this(-1, login, password, master);
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

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getMaster() {
        return master.get();
    }

    public SimpleIntegerProperty masterProperty() {
        return master;
    }

    public void setMaster(int master) {
        this.master.set(master);
    }

    @Override
    public String toString() {
        try {
            return "Мастер: " + mastersManager.getById(master.get()).toString() + "\n" +
                    "Логин: " + login.get()  + "\n" +
                    "Пароль: " + password.get();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
