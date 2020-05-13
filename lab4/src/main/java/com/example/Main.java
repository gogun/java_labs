package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    private static String delete_query = "DELETE FROM goods";

    private static void generateValues(Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(delete_query)) {
            statement.executeUpdate();
        }
        try(PreparedStatement statement
                    = connection.prepareStatement(CommandHandler.insert_product_query)) {
            for (int i = 1; i < 11; i++) {
                statement.setString(1, "tovar_" + i);
                statement.setInt(2, i*10);
                statement.executeUpdate();
            }
        }
    }

    private static Consumer<String> invalidCommandAction = (command ->
            System.out.println("Неправильная команда"));

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String full_command = scanner.nextLine();
            String[] command_data = full_command.split(" ");
            String command = command_data[0];
            String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String pass = "123";
            try (Connection connection = DriverManager.getConnection(dbUrl, username, pass)) {
                generateValues(connection);
                CommandHandler ch = new CommandHandler(command_data, connection);
                Consumer<String> action = ch
                        .getActionsByCommand()
                        .getOrDefault(command, invalidCommandAction);
                action.accept(command);
            }
        }
    }

}
