package doctorController;

import doctorModel.PrescriptionData;
import Doctorapplication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrescriptionController {

	
	// Prescriptions
    @FXML
    private Button prescription_PIDyesBtn, prescription_PIDnoBtn;
    @FXML
    private TextField prescription_prescriptionID;
    @FXML
    private Label prescription_totalPriceLabel;
    @FXML
    private TableView<PrescriptionData> prescriptionpanel_tableView;
    @FXML
    private TableColumn<PrescriptionData, String> prescriptionpanel_col_prescriptionID, prescriptionpanel_col_patient, 
                                                   prescriptionpanel_col_treatment, prescriptionpanel_col_item, 
                                                   prescriptionpanel_col_status, prescriptionpanel_col_uprice, 
                                                   prescriptionpanel_col_quantity;
	
	private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private final AlertMessage alert = new AlertMessage();
	
    public void setPrescriptionsComponents(
            Button prescription_PIDyesBtn, Button prescription_PIDnoBtn,
            TextField prescription_prescriptionID,
            Label prescription_totalPriceLabel,
            TableView<PrescriptionData> prescriptionpanel_tableView,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_prescriptionID,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_patient,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_treatment,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_item,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_status,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_uprice,
            TableColumn<PrescriptionData, String> prescriptionpanel_col_quantity) {

        this.prescription_PIDyesBtn = prescription_PIDyesBtn;
        this.prescription_PIDnoBtn = prescription_PIDnoBtn;
        this.prescription_prescriptionID = prescription_prescriptionID;
        this.prescription_totalPriceLabel = prescription_totalPriceLabel;
        this.prescriptionpanel_tableView = prescriptionpanel_tableView;
        this.prescriptionpanel_col_prescriptionID = prescriptionpanel_col_prescriptionID;
        this.prescriptionpanel_col_patient = prescriptionpanel_col_patient;
        this.prescriptionpanel_col_treatment = prescriptionpanel_col_treatment;
        this.prescriptionpanel_col_item = prescriptionpanel_col_item;
        this.prescriptionpanel_col_status = prescriptionpanel_col_status;
        this.prescriptionpanel_col_uprice = prescriptionpanel_col_uprice;
        this.prescriptionpanel_col_quantity = prescriptionpanel_col_quantity;
    }

    public ObservableList<PrescriptionData> prescriptionGetData() {

        ObservableList<PrescriptionData> listData = FXCollections.observableArrayList();

        int patientId = Data.patient_id; // Assuming patient_id is globally accessible

        String sql = "SELECT " +
                     "    p.prescription_id, " +
                     "    CONCAT(pa.first_name, ' ', pa.last_name) AS patient, " +
                     "    a.treatment AS treatment, " +
                     "    si.item_name AS item, " +
                     "    p.status_, " +
                     "    si.unit_price AS price, " +
                     "    pi.quantity AS quantity, " +
                     "    p.date_issued, " +
                     "    (SELECT SUM(pi.quantity * si.unit_price) " +
                     "     FROM PrescriptionItem pi " +
                     "     JOIN StockItem si ON pi.item_id = si.item_id " +
                     "     WHERE pi.prescription_id = p.prescription_id) AS total_price " +
                     "FROM Prescription p " +
                     "JOIN Patient pa ON pa.patient_id = p.patient_id " +
                     "JOIN Appointment a ON a.appointment_id = p.appointment_id " +
                     "JOIN PrescriptionItem pi ON p.prescription_id = pi.prescription_id " +
                     "JOIN StockItem si ON si.item_id = pi.item_id " +
                     "JOIN MedicalSupplyDescription msd ON msd.medsupply_id = si.medsupply_id " +
                     "WHERE p.patient_id = ?";

        Connection connect = Database.connectDB(); // Assuming a Database class with a connectDB method
        PreparedStatement prepare = null;
        ResultSet result = null;

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, patientId);

            result = prepare.executeQuery();

            PrescriptionData prescriptionData;
            double total = 0;

            while (result.next()) {
            	prescriptionData = new PrescriptionData(
            		    result.getInt("prescription_id"),        // Prescription ID
            		    result.getString("patient"),             // Patient Name
            		    result.getString("treatment"),           // Treatment (from Appointment table)
            		    result.getString("item"),                // Item Name
            		    result.getString("status_"),             // Correctly fetch status_
            		    result.getDouble("price"),               // Unit Price
            		    result.getInt("quantity"),               // Quantity
            		    result.getDate("date_issued")            // Date Issued
            		);
                total = result.getDouble("total_price");    // Total price for the prescription
                listData.add(prescriptionData);
            }

            // Update the label with the total price
            prescription_totalPriceLabel.setText("Total: $" + String.format("%.2f", total));

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

   
    public ObservableList<PrescriptionData> prescriptionListData;

    public void prescriptionShowData() {
        // Fetch the data for the Prescription table
        prescriptionListData = prescriptionGetData();

        // Map PrescriptionData class fields to TableView columns
        prescriptionpanel_col_prescriptionID.setCellValueFactory(new PropertyValueFactory<>("prescriptionId"));
        prescriptionpanel_col_patient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        prescriptionpanel_col_treatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        prescriptionpanel_col_item.setCellValueFactory(new PropertyValueFactory<>("item"));
        prescriptionpanel_col_status.setCellValueFactory(new PropertyValueFactory<>("status_"));  
        prescriptionpanel_col_uprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        prescriptionpanel_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Set data to the TableView
        prescriptionpanel_tableView.setItems(prescriptionListData);
    }



    public void prescriptionSelect() {
        // Get the selected item from the TableView
        PrescriptionData prescriptionData = prescriptionpanel_tableView.getSelectionModel().getSelectedItem();
        int num = prescriptionpanel_tableView.getSelectionModel().getSelectedIndex();

        // Check for invalid selection
        if ((num - 1) < -1 || prescriptionData == null) {
            return;
        }

        // Set the prescriptionId TextField with the selected record's prescription ID
        prescription_prescriptionID.setText(String.valueOf(prescriptionData.getPrescriptionId()));
    }
    
    
    public void prescriptionconfirmYesBtn() {
        // Get the selected PrescriptionData from the TableView
        PrescriptionData selectedPrescription = prescriptionpanel_tableView.getSelectionModel().getSelectedItem();

        // Check if a prescription is selected
        if (selectedPrescription == null) {
            alert.errorMessage("Please select a prescription from the table to proceed.");
            return;
        }

        // SQL query to update the Prescription status to 'Complete'
        String updatePrescriptionQuery = "UPDATE Prescription p SET p.status_ = 'Complete' WHERE prescription_id = ?";
        
        // SQL query to update the Appointment status to 'Completed' where the status is 'Prescribed'
        String updateAppointmentQuery = "UPDATE Appointment a SET a.status_ = 'Completed' WHERE a.appointment_id = ? AND a.status_ = 'Prescribed'";

        connect = Database.connectDB();

        try {
            connect.setAutoCommit(false);

            prepare = connect.prepareStatement(updatePrescriptionQuery);
            prepare.setInt(1, selectedPrescription.getPrescriptionId()); 
            int rowsAffectedPrescription = prepare.executeUpdate();

            prepare = connect.prepareStatement(updateAppointmentQuery);
            prepare.setInt(1, selectedPrescription.getPrescriptionId()); 
            int rowsAffectedAppointment = prepare.executeUpdate();

            if (rowsAffectedPrescription > 0 && rowsAffectedAppointment > 0) {
                connect.commit(); 
                // Refresh the Prescription table data
                prescriptionShowData();
                
                alert.successMessage("Prescription status updated to 'Complete' and Appointment status updated to 'Completed'!");
            } else {
                connect.rollback();  
                alert.errorMessage("Failed to update prescription or appointment status.");
            }
        } catch (Exception e) {
            try {
                if (connect != null) {
                    connect.rollback(); 
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            alert.errorMessage("An error occurred while updating prescription and appointment status.");
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void prescriptionconfirmNoBtn() {
        PrescriptionData selectedPrescription = prescriptionpanel_tableView.getSelectionModel().getSelectedItem();
        if (selectedPrescription == null) {
            alert.errorMessage("Please select a prescription from the table to proceed.");
            return;
        }
        alert.errorMessage("No changes made. The prescription status was not updated.");
    }

    
}
