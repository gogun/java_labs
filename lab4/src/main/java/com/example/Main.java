package com.example;

import java.sql.*;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Main {

    static private String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    static private String username = "postgres";
    static private String pass = "123";

    private static void generateValues() throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl, username, pass);
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

        String delete_query = "DELETE FROM goods";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(delete_query);
        }

        try (PreparedStatement statement
                     = connection.prepareStatement(CommandHandler.insert_product_query)) {
            //задавать кол-во генерируемых строк
            for (int i = 1; i < 11; i++) {
                statement.setString(1, "tovar_" + i);
                statement.setInt(2, i * 10);
                statement.executeUpdate();
            }
        }
    }

    private static Consumer<Logger> invalidCommandAction = (command ->
            System.out.println("Неправильная команда"));

    public static void main(String[] args) throws SQLException {
        generateValues();
        Logger logger = Logger.getLogger(Main.class.getName());
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String full_command = scanner.nextLine();
            String[] command_data = full_command.split(" ");
            String command = command_data[0];
            try (Connection connection = DriverManager.getConnection(dbUrl, username, pass)) {
                CommandHandler ch = new CommandHandler(command_data, connection);
                Consumer<Logger> action = ch
                        .getActionsByCommand()
                        .getOrDefault(command, invalidCommandAction);
                action.accept(logger);
            }
        }
    }

}
