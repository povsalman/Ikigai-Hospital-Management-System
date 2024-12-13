package patientHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import patientModel.*;

public class DashBoardDBHandler {
	private Connection con;
	
	public DashBoardDBHandler() {
        this.con = DBHandler.getInstance().getConnection();
    }

    public ObservableList<PatientTreatment> getPatientTreatmentData(int patientId) {
        ObservableList<PatientTreatment> treatmentList = FXCollections.observableArrayList();
        String sql = "SELECT medical_details, start_date, end_date, status, price " +
                     "FROM Patient_Treatment T " +
                     "JOIN Patient P ON P.patient_id = T.patient_id " +
                     "WHERE P.patient_id = ?";

        try (PreparedStatement prepare = con.prepareStatement(sql)) {
            prepare.setInt(1, patientId);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                PatientTreatment treatmentData = new PatientTreatment(
                    patientId,
                    result.getString("medical_details"),
                    result.getDate("start_date"),
                    result.getDate("end_date"),
                    result.getString("status"),
                    result.getDouble("price")
                );
                treatmentList.add(treatmentData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return treatmentList;
    }

    public ObservableList<Appointment> getAppointmentsForPatient(int patientId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT A.appointment_id, description, " +
                       "CONCAT(D.doc_Fname, ' ', D.doc_Lname) AS doc_name, " +
                       "A.appointment_date, A.price " +
                       "FROM Appointment A " +
                       "JOIN Patient P ON P.patient_id = A.patient_id " +
                       "JOIN Doctor D ON A.doctor_id = D.doctor_id " +
                       "WHERE P.patient_id = ?";

        try (PreparedStatement prepare = con.prepareStatement(query)) {
            prepare.setInt(1, patientId);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                Appointment appointment = new Appointment(
                    result.getInt("appointment_id"),
                    result.getString("description"),
                    result.getString("doc_name"),
                    result.getDate("appointment_date"),
                    result.getDouble("price")
                );

                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentList;
    }

    public ObservableList<Doctor> getDoctorDetails(int patientId) {
        ObservableList<Doctor> doctorDetailsList = FXCollections.observableArrayList();
        String query = "SELECT D.doctor_id, CONCAT(D.doc_Fname, ' ', D.doc_Lname) AS doc_name, " +
                       "D.specialized, D.email, D.mobile_number, D.image " +
                       "FROM Appointment A " +
                       "JOIN Doctor D ON A.doctor_id = D.doctor_id " +
                       "WHERE A.patient_id = ? " +
                       "ORDER BY A.appointment_date DESC LIMIT 1";

        try (PreparedStatement prepare = con.prepareStatement(query)) {
            prepare.setInt(1, patientId);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                Doctor doctorDetails = new Doctor(result.getInt("doctor_id"),
                    result.getString("doc_name"),
                    result.getString("email"),null,
                    result.getString("mobile_number"),
                    result.getString("specialized"),
                    result.getString("image")
                );

                doctorDetailsList.add(doctorDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorDetailsList;
    }
}

