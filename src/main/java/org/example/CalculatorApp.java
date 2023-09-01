package org.example;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import javafx.collections.FXCollections;
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

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "(", "+"}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button btn = new Button(buttons[i][j]);
                btn.setPrefSize(50, 50);
                btn.setOnAction(e -> buttonPressed(btn.getText()));
                grid.add(btn, j, i + 1);
            }
        }

        // Equals button
        Button equalsBtn = new Button("=");
        equalsBtn.setPrefSize(50, 50);
        equalsBtn.setOnAction(e -> evaluateExpression());
        grid.add(equalsBtn, 3, 5);

        // Backspace button
        Button backspaceBtn = new Button("Borrar");
        backspaceBtn.setPrefSize(100, 50);
        backspaceBtn.setOnAction(e -> {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        grid.add(backspaceBtn, 1, 5, 2, 1); // Span 2 columns

        // Clear button
        Button clearBtn = new Button("C");
        clearBtn.setPrefSize(50, 50);
        clearBtn.setOnAction(e -> display.clear());
        grid.add(clearBtn, 0, 5);

        display.setPrefWidth(200);
        grid.add(display, 0, 0, 4, 1);

        vBox.getChildren().addAll(display, grid);
        return vBox;
    }

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