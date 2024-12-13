package adminModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShiftHandler extends BaseDatabaseHandler {

	public List<Shift> getAllCurrentShifts() {
	    List<Shift> shifts = new ArrayList<>();
	    String query = "SELECT s.doctor_id AS docID, CONCAT(d.doc_Fname, ' ', d.doc_Lname) AS doctor_fullname, " +
	               "s.admin_id AS admin_id, CONCAT(a.admin_Fname, ' ', a.admin_Lname) AS admin_fullname, " +
	               "s.start_time AS shift_start_time, s.end_time AS shift_end_time, s.shift_date AS shift_date, " +
	               "d.specialized AS specialized, s.status AS shift_status, s.description as type " + // Fixed spelling here
	               "FROM Shift s " +
	               "INNER JOIN Doctor d ON s.doctor_id = d.doctor_id " +
	               "INNER JOIN Admin a ON s.admin_id = a.admin_id " +
	               "WHERE s.status = 'Active'";

	    try (ResultSet rs = executeQuery(query)) {
	        while (rs.next()) {
	            shifts.add(new Shift(
	                rs.getInt("docID"),
	                rs.getString("doctor_fullname"),
	                rs.getInt("admin_id"),
	                rs.getString("admin_fullname"),
	                rs.getString("shift_start_time"),
	                rs.getString("shift_end_time"),
	                rs.getString("shift_date"),
	                rs.getString("specialized"),
	                rs.getString("shift_status"),
	                rs.getString("type")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return shifts;
	}


  

	public boolean checkShiftClash(int doctorId, String date, String startTime, String endTime) {
	    String query = "SELECT COUNT(*) AS clash_count " +
	                   "FROM Shift " +
	                   "WHERE doctor_id = ? AND shift_date = ? AND " +
	                   "((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        stmt.setString(2, date);
	        stmt.setString(3, endTime);
	        stmt.setString(4, startTime);
	        stmt.setString(5, endTime);
	        stmt.setString(6, startTime);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("clash_count") > 0;
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking shift clash: " + e.getMessage());
	    }

	    return false;
	}

	//inserting the shift for the new doctor in the system

	public boolean insertShift(int doctorId, int adminId, String date, String description, String startTime, String endTime, String status) {
	    String query = "INSERT INTO Shift (doctor_id, admin_id, shift_date, description, start_time, end_time, status) " +
	                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        stmt.setInt(2, adminId);
	        stmt.setString(3, date);
	        stmt.setString(4, description);
	        stmt.setString(5, startTime);
	        stmt.setString(6, endTime);
	        stmt.setString(7, status);

	        return stmt.executeUpdate() > 0; // True if insert was successful
	    } catch (SQLException e) {
	        System.err.println("Error inserting shift: " + e.getMessage());
	    }
	    return false;
	}

	// Updating the shift after assigining
	
	public boolean updateShiftForAssignShift(int doctorId, int adminId, String date, String description, String startTime, String endTime, String status) {
	    String query = "UPDATE Shift SET admin_id = ?, shift_date = ?, description = ?, start_time = ?, end_time = ?, status = ? " +
	                   "WHERE doctor_id = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, adminId);
	        stmt.setString(2, date);
	        stmt.setString(3, description);
	        stmt.setString(4, startTime);
	        stmt.setString(5, endTime);
	        stmt.setString(6, status);
	        stmt.setInt(7, doctorId); // Tie the update to the correct doctor

	        return stmt.executeUpdate() > 0; // True if update was successful
	    } catch (SQLException e) {
	        System.err.println("Error updating shift: " + e.getMessage());
	    }
	    return false;
	}

	
	
	public boolean updateShift(int doctorId, int adminId, String date, String description, String startTime, String endTime, String status) {
	    String query = "UPDATE Shift SET admin_id = ?, shift_date = ?, description = ?, start_time = ?, end_time = ?, status = ? " +
	                   "WHERE doctor_id = ? AND shift_date = ? AND status = 'Active'";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, adminId);
	        stmt.setString(2, date);
	        stmt.setString(3, description);
	        stmt.setString(4, startTime);
	        stmt.setString(5, endTime);
	        stmt.setString(6, status);
	        stmt.setInt(7, doctorId);
	        stmt.setString(8, date); // Ensure the update is tied to the correct shift date

	        return stmt.executeUpdate() > 0; // True if update was successful
	    } catch (SQLException e) {
	        System.err.println("Error updating shift: " + e.getMessage());
	    }
	    return false;
	}


//checking doctor appointments during shift
	
	
	public List<String> checkDoctorEventsForDate(int doctorId, String date) {
	    List<String> conflicts = new ArrayList<>();
	    String query = "SELECT 'Appointment' AS event_type, a.appointment_id AS event_id, " +
	                   "a.appointment_date AS event_date, a.status_ AS event_status, " +
	                   "a.description AS event_description " +
	                   "FROM Appointment a " +
	                   "WHERE a.doctor_id = ? AND a.appointment_date = ? " +
	                   "UNION ALL " +
	                   "SELECT 'Surgery' AS event_type, s.surgery_id AS event_id, " +
	                   "s.operation_date AS event_date, s.status AS event_status, " +
	                   "CONCAT('Room ', s.room_id) AS event_description " +
	                   "FROM Surgery s " +
	                   "WHERE s.doctor_id = ? AND s.operation_date = ?";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        stmt.setString(2, date);
	        stmt.setInt(3, doctorId);
	        stmt.setString(4, date);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String event = rs.getString("event_type") + " - " +
	                           "ID: " + rs.getInt("event_id") + ", " +
	                           "Date: " + rs.getString("event_date") + ", " +
	                           "Status: " + rs.getString("event_status") + ", " +
	                           "Description: " + rs.getString("event_description");
	            conflicts.add(event);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking doctor events: " + e.getMessage());
	    }

	    return conflicts;
	}

	
	public boolean isShiftActiveForDoctor(int doctorId, String date) {
	    String query = "SELECT COUNT(*) AS active_shift_count FROM Shift " +
	                   "WHERE doctor_id = ? AND shift_date = ? AND status = 'Active'";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        stmt.setString(2, date);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("active_shift_count") > 0;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking shift status: " + e.getMessage());
	    }
	    return false;
	}

	public boolean isShiftAssignedForDoctor(int doctorId, String date) {
	    String query = "SELECT COUNT(*) AS count FROM Shift WHERE doctor_id = ? AND shift_date = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        stmt.setString(2, date);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            int count = rs.getInt("count");
	            return count > 0; // If count > 0, the shift is assigned
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking if shift is assigned for doctor: " + e.getMessage());
	    }
	    return false;
	}

	public String getShiftStatusForDoctor(int doctorId) {
	    String query = "SELECT status FROM Shift WHERE doctor_id = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, doctorId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("status");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching shift status for doctor: " + e.getMessage());
	    }
	    return null; // Return null if no record exists
	}


   
    }




