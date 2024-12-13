package doctorController;

import doctorModel.SurgeryData;
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

public class SurgeryController {

	@FXML private TextField surgerylist_surgeryID;
	@FXML private TableView<SurgeryData> surgerylist_tableView;
	@FXML private TableColumn<SurgeryData, String> surgerylist_col_surgeryID, surgerylist_col_patient, 
	                                                surgerylist_col_diagnosis, surgerylist_col_room, 
	                                                surgerylist_col_date, surgerylist_col_time, 
	                                                surgerylist_col_status;
	
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private final AlertMessage alert = new AlertMessage();
    
    
    public void setSurgeryListComponents(TextField surgerylist_surgeryID, TableView<SurgeryData> surgerylist_tableView,
            TableColumn<SurgeryData, String> surgerylist_col_surgeryID, TableColumn<SurgeryData, String> surgerylist_col_patient,
            TableColumn<SurgeryData, String> surgerylist_col_diagnosis, TableColumn<SurgeryData, String> surgerylist_col_room,
            TableColumn<SurgeryData, String> surgerylist_col_date, TableColumn<SurgeryData, String> surgerylist_col_time,
            TableColumn<SurgeryData, String> surgerylist_col_status) {

        this.surgerylist_surgeryID = surgerylist_surgeryID; 
        this.surgerylist_tableView = surgerylist_tableView;
        this.surgerylist_col_surgeryID = surgerylist_col_surgeryID; 
        this.surgerylist_col_patient = surgerylist_col_patient;
        this.surgerylist_col_diagnosis = surgerylist_col_diagnosis; 
        this.surgerylist_col_room = surgerylist_col_room;
        this.surgerylist_col_date = surgerylist_col_date; 
        this.surgerylist_col_time = surgerylist_col_time;
        this.surgerylist_col_status = surgerylist_col_status;
    }

    
    public ObservableList<SurgeryData> surgeryGetData() {

        ObservableList<SurgeryData> listData = FXCollections.observableArrayList();

        String sql = "SELECT s.surgery_id, " +
                     "       p.first_name AS patientName, " +
                     "       a.diagnosis AS diagnosis, " +
                     "       r.room_type AS room, " +
                     "       s.operation_date AS date, " +
                     "       s.operation_time, " +
                     "       s.status, " +
                     "       p.patient_id " +
                     "FROM Surgery s " +
                     "JOIN Patient p ON p.patient_id = s.patient_id " +
                     "JOIN Appointment a ON s.patient_id = a.patient_id " +
                     "JOIN Room r ON r.room_id = s.room_id " +
                     "WHERE a.status_ = 'Surgery' " +
                     "AND a.doctor_id = ?";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id);
            result = prepare.executeQuery();

            while (result.next()) {
                SurgeryData surgeryData = new SurgeryData(
                    result.getInt("surgery_id"),
                    result.getString("patientName"),
                    result.getString("diagnosis"),
                    result.getString("room"),
                    result.getString("date"),
                    result.getString("operation_time"),
                    result.getString("status"),
                    result.getInt("patient_id")
                );
                listData.add(surgeryData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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


    public ObservableList<SurgeryData> surgeryListData;

    public void surgeryShowData() {
        // Fetch the data for the Surgery table
        surgeryListData = surgeryGetData();

        // Map SurgeryData class fields to TableView columns
        surgerylist_col_surgeryID.setCellValueFactory(new PropertyValueFactory<>("surgeryId"));
        surgerylist_col_patient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        surgerylist_col_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        surgerylist_col_room.setCellValueFactory(new PropertyValueFactory<>("room"));
        surgerylist_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        surgerylist_col_time.setCellValueFactory(new PropertyValueFactory<>("operation_time"));
        surgerylist_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set data to the TableView
        surgerylist_tableView.setItems(surgeryListData);
    }
    
    
	  public void surgerySelect() {
	  // Get the selected item from the TableView
	  SurgeryData surgeryData = surgerylist_tableView.getSelectionModel().getSelectedItem();
	  int num = surgerylist_tableView.getSelectionModel().getSelectedIndex();
	
	  // Check for invalid selection
	  if ((num - 1) < -1 || surgeryData == null) {
	      return;
	  }
	  surgerylist_surgeryID.setText(String.valueOf(surgeryData.getSurgeryId()));
	}

	  public void surgerylistYesBtn() {
		    // Get the selected SurgeryData from the TableView
		    SurgeryData selectedSurgery = surgerylist_tableView.getSelectionModel().getSelectedItem();

		    // Check if a surgery is selected
		    if (selectedSurgery == null) {
		        alert.errorMessage("Please select a surgery from the table to proceed.");
		        return;
		    }

		    // SQL queries to fetch and update the status
		    String checkStatusQuery = "SELECT status FROM Surgery WHERE surgery_id = ?";
		    String updateSurgeryQuery = "UPDATE Surgery SET status = 'Complete' WHERE surgery_id = ?";
		    String updateAppointmentQuery = "UPDATE Appointment SET status_ = 'Completed' WHERE patient_id = ? AND doctor_id = ? AND status_ = 'Surgery'";

		    connect = Database.connectDB();

		    try {
		        // Check the current status of the surgery
		        prepare = connect.prepareStatement(checkStatusQuery);
		        prepare.setInt(1, selectedSurgery.getSurgeryId());
		        result = prepare.executeQuery();

		        if (result.next()) {
		            String currentStatus = result.getString("status");

		            // If the status is already 'Complete', display a message
		            if ("Complete".equalsIgnoreCase(currentStatus)) {
		                alert.errorMessage("This surgery is already marked as 'Complete'.");
		                return;
		            }

		            // If the status is 'Pending', update it to 'Complete'
		            prepare = connect.prepareStatement(updateSurgeryQuery);
		            prepare.setInt(1, selectedSurgery.getSurgeryId());
		            int surgeryRowsAffected = prepare.executeUpdate();

		            // Update the associated appointment status
		            prepare = connect.prepareStatement(updateAppointmentQuery);
		            prepare.setInt(1, selectedSurgery.getPatientId());  // Assuming SurgeryData has a `getPatientId()` method
		            prepare.setInt(2, Data.doctor_id);                 // Assuming Data.doctor_id stores the logged-in doctor's ID
		            int appointmentRowsAffected = prepare.executeUpdate();

		            // Check if both updates were successful
		            if (surgeryRowsAffected > 0 && appointmentRowsAffected > 0) {
		                // Refresh the Surgery table data
		                surgeryShowData();
		                alert.successMessage("Surgery and Appointment status updated to 'Complete' successfully!");
		            } else {
		                alert.errorMessage("Failed to update surgery or appointment status.");
		            }
		        } else {
		            alert.errorMessage("No record found for the selected surgery.");
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        alert.errorMessage("An error occurred while updating the statuses.");
		    } finally {
		        try {
		            if (result != null) result.close();
		            if (prepare != null) prepare.close();
		            if (connect != null) connect.close();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		}


    public void surgerylistNoBtn() {
        SurgeryData selectedSurgery = surgerylist_tableView.getSelectionModel().getSelectedItem();

        // Check if a surgery is selected
        if (selectedSurgery == null) {
            alert.errorMessage("Please select a surgery from the table to proceed.");
            return;
        }
        alert.errorMessage("Surgery status not updated.");
    }

	
}
