package patientController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import patientModel.Feedback;

public class FeedbackController {
    private Connection con;

    public FeedbackController(Connection con) {
        this.con = con;
    }

    public boolean submitFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback (patient_id, doctor_id, feedback_date, description_, rating_stars, status_) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getPatientId());
            stmt.setInt(2, feedback.getDoctorId());
            stmt.setDate(3, java.sql.Date.valueOf(feedback.getFeedbackDate())); // Convert LocalDate to SQL Date
            stmt.setString(4, feedback.getDescription());
            stmt.setInt(5, feedback.getRating());
            stmt.setString(6, feedback.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if feedback was successfully inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurred
        }
    }
}

