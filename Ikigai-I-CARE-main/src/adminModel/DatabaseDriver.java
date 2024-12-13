package adminModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDriver {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/ikigai"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "ilovecoffee"; 

    // Method to load the driver and establish a connection
    public static Connection connectToDatabase() {
        Connection connection = null;
        try {
            // Load the JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database!");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
