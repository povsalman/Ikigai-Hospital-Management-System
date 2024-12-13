package Doctorapplication;

import java.sql.Connection;
import java.sql.DriverManager;


public class Database {

    public static Connection connectDB() {

        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection connect
                    = DriverManager.getConnection("jdbc:mysql://localhost:3306/Ikigai", "root", "50258952"); // root IS OUR DEFAULT USERNAME AND EMPTY OR NULL OR BLANK TO OUR PASSWORD
        
            
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
