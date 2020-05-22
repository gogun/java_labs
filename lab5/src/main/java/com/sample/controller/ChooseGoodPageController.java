package com.sample.controller;

import com.sample.model.Good;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;

public class ChooseGoodPageController implements Initializable {

    private Alert alert;
    @FXML
    private TextField textField;
    private Function<String, ObservableList<Good>> action;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAlert();
    }

    private void setAlert() {
        alert = new Alert(WARNING, "", ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Причина: ");
        alert.setContentText("Такого товара нет");
    }

    @FXML
    public void setString() {
        String name = textField.getCharacters().toString();
        ObservableList<Good> good = action.apply(name);
        if (good == null) {
            setAlert();
            alert.show();
        } else {
            alert = new Alert(INFORMATION, "", ButtonType.OK);
            alert.setTitle("Результат");
            alert.setHeaderText("Цена товара " + name + ":");
            alert.setContentText(String.valueOf(good.get(0).getCost()));
            alert.show();
        }
    }

    public void setActions(Function<String, ObservableList<Good>> action) {
        this.action = action;
    }
}
