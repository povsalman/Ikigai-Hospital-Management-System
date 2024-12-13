package patientHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import patientModel.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoctorDBHandler {
	private Connection con;

    public DoctorDBHandler() {
    	this.con = DBHandler.getInstance().getConnection();
	}

    // Method to load all doctors
    public ObservableList<Doctor> loadAllDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String query = "SELECT doctor_id, CONCAT(doc_Fname, ' ', doc_Lname) AS doc_name, email, gender, mobile_number, specialized FROM Doctor";

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                    resultSet.getInt("doctor_id"),
                    resultSet.getString("doc_name"),
                    resultSet.getString("email"),
                    resultSet.getString("gender"),
                    resultSet.getString("mobile_number"),
                    resultSet.getString("specialized"),
                    null
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    // Method to search doctors based on field and value
    public ObservableList<Doctor> searchDoctors(String field, String value) {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        String query = buildSearchQuery(field, value);

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                    resultSet.getInt("doctor_id"),
                    resultSet.getString("doc_name"),
                    resultSet.getString("email"),
                    resultSet.getString("gender"),
                    resultSet.getString("mobile_number"),
                    resultSet.getString("specialized"),
                    null
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    // Helper method to build the search query
    private String buildSearchQuery(String field, String value) {
        String column;
        String formattedValue = "'%" + value + "%'";
        switch (field) {
            case "Name":
                column = "CONCAT(doc_Fname, ' ', doc_Lname)";
                break;
            case "Email":
                column = "email";
                break;
            case "Specialization":
                column = "specialized";
                break;
            case "Phone Number":
                column = "mobile_number";
                break;
            case "Gender":
                column = "gender";
                formattedValue = "'" + value + "'"; // Exact match for gender
                break;
            default:
                throw new IllegalArgumentException("Invalid search field selected.");
        }
        return "SELECT doctor_id, CONCAT(doc_Fname, ' ', doc_Lname) AS doc_name, email, gender, mobile_number, specialized FROM Doctor WHERE " + column + " LIKE " + formattedValue;
    }
}

