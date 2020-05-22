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

public class AddingController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField cost;
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
    private void setGood() {
        String name = this.name.getText();
        String cost = this.cost.getText();


        int numCost = -1;
        try {
            numCost = Integer.parseInt(cost);
        } catch (NumberFormatException e) {
            alert.show();
        }
        if (numCost > 0) {
            action.accept(new Good(name, numCost));
//                Stage stage = (Stage) this.name.getScene().getWindow();
//                stage.close();
        } else {
            alert.show();
        }

    }

    public void setAction(Consumer<Good> action) {
        this.action = action;
    }
}
