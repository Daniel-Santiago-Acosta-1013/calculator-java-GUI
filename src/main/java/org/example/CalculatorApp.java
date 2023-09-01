
package org.example;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import org.mariuszgromada.math.mxparser.Expression;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CalculatorApp {
    private TextField display = new TextField();
    private List<String> historyList = new ArrayList<>();
    private ListView<String> historyView = new ListView<>();
    private ComboBox<String> fromUnitComboBox = new ComboBox<>();
    private ComboBox<String> toUnitComboBox = new ComboBox<>();
    private TextField fromValueTextField = new TextField();
    private TextField toValueTextField = new TextField();

    public TabPane createMainPane() {
        TabPane tabPane = new TabPane();

        // Tabs
        Tab calculatorTab = new Tab("Calculator", createCalculatorPane());
        Tab historyTab = new Tab("History", createHistoryPane());
        Tab unitConversionTab = new Tab("Unit Conversion", createUnitConversionPane());

        tabPane.getTabs().addAll(calculatorTab, historyTab, unitConversionTab);

        return tabPane;
    }

    private VBox createCalculatorPane() {
        VBox vBox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {".", "0", "(", "+"}
        };

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLUE);
        shadow.setRadius(5.0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button btn = new Button(buttons[i][j]);
                btn.setPrefSize(60, 60);
                btn.setStyle("-fx-background-color: #f0f0f0; -fx-font-size: 18px;");
                btn.setOnMousePressed(e -> btn.setEffect(shadow));
                btn.setOnMouseReleased(e -> btn.setEffect(null));
                btn.setOnAction(e -> buttonPressed(btn.getText()));
                grid.add(btn, j, i);
            }
        }

        // Equals, Backspace and Clear button layout adjustments
        HBox controlButtons = new HBox(10);
        controlButtons.setAlignment(Pos.CENTER);

        // Equals button
        Button equalsBtn = new Button("=");
        equalsBtn.setPrefSize(60, 60);
        equalsBtn.setStyle("-fx-background-color: #4CAF50; -fx-font-size: 18px; -fx-text-fill: white;");
        equalsBtn.setOnMousePressed(e -> equalsBtn.setEffect(shadow));
        equalsBtn.setOnMouseReleased(e -> equalsBtn.setEffect(null));
        equalsBtn.setOnAction(e -> evaluateExpression());

        // Backspace button
        Button backspaceBtn = new Button("Borrar");
        backspaceBtn.setPrefSize(90, 60);
        backspaceBtn.setStyle("-fx-font-size: 18px;");
        backspaceBtn.setOnAction(e -> {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        });

        // Clear button
        Button clearBtn = new Button("C");
        clearBtn.setPrefSize(60, 60);
        clearBtn.setStyle("-fx-background-color: #f44336; -fx-font-size: 18px; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> display.clear());

        controlButtons.getChildren().addAll(backspaceBtn, clearBtn, equalsBtn);
        display.setPrefWidth(300);
        display.setStyle("-fx-font-size: 20px;");

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(display, grid, controlButtons);
        return vBox;
    }

    // !hasta aqui modifico el codigo ----------------------------------------------------------

    private ListView<String> createHistoryPane() {
        return historyView;
    }

    private VBox createUnitConversionPane() {
        VBox vBox = new VBox(10);

        ObservableList<String> units = FXCollections.observableArrayList("Metros", "Kilómetros", "Centímetros", "Milímetros");
        fromUnitComboBox.setItems(units);
        toUnitComboBox.setItems(units);

        Button convertButton = new Button("Convertir");
        convertButton.setOnAction(e -> convertUnits());

        vBox.getChildren().addAll(
                new Label("Convertir de:"), fromValueTextField, fromUnitComboBox,
                new Label("a:"), toValueTextField, toUnitComboBox,
                convertButton
        );
        return vBox;
    }

    private void convertUnits() {
        String fromUnit = fromUnitComboBox.getValue();
        String toUnit = toUnitComboBox.getValue();

        double fromValue;
        try {
            fromValue = Double.parseDouble(fromValueTextField.getText());
        } catch (NumberFormatException e) {
            toValueTextField.setText("Valor inválido");
            return;
        }

        double conversionRate = getConversionRate(fromUnit, toUnit);
        double toValue = fromValue * conversionRate;

        toValueTextField.setText(Double.toString(toValue));
    }

    private double getConversionRate(String from, String to) {
        if (from.equals(to)) return 1;

        switch (from) {
            case "Metros":
                switch (to) {
                    case "Kilómetros": return 0.001;
                    case "Centímetros": return 100;
                    case "Milímetros": return 1000;
                }
                break;
            case "Kilómetros":
                switch (to) {
                    case "Metros": return 1000;
                    case "Centímetros": return 100000;
                    case "Milímetros": return 1000000;
                }
                break;
            case "Centímetros":
                switch (to) {
                    case "Metros": return 0.01;
                    case "Kilómetros": return 0.00001;
                    case "Milímetros": return 10;
                }
                break;
            case "Milímetros":
                switch (to) {
                    case "Metros": return 0.001;
                    case "Kilómetros": return 0.000001;
                    case "Centímetros": return 0.1;
                }
                break;
        }
        throw new UnsupportedOperationException("Conversión no soportada para " + from + " a " + to);
    }

    private void buttonPressed(String text) {
        display.appendText(text);
    }

    private void evaluateExpression() {
        String expression = display.getText();

        // Check for division by zero
        if (expression.contains("/0")) {
            display.setText("División por cero");
            return;
        }

        Expression e = new Expression(expression);
        if (e.checkSyntax()) {
            double result = e.calculate();
            display.setText(Double.toString(result));
            historyList.add(expression + " = " + result);
            updateHistoryView();
        } else {
            display.setText("Expresión inválida");
        }
    }

    private void updateHistoryView() {
        historyView.getItems().clear();
        historyView.getItems().addAll(historyList);
    }
}