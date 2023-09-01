package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

public class Javafx extends Application {

    @Override
    public void start(Stage stage) {
        CalculatorApp calculator = new CalculatorApp();
        GridPane calculatorPane = calculator.createCalculatorPane();

        Scene scene = new Scene(calculatorPane, 640, 480);
        stage.setScene(scene);
        stage.setTitle("JavaFX Calculator");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}