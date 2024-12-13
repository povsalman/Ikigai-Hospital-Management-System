package doctorController;

import doctorModel.*;
import Doctorapplication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentController {

	// Appointments
    @FXML
    private TableView<AppointmentData> appointments_tableView;
    @FXML
    private TableColumn<AppointmentData, String> appointments_col_appointmentID, appointments_col_name, appointments_col_gender, 
                                                 appointments_col_contactNumber, appointments_col_description, appointments_col_date, 
                                                 appointments_col_dateModify, appointments_col_status, appointments_col_action;
    @FXML
    private TextField appointment_appointmentID, appointment_diagnosis, appointment_treatment, appointment_time;
    @FXML
    private ComboBox<String> appointment_status;
    @FXML
    private DatePicker appointment_startdate, appointment_enddate;
    @FXML
    private Button appointment_updateBtn, appointment_clearBtn, appointment_treatmentBtn, appointment_prescriptionBtn, appointment_surgeryBtn;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private final AlertMessage alert = new AlertMessage();
    
    public void setAppointmentsComponents(
            TableView<AppointmentData> appointments_tableView,
            TableColumn<AppointmentData, String> appointments_col_appointmentID,
            TableColumn<AppointmentData, String> appointments_col_name,
            TableColumn<AppointmentData, String> appointments_col_gender,
            TableColumn<AppointmentData, String> appointments_col_contactNumber,
            TableColumn<AppointmentData, String> appointments_col_description,
            TableColumn<AppointmentData, String> appointments_col_date,
            TableColumn<AppointmentData, String> appointments_col_dateModify,
            TableColumn<AppointmentData, String> appointments_col_status,
            TableColumn<AppointmentData, String> appointments_col_action,
            TextField appointment_appointmentID,
            TextField appointment_diagnosis,
            TextField appointment_treatment,
            TextField appointment_time,
            ComboBox<String> appointment_status,
            DatePicker appointment_startdate,
            DatePicker appointment_enddate,
            Button appointment_updateBtn,
            Button appointment_clearBtn,
            Button appointment_treatmentBtn,
            Button appointment_prescriptionBtn,
            Button appointment_surgeryBtn) {

        this.appointments_tableView = appointments_tableView;
        this.appointments_col_appointmentID = appointments_col_appointmentID;
        this.appointments_col_name = appointments_col_name;
        this.appointments_col_gender = appointments_col_gender;
        this.appointments_col_contactNumber = appointments_col_contactNumber;
        this.appointments_col_description = appointments_col_description;
        this.appointments_col_date = appointments_col_date;
        this.appointments_col_dateModify = appointments_col_dateModify;
        this.appointments_col_status = appointments_col_status;
        this.appointments_col_action = appointments_col_action;
        this.appointment_appointmentID = appointment_appointmentID;
        this.appointment_diagnosis = appointment_diagnosis;
        this.appointment_treatment = appointment_treatment;
        this.appointment_time = appointment_time;
        this.appointment_status = appointment_status;
        this.appointment_startdate = appointment_startdate;
        this.appointment_enddate = appointment_enddate;
        this.appointment_updateBtn = appointment_updateBtn;
        this.appointment_clearBtn = appointment_clearBtn;
        this.appointment_treatmentBtn = appointment_treatmentBtn;
        this.appointment_prescriptionBtn = appointment_prescriptionBtn;
        this.appointment_surgeryBtn = appointment_surgeryBtn;
    }
    
    
    // Status Combobox
    public void appointmentStatusList() {
        List<String> listS = new ArrayList<>();
        for (String data : Data.status) {
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        appointment_status.setItems(listData);
    }
    
    public ObservableList<AppointmentData> appointmentGetData() {

        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        // SQL query to fetch appointment details for the current doctor with full name
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.status_, a.price, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS full_name, p.gender, p.phone_no, a.description " +
                     "FROM Appointment a " +
                     "JOIN Patient p ON p.patient_id = a.patient_id " +
                     "JOIN Doctor d ON d.doctor_id = a.doctor_id " +
                     "WHERE a.doctor_id = ?";

        connect = Database.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id);

            result = prepare.executeQuery();

            AppointmentData appData;

            while (result.next()) {
                appData = new AppointmentData(
                    result.getInt("appointment_id"),         // Appointment ID
                    result.getInt("patient_id"),            // Patient ID
                    result.getInt("doctor_id"),             // Doctor ID
                    result.getDate("appointment_date"),     // Appointment Date
                    result.getDate("appointment_date"),     // Appointment Modified Date
                    result.getString("status_"),            // Appointment Status
                    result.getBigDecimal("price"),          // Appointment Price
                    result.getString("full_name"),         // Patient's First Name
                    result.getString("gender"),             // Patient's Gender
                    result.getString("phone_no"),           // Patient's Phone Number
                    result.getString("description")     // Patient's Medical Details
                );
                listData.add(appData);
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

    
    public ObservableList<AppointmentData> appointmentListData;

    public void appointmentShowData() {
        appointmentListData = appointmentGetData();

        appointments_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointments_col_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        appointments_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));		
        appointments_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        appointments_col_description.setCellValueFactory(new PropertyValueFactory<>("medicalDetails"));
        appointments_col_date.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        // below should be set to current date if updatebtn was clicked, otherwise same as appointmentdate
        appointments_col_dateModify.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        appointments_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointments_tableView.setItems(appointmentListData);
    }

    public void appointmentSelect() {

        AppointmentData appData = appointments_tableView.getSelectionModel().getSelectedItem();
        int num = appointments_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        appointment_appointmentID.setText("" + appData.getAppointmentID());
        appointment_diagnosis.setText(appData.getDiagnosis());
        appointment_treatment.setText(appData.getTreatment());
        appointment_status.getSelectionModel().select(appData.getStatus());
        // Formatting the Date object to a string
        Date appointmentDate = appData.getAppointmentDate();
        if (appointmentDate != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); // Customize the format as needed
            appointment_startdate.setPromptText(dateFormatter.format(appointmentDate));
        } else {
            appointment_startdate.setPromptText("No Date"); 
        }

    }
    
    public void appointmentUpdateBtn() {
        if (appointment_diagnosis.getText() == null || appointment_diagnosis.getText().isEmpty() ||
            appointment_treatment.getText() == null || appointment_treatment.getText().isEmpty()) {
            alert.errorMessage("Treatment and Diagnosis fields are compulsory.");
            return;
        }

        String status = appointment_status.getSelectionModel().getSelectedItem();
        if (status == null) {
            alert.errorMessage("Please select a status.");
            return;
        }

        int appointmentID = Integer.parseInt(appointment_appointmentID.getText());

        String checkStatusQuery = "SELECT status_ FROM Appointment WHERE appointment_id = ?";
        connect = Database.connectDB();
        
        try {
            prepare = connect.prepareStatement(checkStatusQuery);
            prepare.setInt(1, appointmentID);
            result = prepare.executeQuery();

            if (result.next()) {
                String currentStatus = result.getString("status_");
                if ("Completed".equals(currentStatus)) {
                    alert.errorMessage("The status is already 'Completed' and cannot be updated.");
                    return;
                }
            }

            String updateData = "UPDATE Appointment SET status_ = ?, diagnosis = ?, treatment = ? WHERE appointment_id = ?";
            
            // Prepare the SQL update query
            prepare = connect.prepareStatement(updateData);
            prepare.setString(1, status); 
            prepare.setString(2, appointment_diagnosis.getText()); 
            prepare.setString(3, appointment_treatment.getText()); 
            prepare.setInt(4, appointmentID); 

            prepare.executeUpdate();

            // Refresh the data in the appointment table
            appointmentShowData(); // This will refresh the table data to show the updated status
//            patientTreatmentController.patientTreatmentShowData();
//            surgeryController.surgeryShowData();
//            prescriptionsController.prescriptionShowData();

            alert.successMessage("Appointment updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Failed to update the appointment.");
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    public void appointmentsurgeryBtn() {
        if (appointment_status.getSelectionModel().getSelectedItem().equals("Surgery")) {

            AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();

            if (appointment_diagnosis.getText().isEmpty() || appointment_treatment.getText().isEmpty() ||
                    appointment_startdate.getValue() == null || appointment_time.getText().isEmpty()) {
                alert.errorMessage("Please fill in the compulsory fields for Surgery status.");
                return;
            }

            if ("Completed".equals(selectedAppointment.getStatus())) {
                alert.errorMessage("This appointment's status is already 'Completed' and cannot be changed to Surgery.");
                return;
            }

            String selectRoomQuery = "SELECT room_id FROM Room WHERE availability_status = 'Available' AND room_type = 'ICU' LIMIT 1";
            String updateRoomQuery = "UPDATE Room SET availability_status = 'Occupied', patient_id = ? WHERE room_id = ?";

            connect = Database.connectDB();
            int roomID = -1;

            try {
                prepare = connect.prepareStatement(selectRoomQuery);
                result = prepare.executeQuery();
                
                if (result.next()) {
                    roomID = result.getInt("room_id");
                } else {
                    alert.errorMessage("No available ICU rooms found.");
                    return;
                }

                prepare = connect.prepareStatement(updateRoomQuery);
                prepare.setInt(1, selectedAppointment.getPatientID());  // Patient ID
                prepare.setInt(2, roomID);  // Room ID
                prepare.executeUpdate();

                int patientID = selectedAppointment.getPatientID();
                int doctorID = selectedAppointment.getDoctorID();  // Assuming you have the doctor ID in the appointment data
                LocalDate operationDateLocal = appointment_startdate.getValue(); 
                java.sql.Date operationDate = java.sql.Date.valueOf(operationDateLocal);  // Start Date
                String operationTime = appointment_time.getText();  // Operation Time
                String diagnosis = appointment_diagnosis.getText(); // Diagnosis
                String treatment = appointment_treatment.getText(); // Treatment

                String insertSurgeryData = "INSERT INTO Surgery (patient_id, doctor_id, room_id, operation_date, operation_time, status) " +
                                           "VALUES (?, ?, ?, ?, ?, ?)";

                String updateAppointmentData = "UPDATE Appointment SET status_ = 'Surgery', diagnosis = ?, treatment = ? WHERE appointment_id = ?";

                prepare = connect.prepareStatement(updateAppointmentData);
                prepare.setString(1, diagnosis);  // Diagnosis
                prepare.setString(2, treatment);  // Treatment
                prepare.setInt(3, selectedAppointment.getAppointmentID());  // Appointment ID
                prepare.executeUpdate();
                
                prepare = connect.prepareStatement(insertSurgeryData);
                prepare.setInt(1, patientID);  // Patient ID
                prepare.setInt(2, doctorID);  // Doctor ID
                prepare.setInt(3, roomID);  // Room ID (assigned ICU room)
                prepare.setDate(4, operationDate);  // Operation Date
                prepare.setString(5, operationTime);  // Operation Time
                prepare.setString(6, "Pending");  // Status (Initially Pending)
                prepare.executeUpdate();           

                // Refresh the data in both Appointment and Surgery tables
                appointmentShowData();
                /////////////////////////////////surgeryController.surgeryShowData(); // Assuming you have this method to show surgery details

                // Show success message
                alert.successMessage("Surgery details added successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("Failed to add surgery details.");
            } finally {
                try {
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            alert.errorMessage("Status must be Surgery to proceed with this action.");
        }
    }

    
    public void appointmenttreatmentBtn() {
        if (appointment_status.getSelectionModel().getSelectedItem().equals("On Treatment")) {
            
            AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
            
            if (appointment_diagnosis.getText().isEmpty() || appointment_treatment.getText().isEmpty() ||
                    appointment_startdate.getValue() == null || appointment_enddate.getValue() == null) {
                alert.errorMessage("Please fill in the compulsory fields for On Treatment status.");
                return;
            }
            
            if ("Completed".equals(selectedAppointment.getStatus())) {
                alert.errorMessage("This appointment's status is already 'Completed' and cannot be changed.");
                return;
            }

            String updateAppointmentData = "UPDATE Appointment " +
                    "SET diagnosis = ?, treatment = ?, status_ = 'On Treatment' " +
                    "WHERE appointment_id = ?";

            String updatePatientTreatmentData = "INSERT INTO Patient_Treatment (patient_id, start_date, end_date, status, price) " +
                                                "VALUES (?, ?, ?, 'Good', ?) " +
                                                "ON DUPLICATE KEY UPDATE start_date = ?, end_date = ?, status = 'Good', price = ?";

            connect = Database.connectDB();

            try {
                prepare = connect.prepareStatement(updateAppointmentData);
                prepare.setString(1, appointment_diagnosis.getText());  // Diagnosis
                prepare.setString(2, appointment_treatment.getText());  // Treatment
                prepare.setInt(3, selectedAppointment.getAppointmentID());  // Use the AppointmentData object for the Appointment ID
                prepare.executeUpdate();

                prepare = connect.prepareStatement(updatePatientTreatmentData);
                prepare.setInt(1, selectedAppointment.getPatientID());  // Use the PatientID from AppointmentData
                prepare.setDate(2, java.sql.Date.valueOf(appointment_startdate.getValue()));  // Start Date
                prepare.setDate(3, java.sql.Date.valueOf(appointment_enddate.getValue()));    // End Date
                prepare.setBigDecimal(4, selectedAppointment.getPrice()); // Use the price from AppointmentData
                prepare.setDate(5, java.sql.Date.valueOf(appointment_startdate.getValue()));  // Start Date (for update)
                prepare.setDate(6, java.sql.Date.valueOf(appointment_enddate.getValue()));    // End Date (for update)
                prepare.setBigDecimal(7, selectedAppointment.getPrice()); // Price (for update)

                prepare.executeUpdate();

                // Refresh the data in both Appointment and Patient_Treatment tables
                appointmentShowData();
                ////////////////////////////////////patientTreatmentController.patientTreatmentShowData();

                alert.successMessage("Treatment details updated successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("Failed to update treatment details.");
            } finally {
                try {
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            alert.errorMessage("Status must be On Treatment to proceed with this action.");
        }
    }


    public void appointmentprescriptionBtn(ActionEvent event) {
        AppointmentData appData = appointments_tableView.getSelectionModel().getSelectedItem();
        int num = appointments_tableView.getSelectionModel().getSelectedIndex();

        if (num == -1 || appData == null) {
            // Run the switchForm function when no item is selected
            ///////////////////////////////////////////////////////////////switchForm(event);
        } else {
            if (appointment_status.getSelectionModel().getSelectedItem().equals("Prescribed")) {
                if (appointment_diagnosis.getText().isEmpty() || appointment_treatment.getText().isEmpty()) {
                    alert.errorMessage("Diagnosis and Treatment fields are compulsory for Prescribed status.");
                    return;
                }

                String insertData = "INSERT INTO Prescription (appointment_id, patient_id, doctor_id, date_issued, status_) " +
                                    "VALUES (?, ?, ?, ?, ?)";

                String updateAppointmentStatus = "UPDATE Appointment SET status_ = 'Prescribed', " +
                                                 "diagnosis = ?, treatment = ? WHERE appointment_id = ?";

                connect = Database.connectDB();

                try {
                    int appointmentID = Integer.parseInt(appointment_appointmentID.getText());  // Appointment ID
                    int patientID = appData.getPatientID();  // Patient ID from selected appointment
                    int doctorID = Data.doctor_id;  // Doctor ID from current session
                    LocalDate dateIssuedLocal = LocalDate.now();  // Today's date for the prescription
                    java.sql.Date dateIssued = java.sql.Date.valueOf(dateIssuedLocal);
                    String diagnosis = appointment_diagnosis.getText();  // Diagnosis from input field
                    String treatment = appointment_treatment.getText();  // Treatment from input field
                    String status = "Pending";  // Default status is "Pending"

                    prepare = connect.prepareStatement(insertData);
                    prepare.setInt(1, appointmentID);  // Set appointment_id
                    prepare.setInt(2, patientID);  // Set patient_id
                    prepare.setInt(3, doctorID);  // Set doctor_id
                    prepare.setDate(4, dateIssued);  // Set date_issued
                    prepare.setString(5, status);  // Set status (Pending)
                    prepare.executeUpdate();  // Insert the prescription record

                    prepare = connect.prepareStatement(updateAppointmentStatus);
                    prepare.setString(1, diagnosis);  // Set diagnosis
                    prepare.setString(2, treatment);  // Set treatment
                    prepare.setInt(3, appointmentID);  // Set appointment_id
                    prepare.executeUpdate();  // Update the status of the appointment

                    // Refresh the appointment table and prescription table to reflect the changes
                    appointmentShowData();
                    /////////////////////////prescriptionsController.prescriptionShowData();

                    alert.successMessage("Prescription added, diagnosis and treatment updated, and Appointment status set to 'Prescribed'!");
                } catch (Exception e) {
                    e.printStackTrace();
                    alert.errorMessage("Failed to add prescription and update appointment status.");
                } finally {
                    try {
                        if (prepare != null) prepare.close();
                        if (connect != null) connect.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                alert.errorMessage("Status must be Prescribed to proceed with this action.");
            }
        }
    }

    
    // Clear Button
    public void appointmentClearBtn() {
        appointment_appointmentID.clear();
        appointment_treatment.clear();
        appointment_diagnosis.clear();
        appointment_time.clear();
        appointment_status.getSelectionModel().clearSelection();
        appointment_startdate.setValue(null);
        appointment_enddate.setValue(null);
        appointments_tableView.getSelectionModel().clearSelection();
    }
    
    

	
}
