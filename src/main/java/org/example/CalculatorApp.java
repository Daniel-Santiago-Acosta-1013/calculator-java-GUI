package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorApp {

    private TextField display = new TextField();

    public GridPane createCalculatorPane() {
        GridPane grid = new GridPane();

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", "(", ")", "+"}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button btn = new Button(buttons[i][j]);
                btn.setPrefSize(50, 50);
                btn.setOnAction(e -> buttonPressed(btn.getText()));
                grid.add(btn, j, i + 1);
            }
        }

        // Agregar botón de igual
        Button equalsBtn = new Button("=");
        equalsBtn.setPrefSize(50, 50);
        equalsBtn.setOnAction(e -> evaluateExpression());
        grid.add(equalsBtn, 3, 5);

        // Agregar botón para borrar último carácter
        Button backspaceBtn = new Button("Borrar");
        backspaceBtn.setPrefSize(100, 50);
        backspaceBtn.setOnAction(e -> {
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        grid.add(backspaceBtn, 1, 5, 2, 1); // Span 2 columns

        // Agregar botón para limpiar todo
        Button clearBtn = new Button("C");
        clearBtn.setPrefSize(50, 50);
        clearBtn.setOnAction(e -> display.clear());
        grid.add(clearBtn, 0, 5);

        display.setPrefWidth(200);
        grid.add(display, 0, 0, 4, 1);

        return grid;
    }

    private void buttonPressed(String text) {
        display.appendText(text);
    }

    private void evaluateExpression() {
        String expression = display.getText();
        org.mariuszgromada.math.mxparser.Expression e = new org.mariuszgromada.math.mxparser.Expression(expression);

        if (e.checkSyntax()) {
            double result = e.calculate();
            display.setText(Double.toString(result));
        } else {
            display.setText("Error");
        }
    }
}