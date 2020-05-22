package com.sample.controller;

import com.sample.AppFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static javafx.scene.control.Alert.AlertType.WARNING;

public class ChoosePageController implements Initializable {

    private Alert alert;
    @FXML
    private TextField amount;
    private Consumer<Integer> setter;
    private Runnable callback;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alert = new Alert(WARNING, "", ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Причина: ");
        alert.setContentText("Вы ввели некорректное число");
    }

    @FXML
    private void setAmount() {
        Integer num = -1;
        try {
            num = Integer.parseInt(amount.getCharacters().toString());
        } catch (NumberFormatException e) {
            alert.show();
        }
        if (num >= 0) {
            setter.accept(num);
            callback.run();
        } else {
            alert.show();
        }


    }

    public void setActions(Consumer<Integer> setter, Runnable callback) {
        this.setter = setter;
        this.callback = callback;
    }
}
