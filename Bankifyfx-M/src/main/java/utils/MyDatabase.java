package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private static final String URL = "jdbc:mysql://localhost:3306/bankify";

    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static MyDatabase instance;



    public static MyDatabase getInstance() {
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}