package com.loja.amor_de_mamae.util;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class MaskUtil {
    public static void cpfField(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            // Remove tudo que não for número
            String value = newValue.replaceAll("[^0-9]", "");

            // Limita em 11 dígitos
            if (value.length() > 11) {
                value = value.substring(0, 11);
            }

            // Aplica a máscara
            StringBuilder formatted = new StringBuilder();
            int len = value.length();
            for (int i = 0; i < len; i++) {
                if (i == 3 || i == 6) {
                    formatted.append(".");
                } else if (i == 9) {
                    formatted.append("-");
                }
                formatted.append(value.charAt(i));
            }

            // Evita loop infinito de listener
            textField.setText(formatted.toString());
            textField.positionCaret(formatted.length());
        });
    }

    public static void dateField(DatePicker datePicker) {
        datePicker.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            // Remove tudo que não for número
            String value = newValue.replaceAll("[^0-9]", "");

            // Limita em 8 dígitos (DDMMYYYY)
            if (value.length() > 8) {
                value = value.substring(0, 8);
            }

            // Aplica a máscara
            StringBuilder formatted = new StringBuilder();
            int len = value.length();
            for (int i = 0; i < len; i++) {
                if (i == 2 || i == 4) {
                    formatted.append("/");
                }
                formatted.append(value.charAt(i));
            }

            // Evita loop infinito de listener
            datePicker.getEditor().setText(formatted.toString());
            datePicker.getEditor().positionCaret(formatted.length());
        });
    }
    
}
