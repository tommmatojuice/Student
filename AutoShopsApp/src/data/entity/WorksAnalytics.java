package data.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WorksAnalytics
{
    private SimpleStringProperty name;
    private SimpleIntegerProperty count;
    private SimpleDoubleProperty income;

    public WorksAnalytics(String name, int count, double income) {
        this.name = new SimpleStringProperty(name);
        this.count = new SimpleIntegerProperty(count);
        this.income = new SimpleDoubleProperty(income);
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

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public double getIncome() {
        return income.get();
    }

    public SimpleDoubleProperty incomeProperty() {
        return income;
    }

    public void setIncome(double income) {
        this.income.set(income);
    }

    @Override
    public String toString() {
        return "WorksAnalytics{" +
                "name=" + name +
                ", count=" + count +
                ", income=" + income +
                '}';
    }
}
