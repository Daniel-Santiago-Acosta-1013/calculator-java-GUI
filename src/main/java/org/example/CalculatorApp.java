package org.example;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;

public class CalculatorApp {
    private TextField display = new TextField();
    private List<String> historyList = new ArrayList<>();
    private ListView<String> historyView = new ListView<>();

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
        // You can expand this section with unit conversion options.
        VBox vBox = new VBox(10);
        vBox.getChildren().add(new Label("Unit Conversion functionality goes here"));
        return vBox;
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