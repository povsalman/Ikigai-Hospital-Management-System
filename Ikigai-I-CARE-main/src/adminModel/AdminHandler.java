package adminModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminHandler extends BaseDatabaseHandler {

    public AdminHandler() {
        super();
    }

    // Fetch the currently logged-in admin's details from the database
    public Admin getLoggedInAdminData() {
        Admin admin = null;
        
        // Get the logged-in admin ID from the AdminModel
        int loggedInAdminId = AdminModel.getInstance().getLoggedInAdminId();

        // SQL query to fetch the details of the logged-in admin by admin_id
        String query = "SELECT admin_id, admin_Fname, admin_Lname, email, password, phone_no " +
                       "FROM Admin WHERE admin_id = ?";  // Use parameterized query to prevent SQL injection

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, loggedInAdminId);  // Set the logged-in admin's ID to the query

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    // Populate the admin object with the logged-in admin's data
                    admin = new Admin(
                        resultSet.getInt("admin_id"),
                        resultSet.getString("admin_Fname"),
                        resultSet.getString("admin_Lname"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_no")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logged-in admin data: " + e.getMessage());
        }

        return admin;
    }
    
 // Method to validate admin login
    public boolean validateAdminLogin(int userId, String password) {
        String query = "SELECT * FROM Admin WHERE admin_id = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);  // Set userId in query
            stmt.setString(2, password);  // Set password in query
            try (ResultSet resultSet = stmt.executeQuery()) {
                // If a result exists, the login is valid
                if (resultSet.next()) {
                    // Save the logged-in admin's ID to AdminModel for further use
                    AdminModel.getInstance().setLoggedInAdminId(resultSet.getInt("admin_id"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating admin login: " + e.getMessage());
        }
        return false;  // Return false if login is invalid
    }
    
    public int addAdmin(String firstName, String lastName, String email, String password, String phoneNo) {
        String insertQuery = "INSERT INTO Admin (admin_Fname, admin_Lname, email, password, phone_no) VALUES (?, ?, ?, ?, ?)";
        String fetchQuery = "SELECT MAX(admin_id) AS latest_admin_id FROM Admin";
        int adminId = -1; // Default value if no ID is found

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            // Insert the admin into the database
            insertStmt.setString(1, firstName);
            insertStmt.setString(2, lastName);
            insertStmt.setString(3, email);
            insertStmt.setString(4, password);
            insertStmt.setString(5, phoneNo);
            int affectedRows = insertStmt.executeUpdate();

            // If insertion is successful, fetch the most recent admin ID
            if (affectedRows > 0) {
                try (PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery)) {
                    try (ResultSet rs = fetchStmt.executeQuery()) {
                        if (rs.next()) {
                            adminId = rs.getInt("latest_admin_id");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding admin: " + e.getMessage());
        }
        return adminId; // Return the fetched admin ID or -1 if not found
    }



}
