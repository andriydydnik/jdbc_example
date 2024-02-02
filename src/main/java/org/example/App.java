package org.example;

import org.flywaydb.core.Flyway;

import java.sql.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Hello world!
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class);

    private static String jdbcUrl = "jdbc:mysql://localhost:3306/osbb";
    private static String username = "root";
    private static String password = "ensml075f0";

    static void fw_migrate() {
        logger.info("Run migration");
        Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .locations("classpath:flyway/scripts")
                .load()
                .migrate();
    }

    public static void main(String[] args) {
        fw_migrate();

        logger.info("Run application");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);) {
            printSmallClountriesList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void printSmallClountriesList(Connection connection) throws SQLException {
        logger.debug("Print mall countries list");
        String sqlQuery = "SELECT  * FROM `country` WHERE Population < ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, 100000); // Встановлення значення параметра
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Отримання даних з результатів
                String name = resultSet.getString("name");
                long population = resultSet.getLong("population");
                System.out.println("Name: " + name + ", Population: " + population);
            }
        }
    }
}
