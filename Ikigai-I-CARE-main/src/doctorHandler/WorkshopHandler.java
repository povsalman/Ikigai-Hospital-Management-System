package doctorHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import doctorModel.WorkshopData;

public class WorkshopHandler {
    private Connection connection;

    // Constructor to accept the database connection
    public WorkshopHandler(DBHandler dbHandler) {
        this.connection = dbHandler.getConnection();
    }

    // Method to get all workshops for a doctor
    public List<WorkshopData> getAllWorkshopsForDoctor(int doctorId) {
        List<WorkshopData> workshops = new ArrayList<>();
        String sql = "SELECT w.workshop_id, w.workshop_name AS topic, w.speaker, w.description, " +
                     "w.workshop_date AS date, w.location, w.start_time, w.end_time, " +
                     "CASE WHEN w.workshop_date > CURDATE() THEN 'Upcoming' " +
                     "     WHEN w.workshop_date = CURDATE() THEN 'Active' " +
                     "     ELSE 'Completed' END AS status, " +
                     "IFNULL(dw.status, 'Unregistered') AS regstatus " +
                     "FROM Workshop w " +
                     "LEFT JOIN Doctor_Workshop dw ON w.workshop_id = dw.workshop_id AND dw.doctor_id = ? " +
                     "ORDER BY w.workshop_date ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                workshops.add(new WorkshopData(
                    resultSet.getInt("workshop_id"),
                    resultSet.getString("topic"),
                    resultSet.getString("speaker"),
                    resultSet.getString("description"),
                    resultSet.getString("date"),
                    resultSet.getString("location"),
                    resultSet.getString("start_time"),
                    resultSet.getString("end_time"),
                    resultSet.getString("status"),
                    resultSet.getString("regstatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workshops;
    }
    
    
 // Method to register for a workshop
    public boolean registerForWorkshop(int doctorId, int workshopId) {
        String sqlInsertOrUpdate = 
            "INSERT INTO Doctor_Workshop (doctor_id, workshop_id, status) VALUES (?, ?, 'Registered') " +
            "ON DUPLICATE KEY UPDATE status = 'Registered'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlInsertOrUpdate)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, workshopId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }

    // Method to unregister from a workshop
    public boolean unregisterFromWorkshop(int doctorId, int workshopId) {
        String sqlUpdate = 
            "UPDATE Doctor_Workshop SET status = 'Unregistered' WHERE doctor_id = ? AND workshop_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, workshopId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }
    
    
    
    
    
    
    
    
    
    
}
