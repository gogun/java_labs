package com.sample.handler;

import com.sample.AppFX;
import com.sample.model.Good;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.scene.control.Alert.AlertType.WARNING;

public class DataBaseHandler {
    private Map<String, Function<Good, ObservableList<Good>>> actionsByCommand;

    private final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String username = "postgres";
    private final String pass = "123";

    private final Alert alert = new Alert(WARNING, "", ButtonType.OK);

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Connection connection;

    final static String select_all_query = "SELECT * FROM goods";
    final static String select_range_query = "SELECT title, cost FROM goods WHERE cost >= ? AND cost <= ?";
    final static String select_product_query = "SELECT title, cost FROM goods WHERE title = ?";
    final static String update_product_query = "UPDATE goods SET cost = ? WHERE title = ?";
    final static String insert_product_query = "INSERT INTO goods (title, cost) VALUES (?, ?)";
    final static String delete_product_query = "DELETE FROM goods WHERE title = ?";

    public DataBaseHandler() {
        this.actionsByCommand = new HashMap<>();
        try {
            this.connection = DriverManager.getConnection(dbUrl, username, pass);
        } catch (Exception ignored) {
        }
        try {
            initData();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "creating error", e);
        }
        this.addActions();
    }

    private void initData() throws SQLException {
        String create_query =
                "CREATE TABLE IF NOT EXISTS goods (\n" +
                        "    id serial primary key ,\n" +
                        "    prodid UUID NOT NULL DEFAULT uuid_generate_v4(),\n" +
                        "    title varchar(255) unique ,\n" +
                        "    cost int\n" +
                        ");";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(create_query);
        }
        try (PreparedStatement statement
                     = connection.prepareStatement(DataBaseHandler.insert_product_query)) {
            for (int i = 1; i < AppFX.num + 1; i++) {
                statement.setString(1, "tovar_" + i);
                statement.setInt(2, i * 10);
                statement.executeUpdate();
            }
        }
    }

    public void shutdown() throws SQLException {
        if (connection != null) {
            String delete_query = "DELETE FROM goods";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(delete_query);
            }
            connection.close();
        }
    }

    void addActions() {

        actionsByCommand.put("/show_all", (param) -> {
            ObservableList<Good> data = FXCollections.observableArrayList();
            try (
                    Statement statement = connection.createStatement();
                    ResultSet response = statement.executeQuery(select_all_query)
            ) {
                while (response.next()) {
                    String title = response.getString("title");
                    int cost = response.getInt("cost");
                    data.add(new Good(title, cost));
                }
            } catch (SQLException e) {
                this.logger.log(Level.SEVERE, "select all error", e);
            }
            return data;
        });

        actionsByCommand.put("/price", good -> {
            Good cost = null;
            try (PreparedStatement statement = connection.prepareStatement(select_product_query)) {

                statement.setString(1, good.getName());

                try (ResultSet response = statement.executeQuery()) {
                    if (response.next()) {
                        cost = new Good(good.getName(), Integer.parseInt(response.getString("cost")));
                        System.out.println(cost.getCost());
                    } else {
                        System.out.println("Такого товара нет");
                        return null;
                    }

                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "select any error", e);
            }
            ObservableList<Good> goods = FXCollections.observableArrayList();
            goods.add(cost);
            return goods;
        });

        actionsByCommand.put("/delete", good -> {
            try (PreparedStatement statement = connection.prepareStatement(delete_product_query)) {
                statement.setString(1, good.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "deleting error", e);
            }
            return null;
        });
//
        actionsByCommand.put("/change_price", good -> {
            try (PreparedStatement statement = connection.prepareStatement(update_product_query)) {
                try {
                    statement.setInt(1, good.getCost());
                    statement.setString(2, good.getName());
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return null;
                }
                if (statement.executeUpdate() == 0) {
                    System.out.println("Такого товара нет");
                } else {
                    System.out.println("Значение успешно обновлено");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "update error", e);
            }
            return null;
        });
//
//        actionsByCommand.put("/filter_by_price", logger -> {
//            try (PreparedStatement statement = connection.prepareStatement(select_range_query)) {
//                try {
//                    statement.setInt(1, Integer.parseInt(parsedCommand[1]));
//                    statement.setInt(2, Integer.parseInt(parsedCommand[2]));
//                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//                    System.out.println("Неправильная команда");
//                    return;
//                }
//                try (ResultSet response = statement.executeQuery()) {
//                    while (response.next()) {
//                        String title = response.getString("title");
//                        String cost = response.getString("cost");
//                        System.out.println(title + " " + cost);
//                    }
//                }
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "filter error", e);
//            }
//        });
//        //обработать отрицательную цену товара
//        actionsByCommand.put("/add", logger -> {
//            try (PreparedStatement statement
//                         = connection.prepareStatement(insert_product_query)) {
//                try {
//                    statement.setString(1, parsedCommand[1]);
//                    statement.setInt(2, Integer.parseInt(parsedCommand[2]));
//                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//                    System.out.println("Неправильная команда");
//                    return;
//                }
//                try {
//                    statement.executeUpdate();
//                    System.out.println("Товар был успешно добавлен");
//                } catch (org.postgresql.util.PSQLException e) {
//                    System.out.println("Такой товар уже существует");
//                }
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "insert error", e);
//            }
//        });
    }

    public Map<String, Function<Good, ObservableList<Good>>> getActionsByCommand() {
        return actionsByCommand;
    }
}
