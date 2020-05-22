package com.sample;

import com.sample.controller.ChoosePageController;
import com.sample.controller.MainController;
import com.sample.handler.DataBaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppFX extends Application {

    static public Integer num;

    private Stage primaryStage;
    private AnchorPane rootLayout;

    private DataBaseHandler dbHandler;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showPrepWindow();
    }

    private void showPrepWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/choosePage.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Выберите начальное кол-во товаров");
            ChoosePageController controller = loader.getController();
            controller.setActions(this::setNum, this::showMainWindow);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainWindow() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainPage.fxml"));
            dbHandler = new DataBaseHandler();
            rootLayout = loader.load();
            MainController controller = loader.getController();
            controller.setDataBaseHandler(dbHandler);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Управление товарами");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNum(Integer num) {
        AppFX.num = num;
    }

    @Override
    public void stop() throws Exception {
        if (dbHandler != null) {
            dbHandler.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
