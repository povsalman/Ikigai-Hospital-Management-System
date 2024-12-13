package patientHandler;

import patientModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDBHandler {
    private Connection con;

    public BillingDBHandler() {
        this.con = DBHandler.getInstance().getConnection();
    }

    public ObservableList<Appointment> getUnpaidAppointments(int patientId) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM Appointment WHERE patient_id = ? AND payment_status = 'Unpaid'";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, patientId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getString("description"),
                    null,
                    null,
                    rs.getDouble("price")
            );
            appointments.add(appointment);
        }
        return appointments;
    }

    public ObservableList<Room> getUnpaidRoomBookings(int patientId) throws SQLException {
        ObservableList<Room> roomBookings = FXCollections.observableArrayList();
        String query = "SELECT booking_id, room_type, price FROM Room r JOIN Booking b ON r.room_id = b.room_id WHERE b.patient_id = ? AND b.payment_status = 'Unpaid'";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, patientId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Room booking = new Room(
                    rs.getInt("booking_id"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),null);
            roomBookings.add(booking);
        }
        return roomBookings;
    }

    public ObservableList<PrescriptionItem> getUnpaidPrescriptions(int patientId) throws SQLException {
        ObservableList<PrescriptionItem> prescriptions = FXCollections.observableArrayList();
        String query = "SELECT pi.item_id, M.medsupply_name, pi.quantity, si.unit_price, si.quantity_available " +
                "FROM PrescriptionItem pi " +
                "JOIN StockItem si ON pi.item_id = si.item_id " +
                "JOIN Prescription P ON P.prescription_id = pi.prescription_id " +
                "JOIN MedicalSupply M ON M.medsupply_id = pi.item_id " +
                "WHERE pi.prescription_id IN ( " +
                "    SELECT prescription_id FROM Prescription WHERE patient_id = ? AND P.payment_status = 'Unpaid' " +
                ")";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, patientId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            PrescriptionItem prescription = new PrescriptionItem(
                    rs.getInt("item_id"),
                    rs.getString("medsupply_name"),
                    rs.getInt("quantity"), null,
                    rs.getDouble("unit_price"),rs.getDouble("unit_price")*rs.getInt("quantity"),
                    rs.getInt("quantity_available")
            );
            prescriptions.add(prescription);
        }
        return prescriptions;
    }

    public ObservableList<PatientTreatment> getUnpaidTreatments(int patientId) throws SQLException {
        ObservableList<PatientTreatment> treatments = FXCollections.observableArrayList();
        String query = "SELECT treatment_id, price FROM Patient_Treatment WHERE patient_id = ? AND payment_status = 'Unpaid'";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, patientId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
        	PatientTreatment treatment = new PatientTreatment(rs.getInt("treatment_id"),null,null,null,null,rs.getDouble("price"));
            treatments.add(treatment);
        }
        return treatments;
    }

    public void updatePaymentStatus(String table, String idColumn, int id) throws SQLException {
        String query = "UPDATE " + table + " SET payment_status = 'Paid' WHERE " + idColumn + " = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void updateStockQuantity(int itemId, int quantity) throws SQLException {
        String query = "UPDATE StockItem SET quantity_available = quantity_available - ? WHERE item_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, quantity);
        stmt.setInt(2, itemId);
        stmt.executeUpdate();
    }

    public void insertGoodOut(int itemId, int quantity, double totalSalePrice) throws SQLException {
        String query = "INSERT INTO GoodOut (item_id, quantity, sale_price, issued_date) VALUES (?, ?, ?, NOW())";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, itemId);
        stmt.setInt(2, quantity);
        stmt.setDouble(3, totalSalePrice);
        stmt.executeUpdate();
    }
}
