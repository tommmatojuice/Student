package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.sql.*;

public class Main extends Application {

//    private final MysqlDatabase mysqlDatabase = new MysqlDatabase("jdbc:postgresql://localhost:5432/auto_shops", "postgres", "1234");
    private final MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");

    @Override
    public void start(Stage primaryStage) throws Exception{
        initDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initDatabase()
    {
        try(Connection c = mysqlDatabase.getConnection()) {
            System.out.println("Успешное подключение к базе!");
//        try(Connection conn = DriverManager.getConnection(
//                "jdbc:postgresql://localhost:5432/auto_shops", "postgres", "1234")){
        } catch (SQLException e) {
            System.out.println("Соедиение с базой установить не удалось");
            e.printStackTrace();
            System.exit(111);
        }
//        Controller controller = new Controller(mysqlDatabase);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
