package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandHandler {
    private Map<String, Consumer<String>> actionsByCommand;
    private String[] parsedCommand;
    private Connection connection;

    private static String select_all_query = "SELECT * FROM goods";
    private static String select_range_query = "SELECT title FROM goods WHERE cost > ? AND cost < ?";
    private static String select_product_query = "SELECT cost FROM goods WHERE title = ?";
    private static String update_product_query = "UPDATE goods SET title = ? WHERE cost = ?";
    static String insert_product_query = "INSERT INTO goods (title, cost) VALUES (?, ?)";
    private static String delete_product_query = "DELETE FROM goods WHERE title = ?";

    CommandHandler(String[] parsedCommand, Connection connection) {
        this.parsedCommand = parsedCommand;
        this.actionsByCommand = new HashMap<>();
        this.connection = connection;
        this.addActions();
    }

    void addActions() {
        actionsByCommand.put("/show_all", command -> {
            try (
                    PreparedStatement statement = connection.prepareStatement(select_all_query);
                    ResultSet response = statement.executeQuery()
            ) {
                while (response.next()) {
                    String title = response.getString("title");
                    int cost = response.getInt("cost");
                    System.out.println(title + " " + cost);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        actionsByCommand.put("/price", command -> {
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
                e.printStackTrace();
            }
        });

        actionsByCommand.put("/delete", command -> {
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
                e.printStackTrace();
            }
        });

        actionsByCommand.put("/change_price", command -> {
            try (PreparedStatement statement = connection.prepareStatement(update_product_query)) {
                try {
                    statement.setString(1, parsedCommand[1]);
                    statement.setInt(2, Integer.parseInt(parsedCommand[2]));
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
                e.printStackTrace();
            }
        });

        actionsByCommand.put("/filter_by_price", command -> {
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
                e.printStackTrace();
            }
        });

        actionsByCommand.put("/add", command -> {
            try (PreparedStatement statement = connection.prepareStatement(insert_product_query)) {
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
                e.printStackTrace();
            }
        });
    }

    public Map<String, Consumer<String>> getActionsByCommand() {
        return actionsByCommand;
    }
}
