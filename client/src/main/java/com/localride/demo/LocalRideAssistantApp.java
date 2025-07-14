package com.localride.demo;

import com.localride.util.LanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.util.ResourceBundle;

public class LocalRideAssistantApp extends Application {
    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        ResourceBundle bundle = LanguageManager.getBundle();
        // Load the FXML file and set up the scene
        //javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(LocalRideAssistantApp.class.getResource("Dashboard.fxml"));
        //javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
        //stage.setTitle("Local Ride Assistant");
        //stage.setScene(scene);
        //stage.show();

            //FXMLLoader fxmlLoader = new FXMLLoader(LocalRideAssistantApp.class.getResource("Dashboard.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        if (fxmlLoader.getLocation() == null) {
            System.out.println("Error: dashboard.fxml not found in /fxml/");
            return;
        }
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
