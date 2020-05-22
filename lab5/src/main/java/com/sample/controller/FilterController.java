package com.sample.controller;

import com.sample.model.Good;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static javafx.scene.control.Alert.AlertType.WARNING;

public class FilterController implements Initializable {


    @FXML
    private TextField min;
    @FXML
    private TextField max;
    private Alert alert;
    private Consumer<Good> action;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(WARNING, "", ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Причина: ");
        alert.setContentText("Вы ввели некорректное число");
    }

    @FXML
    private void filter() {
        String min = this.min.getText();
        String max = this.max.getText();


        int numMin = -1;
        int numMax = -1;
        try {
            numMin = Integer.parseInt(min);
            numMax = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            alert.show();
        }
        if (numMin > 0 && numMax > 0 && numMin < numMax) {
            action.accept(new Good(String.valueOf(numMin), numMax));
        } else {
            alert.show();
        }
    }

    public void setAction(Consumer<Good> action) {
        this.action = action;
    }
}
