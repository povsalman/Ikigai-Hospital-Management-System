package doctorController;

import doctorModel.PatientTreatmentData;
import Doctorapplication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientTreatmentController {

	@FXML private TextField patienttreatment_treatmentID, patienttreatment_newtreatment;
	@FXML private TableView<PatientTreatmentData> patienttreatment_tableView;
	@FXML private TableColumn<PatientTreatmentData, String> patienttreatment_col_treatmentID, patienttreatment_col_patient, 
	                                                         patienttreatment_col_treatment, patienttreatment_col_startdate, 
	                                                         patienttreatment_col_exenddate, patienttreatment_col_status;

	private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private final AlertMessage alert = new AlertMessage();
    
    public void setPatientTreatmentComponents(
            TextField patienttreatment_treatmentID, TextField patienttreatment_newtreatment,
            TableView<PatientTreatmentData> patienttreatment_tableView,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_treatmentID,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_patient,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_treatment,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_startdate,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_exenddate,
            TableColumn<PatientTreatmentData, String> patienttreatment_col_status) {

        this.patienttreatment_treatmentID = patienttreatment_treatmentID;
        this.patienttreatment_newtreatment = patienttreatment_newtreatment;
        this.patienttreatment_tableView = patienttreatment_tableView;
        this.patienttreatment_col_treatmentID = patienttreatment_col_treatmentID;
        this.patienttreatment_col_patient = patienttreatment_col_patient;
        this.patienttreatment_col_treatment = patienttreatment_col_treatment;
        this.patienttreatment_col_startdate = patienttreatment_col_startdate;
        this.patienttreatment_col_exenddate = patienttreatment_col_exenddate;
        this.patienttreatment_col_status = patienttreatment_col_status;
    }

	
    public ObservableList<PatientTreatmentData> patientTreatmentGetData() {

        ObservableList<PatientTreatmentData> listData = FXCollections.observableArrayList();

        String sql = "SELECT t.treatment_id, p.first_name AS patientName, a.treatment AS treatment, " +
                "t.start_date AS startDate, t.end_date AS endDate, t.status, t.patient_id " +
                "FROM Patient_Treatment t " +
                "JOIN Patient p ON p.patient_id = t.patient_id " + // Added space here
                "JOIN Appointment a ON t.patient_id = a.patient_id " +
                "WHERE a.status_ = 'On Treatment'" +
                "AND a.doctor_id = ?"; // Closed the quote here

        connect = Database.connectDB();

        try {
            // Prepare the SQL statement
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); // Set the doctor_id in the query

            // Execute the query
            result = prepare.executeQuery();

            PatientTreatmentData treatmentData;

            while (result.next()) {
                // Assuming the PatientTreatmentData constructor matches these parameters
                treatmentData = new PatientTreatmentData(
                    result.getInt("treatment_id"),       // Treatment ID
                    result.getString("patientName"),    // Patient Name
                    result.getString("treatment"),      // Treatment
                    result.getString("startDate"),      // Start Date
                    result.getString("endDate"),        // End Date
                    result.getString("status"),          // Status
                    result.getInt("patient_id")
                );
                // Add the data to the list
                listData.add(treatmentData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listData;
    }

    public ObservableList<PatientTreatmentData> patientTreatmentListData;

    public void patientTreatmentShowData() {
        // Fetch the data for the Patient Treatment table
        patientTreatmentListData = patientTreatmentGetData();

        // Map PatientTreatmentData class fields to TableView columns
        patienttreatment_col_treatmentID.setCellValueFactory(new PropertyValueFactory<>("treatmentId"));
        patienttreatment_col_patient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patienttreatment_col_treatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        patienttreatment_col_startdate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        patienttreatment_col_exenddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        patienttreatment_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set data to the TableView
        patienttreatment_tableView.setItems(patientTreatmentListData);
    }

    public void patientTreatmentSelect() {
        // Get the selected item from the TableView
        PatientTreatmentData treatmentData = patienttreatment_tableView.getSelectionModel().getSelectedItem();
        int num = patienttreatment_tableView.getSelectionModel().getSelectedIndex();

        // Check for invalid selection
        if ((num - 1) < -1 || treatmentData == null) {
            return;
        }

        // Set the treatmentId TextField with the selected record's treatment ID
        patienttreatment_treatmentID.setText(String.valueOf(treatmentData.getTreatmentId()));
    }
    
    public void patienttreatmentYesBtn() {
        // Get the selected treatment record
        PatientTreatmentData treatmentData = patienttreatment_tableView.getSelectionModel().getSelectedItem();
        
        // Get the treatment ID and new treatment from the text fields
        String treatmentID = patienttreatment_treatmentID.getText();
        String newTreatment = patienttreatment_newtreatment.getText();
        
        // Check if the fields are not empty and a record is selected
        if (treatmentData != null && !treatmentID.isEmpty() && !newTreatment.isEmpty()) {
            // Update the Appointment table with the new treatment using JOIN
            String updateAppointmentSql = "UPDATE Appointment a " +
                                         "JOIN Patient_Treatment pt ON a.patient_id = pt.patient_id " +
                                         "SET a.treatment = ? " +
                                         "WHERE pt.treatment_id = ? AND a.status_ = 'On Treatment'";

            // Update the Patient_Treatment table status to 'Good'
            String updatePatientTreatmentSql = "UPDATE Patient_Treatment SET status = 'Good' WHERE treatment_id = ?";

            connect = Database.connectDB();

            try {
                // Begin transaction to ensure both updates happen together
                connect.setAutoCommit(false);

                // Update the Appointment table with the new treatment description
                prepare = connect.prepareStatement(updateAppointmentSql);
                prepare.setString(1, newTreatment);  // Set new treatment description
                prepare.setInt(2, Integer.parseInt(treatmentID));  // Set treatment ID
                prepare.executeUpdate();

                // Update the Patient_Treatment table status to 'Good'
                prepare = connect.prepareStatement(updatePatientTreatmentSql);
                prepare.setInt(1, Integer.parseInt(treatmentID));  // Set treatment ID
                int rowsAffected = prepare.executeUpdate();

                if (rowsAffected > 0) {
                    AlertMessage alertMessage = new AlertMessage();
                    alertMessage.successMessage("Feedback status updated to 'Reviewed'.");

                } else {
                    System.out.println("Failed to update feedback status.");
                }
                
                connect.commit();
                
                // Refresh the table data
                patientTreatmentShowData();
            } catch (Exception e) {
                try {
                    connect.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } 
    }

    
    public void patienttreatmentNoBtn() {
        // Clear the text fields or reset as needed
        patienttreatment_treatmentID.clear();
        patienttreatment_newtreatment.clear();
        
        patienttreatment_tableView.getSelectionModel().clearSelection();

        alert.errorMessage("No changes made. The treatment details were not updated.");

    }
   
    
}
