package doctorHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {
	private static DBHandler instance;
	private Connection connection;

	private static final String DB_URL = "jdbc:mysql://localhost:3306/Ikigai"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "50258952"; 


    public DBHandler() {
        // Initialize the connection
        try {
        	try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public static DBHandler getInstance() {
        if (instance == null) {
            synchronized (DBHandler.class) {
                if (instance == null) {
                    instance = new DBHandler();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection: " + e.getMessage());
            }
        }
    }
}
