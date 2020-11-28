package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.sql.*;

public class Main extends Application
{
    private final MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");

    @Override
    public void start(Stage primaryStage) throws Exception{
        initDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Conner Brothers Auto Shops");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
//        primaryStage.getIcons().add(new Image("asseats/logo.png"));
        primaryStage.setResizable(false);
    }

    private void initDatabase()
    {
        try(Connection c = mysqlDatabase.getConnection()) {
            System.out.println("Успешное подключение к базе!");
        } catch (SQLException e) {
            System.out.println("Соедиение с базой установить не удалось");
            e.printStackTrace();
            System.exit(111);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
