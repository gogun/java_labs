package com.sample.controller;

import com.sample.handler.DataBaseHandler;
import com.sample.model.Good;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.control.Alert.AlertType.WARNING;

public class MainController implements Initializable {

    private Good selected = null;

    private Integer newPrice;

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
        table.setPlaceholder(new Label("Нет товаров"));
        Platform.runLater(this::showData);
    }

    private void showData() {
        data = dataBaseHandler.getActionsByCommand().get("/show_all").apply(null);
        data.sort(Comparator.comparing(Good::getCost));
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
    private void changePrice() throws IOException {
        if (selected == null) {
            goodNotSelected();
        } else {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/choosePage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Выберите новую цену для " + selected.getName());
            ChoosePageController controller = loader.getController();
            controller.setActions(this::setNewPrice, this::changePriceQuery);
            stage.show();
        }
    }

    private void changePriceQuery() {
        new Thread(() -> {
            dataBaseHandler.getActionsByCommand()
                    .get("/change_price")
                    .apply(new Good(selected.getName(), newPrice));
            showData();
            selected = null;
        }).start();

    }

    @FXML
    private void deleteGood() {
        if (selected == null) {
            goodNotSelected();
        } else {
            new Thread(() -> {
                dataBaseHandler.getActionsByCommand().get("/delete").apply(selected);
                showData();
                selected = null;
            }).start();

        }
    }

    @FXML
    private void search() throws IOException {
        Alert alert = new Alert(WARNING, "", ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Причина: ");
        alert.setContentText("Такого товара нет");

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooseGoodPage.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Поиск по товарам");
        ChooseGoodPageController controller = loader.getController();
        controller.setActions((goodName) -> dataBaseHandler.getActionsByCommand().get("/price").apply(new Good(goodName, 2)));
        stage.show();
    }

    private void goodNotSelected() {
        Alert alert = new Alert(WARNING, "", ButtonType.OK);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Причина: ");
        alert.setContentText("Вы не выбрали товар");
        alert.show();
    }

    public void setDataBaseHandler(DataBaseHandler dbHandler) {
        this.dataBaseHandler = dbHandler;
    }

    public void setNewPrice(Integer newPrice) {
        this.newPrice = newPrice;
    }
}