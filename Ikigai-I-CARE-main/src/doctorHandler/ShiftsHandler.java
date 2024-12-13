package doctorHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import doctorModel.ShiftsData;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ShiftsHandler {

	private Connection connection;

    public ShiftsHandler(DBHandler dbHandler) {
        this.connection = dbHandler.getConnection();
    }

    // Method to get all shift data
    public ObservableList<ShiftsData> getAllShifts() {
        ObservableList<ShiftsData> shiftsList = FXCollections.observableArrayList();
        String sql = "SELECT s.shift_id, " +
                "CONCAT(d.doc_Fname, ' ', d.doc_Lname) AS doctorAssigned, " +
                "d.specialized AS isDepartment, s.description, " +
                "s.shift_date AS date, s.start_time, s.end_time, s.status, " +
                "d.doctor_id " +
                "FROM Shift s " +
                "JOIN Doctor d ON d.doctor_id = s.doctor_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ShiftsData shift = new ShiftsData(
                        rs.getInt("shift_id"),
                        rs.getString("doctorAssigned"),
                        rs.getString("isDepartment"),
                        rs.getString("description"),
                        rs.getString("date") != null ? rs.getString("date") : "-",
                        rs.getString("start_time") != null ? rs.getString("start_time") : "-",
                        rs.getString("end_time") != null ? rs.getString("end_time") : "-",
                        rs.getString("status"),
                        rs.getInt("doctor_id")
                );
                shiftsList.add(shift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shiftsList;
    }

    // Method to update shift status for "Yes" button
    public boolean updateShiftStatus(int shiftId, int doctorId) {
        String sqlCheck = "SELECT doctor_id, status FROM Shift WHERE shift_id = ?";
        String sqlUpdate = "UPDATE Shift SET status = 'Available', shift_date = NULL, start_time = NULL, end_time = NULL " +
                "WHERE shift_id = ? AND doctor_id = ? AND status != 'Available'";

        try (PreparedStatement checkStmt = connection.prepareStatement(sqlCheck)) {
            checkStmt.setInt(1, shiftId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int shiftDoctorId = rs.getInt("doctor_id");
                    String currentStatus = rs.getString("status");

                    if (shiftDoctorId == doctorId && !"Available".equals(currentStatus)) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                            updateStmt.setInt(1, shiftId);
                            updateStmt.setInt(2, doctorId);
                            int rowsUpdated = updateStmt.executeUpdate();
                            return rowsUpdated > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	
}
