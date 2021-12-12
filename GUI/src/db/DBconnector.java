package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnector {

    private static String url = "jdbc:mysql://localhost:3306/car_rental";
    private static String username = "root";
    private static String password = ""; //enter your password here

    public static Connection connect(){
        System.out.println("Connecting database...");
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }   
}
