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

import static javafx.scene.control.Alert.AlertType.INFORMATION;
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
//        data.clear();
//        new Thread(() -> {
//            data.addAll(dataBaseHandler.getActionsByCommand().get("/show_all").apply(null));
//            data.sort(Comparator.comparing(Good::getCost));
//            name.setCellValueFactory(new PropertyValueFactory<>("name"));
//            cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
//
//            table.setItems(data);
//        }).start();
        data = (dataBaseHandler.getActionsByCommand().get("/show_all").apply(null));
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

    private void showData(ObservableList<Good> data) {
        this.data = data;
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
        if (selected == null) {
            goodNotSelected();
        } else {
            new Thread(() -> {
                dataBaseHandler.getActionsByCommand()
                        .get("/change_price")
                        .apply(new Good(selected.getName(), newPrice));
                showData();
            }).start();
        }

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
        controller.setActions((goodName) ->
                dataBaseHandler.getActionsByCommand().get("/price").apply(new Good(goodName, 2)));
        stage.show();
    }

    @FXML
    private void add() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/addingGood.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Добавление товара");
        AddingController controller = loader.getController();
        controller.setAction(this::dbAdding);
        stage.show();
    }

    private void dbAdding(Good good) {
        if (dataBaseHandler.getActionsByCommand().get("/add").apply(good) == null) {
            Alert alert = new Alert(WARNING, "", ButtonType.OK);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Причина: ");
            alert.setContentText("Такой товар уже существует");
            alert.show();
        } else {
            showData();
            Alert alert = new Alert(INFORMATION, "", ButtonType.OK);
            alert.setTitle("Результат");
            alert.setHeaderText("Был создан товар: ");
            alert.setContentText("Название - " + good.getName() + " Цена - " + good.getCost());
            alert.show();
        }
    }

    @FXML
    private void filter() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/filter.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Фильтрация Товаров");
        FilterController controller = loader.getController();
        controller.setAction(this::dbFilter);
        stage.show();
    }

    private void dbFilter(Good good) {
        new Thread(() -> {
            showData(dataBaseHandler.getActionsByCommand()
                    .get("/filter_by_price")
                    .apply(good));
        }).start();
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