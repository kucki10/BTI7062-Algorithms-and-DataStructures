package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = FXMLLoader.load(Main.class.getResource("view/Application.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(800);
        primaryStage.setTitle("QuickSortDnc - Performance");
        primaryStage.show();
    }
}
