package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.MysqlDatabase;

import java.awt.*;
import java.io.IOException;
import java.sql.*;

public class Main extends Application
{
    private final MysqlDatabase mysqlDatabase = new MysqlDatabase("127.0.0.1", "auto_shops", "root", "");
    protected Scene activeScene, nextScene;
    private static Stage myStage;
    private static Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private double height = dimension.getHeight();
    private double width = dimension.getWidth();

    @Override
    public void start(Stage primaryStage) throws Exception{
        initDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("ui/sample.fxml"));
        activeScene = new Scene(root, 800, 600);
        primaryStage.setScene(activeScene);
        myStage = primaryStage;
        myStage.setTitle("Conner Brothers Auto Shops");
        myStage.show();
        myStage.setResizable(false);
        primaryStage.getIcons().add(new Image("sample/logo.png"));
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

//    protected FXMLLoader goToShops() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("autoshops.fxml"));
//        Parent root = loader.load();
//        this.showScene(root);
//        return loader;
//    }
//
//    protected FXMLLoader goToMasters() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("masters.fxml"));
//        Parent root = loader.load();
//        this.showScene(root);
//        return loader;
//    }

    public FXMLLoader showScene(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        if(!path.equals("sample.fxml")){
            myStage.setResizable(true);
            if (myStage.getWidth()<1000){
                activeScene = new Scene(root, 870, 650);
            } else {
                activeScene = new Scene(root, 1680, 890);
            }
        } else {
            myStage.setResizable(false);
            activeScene = new Scene(root);
        }
        myStage.setScene(activeScene);
        myStage.centerOnScreen();
        myStage.show();
        return loader;
    }

//    private void showScene(Parent root){
//
//        myStage.setResizable(true);
//        if (myStage.getWidth()<1000){
//            activeScene = new Scene(root, 870, 650);
//        } else {
//            activeScene = new Scene(root, 1700, 920);
//        }
//        myStage.setScene(activeScene);
//        myStage.show();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
