package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

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
                CommandHandler ch = new CommandHandler(command_data, connection);
                Consumer<String> action = ch
                        .getActionsByCommand()
                        .getOrDefault(command, invalidCommandAction);
                action.accept(command);
            }
        }
    }

}
