package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;

public class Javafx extends Application {

    @Override
    public void start(Stage stage) {
        CalculatorApp calculator = new CalculatorApp();
        TabPane mainPane = calculator.createMainPane();

        // Set scene
        Scene scene = new Scene(mainPane, 300, 400); // Adjusted dimensions
        stage.setScene(scene);
        stage.setTitle("JavaFX Calculator");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}