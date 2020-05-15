package com.example;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler {
    private Map<String, Consumer<Logger>> actionsByCommand;

    private String[] parsedCommand;
    private Connection connection;

    final static String select_all_query = "SELECT * FROM goods";
    final static String select_range_query = "SELECT title FROM goods WHERE cost > ? AND cost < ?";
    final static String select_product_query = "SELECT cost FROM goods WHERE title = ?";
    final static String update_product_query = "UPDATE goods SET cost = ? WHERE title = ?";
    final static String insert_product_query = "INSERT INTO goods (title, cost) VALUES (?, ?)";
    final static String delete_product_query = "DELETE FROM goods WHERE title = ?";

    CommandHandler(final String[] parsedCommand, final Connection connection) {
        this.parsedCommand = parsedCommand;
        this.actionsByCommand = new HashMap<>();
        this.connection = connection;
        this.addActions();
    }

    void addActions() {
        actionsByCommand.put("/show_all", logger -> {
            try (
                    Statement statement = connection.createStatement();
                    ResultSet response = statement.executeQuery(select_all_query)
            ) {
                while (response.next()) {
                    String title = response.getString("title");
                    int cost = response.getInt("cost");
                    System.out.println(title + " " + cost);
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "select all error", e);
            }
        });

        actionsByCommand.put("/price", logger -> {
            try (PreparedStatement statement = connection.prepareStatement(select_product_query)) {
                try {
                    statement.setString(1, parsedCommand[1]);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return;
                }
                try (ResultSet response = statement.executeQuery()) {
                    if (response.next()) {
                        String cost = response.getString("cost");
                        System.out.println(cost);
                    } else {
                        System.out.println("Такого товара нет");
                    }

                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "select any error", e);
            }
        });

        actionsByCommand.put("/delete", logger -> {
            try (PreparedStatement statement = connection.prepareStatement(delete_product_query)) {
                try {
                    statement.setString(1, parsedCommand[1]);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return;
                }
                if (statement.executeUpdate() == 0) {
                    System.out.println("Такого товара нет");
                } else {
                    System.out.println("Значение успешно удалено");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "deleting error", e);
            }
        });

        actionsByCommand.put("/change_price", logger -> {
            try (PreparedStatement statement = connection.prepareStatement(update_product_query)) {
                try {
                    statement.setInt(1, Integer.parseInt(parsedCommand[2]));
                    statement.setString(2, parsedCommand[1]);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return;
                }
                if (statement.executeUpdate() == 0) {
                    System.out.println("Такого товара нет");
                } else {
                    System.out.println("Значение успешно обновлено");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "update error", e);
            }
        });

        actionsByCommand.put("/filter_by_price", logger -> {
            try (PreparedStatement statement = connection.prepareStatement(select_range_query)) {
                try {
                    statement.setInt(1, Integer.parseInt(parsedCommand[1]));
                    statement.setInt(2, Integer.parseInt(parsedCommand[2]));
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return;
                }
                try (ResultSet response = statement.executeQuery()) {
                    while (response.next()) {
                        String title = response.getString("title");
                        System.out.println(title);
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "filter error", e);
            }
        });

        actionsByCommand.put("/add", logger -> {
            try (PreparedStatement statement
                         = connection.prepareStatement(CommandHandler.insert_product_query)) {
                try {
                    statement.setString(1, parsedCommand[1]);
                    statement.setInt(2, Integer.parseInt(parsedCommand[2]));
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Неправильная команда");
                    return;
                }
                try {
                    statement.executeUpdate();
                    System.out.println("Товар был успешно добавлен");
                } catch (org.postgresql.util.PSQLException e) {
                    System.out.println("Такой товар уже существует");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "insert error", e);
            }
        });
    }

    Map<String, Consumer<Logger>> getActionsByCommand() {
        return actionsByCommand;
    }
}
