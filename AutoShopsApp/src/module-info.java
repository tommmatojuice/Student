module AutoShopsApp {
    requires javafx.fxml;
    requires javafx.controls;
    requires mysql.connector.java;
    requires java.sql;

    requires java.naming;
    requires com.jfoenix;
    requires java.datatransfer;
    requires java.desktop;
    opens sample;
    opens sample.ui;
    opens sample.asseats;
    opens data.entity;
    opens data.manager;
    opens data.enumes;
}