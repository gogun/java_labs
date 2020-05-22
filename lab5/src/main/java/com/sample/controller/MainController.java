package com.sample.controller;

import com.sample.handler.DataBaseHandler;
import com.sample.model.Good;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {
//    private static Consumer<Logger> invalidCommandAction = (command ->
//            System.out.println("Неправильная команда"));


    private Good selected = null;

    private ObservableList<Good> data = FXCollections.observableArrayList();

    private DataBaseHandler dataBaseHandler;
    @FXML
    private TableView<Good> table;
    @FXML
    private TableColumn<Good, String> name;
    @FXML
    private TableColumn<Good, Integer> cost;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::showData);

    }

    private void showData() {
        data = dataBaseHandler.getActionsByCommand().get("/show_all").apply(null);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, good, t1) -> {
            if (t1 != null) {
                selected = t1;
            }
        });
    }

    @FXML
    private void changePrice() {
        if (selected != null) {
//            dataBaseHandler.getActionsByCommand().get("/change_price").apply(new Good());
//            showData();
        }
    }

    @FXML
    private void deleteGood() {
        if (selected != null) {

                dataBaseHandler.getActionsByCommand().get("/delete").apply(selected);
                showData();

        }
    }

    public void setDataBaseHandler(DataBaseHandler dbHandler) {
        this.dataBaseHandler = dbHandler;
    }
}