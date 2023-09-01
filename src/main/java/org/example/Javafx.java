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

        // Ajusta el tamaño de la Scene al contenido
        Scene scene = new Scene(calculatorPane);

        stage.setScene(scene);
        stage.setTitle("JavaFX Calculator");
        stage.sizeToScene();  // Ajusta el tamaño del Stage al tamaño de la Scene
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}