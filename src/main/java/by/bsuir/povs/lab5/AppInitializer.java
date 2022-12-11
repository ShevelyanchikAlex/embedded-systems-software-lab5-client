package by.bsuir.povs.lab5;

import by.bsuir.povs.lab5.controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("view.fxml"));
        fxmlLoader.setControllerFactory(c -> new AppController());
        var scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("POVS-lab5");
        primaryStage.show();
    }

}