package patientHandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class AppointmentDBHandler {

	private final Connection con;

    public AppointmentDBHandler() {
    	this.con = DBHandler.getInstance().getConnection(); 
    }

    public boolean insertAppointment(int patientId, int doctorId, String description, LocalDate appointmentDate, String status, double price) {
        String query = "INSERT INTO Appointment (patient_id, doctor_id, description, appointment_date, status_, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, description);
            stmt.setDate(4, Date.valueOf(appointmentDate));
            stmt.setString(5, status);
            stmt.setDouble(6, price);

            return stmt.executeUpdate() > 0; // Returns true if insertion succeeds
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDoctorFullyBooked(int doctorId, LocalDate appointmentDate) {
        String query = "SELECT COUNT(*) AS appointment_count FROM Appointment WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            stmt.setDate(2, Date.valueOf(appointmentDate));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("appointment_count") >= 50;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean deleteAppointment(int appointmentId) {
        String query = "DELETE FROM Appointment WHERE appointment_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
