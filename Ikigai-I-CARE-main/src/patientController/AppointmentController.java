package patientController;

import patientModel.Doctor;
import patientModel.Patient;
import javafx.scene.control.*;
import java.time.LocalDate;

import patientHandler.AppointmentDBHandler;

public class AppointmentController {

    private AppointmentDBHandler dbHandler;

    public AppointmentController() {
        this.dbHandler = new AppointmentDBHandler();
    }

    public void bookAppointment(int patientId, ComboBox<Doctor> appointmentDoctorComboBox, TextArea descriptionField, DatePicker scheduleDatePicker, Runnable dashboardUpdater) {
        try {
            Doctor selectedDoctor = appointmentDoctorComboBox.getValue();
            String description = descriptionField.getText().trim();
            LocalDate appointmentDate = scheduleDatePicker.getValue();

            if (selectedDoctor == null || description.isEmpty() || appointmentDate == null) {
                showAlert("Error", "Please fill all the fields to book an appointment.");
                return;
            }
            
            //Update Database
            boolean isInserted = dbHandler.insertAppointment(patientId, selectedDoctor.getDoctorId(), description, appointmentDate, "Prescribed", 200 + Math.random() * 800);
            if (isInserted) {
                dashboardUpdater.run();
                showAlert("Success", "Appointment booked successfully!");
            } else {
                showAlert("Error", "Failed to book the appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to book the appointment.");
        }
    }

    public void confirmAppointment(ComboBox<Doctor> appointmentDoctorComboBox, TextArea descriptionField, DatePicker scheduleDatePicker,
                                   Label appointmentAdDescription, Label appointmentAdSchedule, Patient P,
                                   Label appointmentAdName, Label appointmentAdGender, Label appointmentAdMobileNumber, Label appointmentAdAddress,
                                   Label appointmentAdDoctorName, Label appointmentAdSpecialization) {

        Doctor selectedDoctor = appointmentDoctorComboBox.getValue();
        String description = descriptionField.getText().trim();
        LocalDate scheduleDate = scheduleDatePicker.getValue();

        //checking if any field is null
        if (selectedDoctor == null || description.isEmpty() || scheduleDate == null) {
            showAlert("Error", "Please select a doctor, provide a description, and choose a schedule.");
            return;
        }

        //checking if doctor is available
        if (dbHandler.isDoctorFullyBooked(selectedDoctor.getDoctorId(), scheduleDate)) {
            showAlert("Error", "The selected doctor is not available on this day as they have reached the maximum appointment limit of 50.");
            return;
        }

        // set labels
        appointmentAdDescription.setText(description);
        appointmentAdSchedule.setText(scheduleDate.toString());

        appointmentAdName.setText(P.getName());
        appointmentAdGender.setText(P.getGender());
        appointmentAdMobileNumber.setText(P.getPhoneNo());
        appointmentAdAddress.setText(P.getAddress());

        appointmentAdDoctorName.setText(selectedDoctor.getDocName());
        appointmentAdSpecialization.setText(selectedDoctor.getSpecialization());
    }

    public boolean cancelAppointment(int appointmentId) {
    	//delete from Database
        boolean isDeleted = dbHandler.deleteAppointment(appointmentId);
        if (isDeleted) {
            showAlert("Success", "Appointment deleted successfully!");
        } else {
            showAlert("Error", "Appointment not found or already deleted.");
        }
        return isDeleted;
    }

    public void clearAppointment(TextArea descriptionField, ComboBox<Doctor> doctorComboBox, Label... patientAndDoctorLabels) {
        // Clear the description field and doctor selection
        descriptionField.clear();
        doctorComboBox.setValue(null);

        // Clear all associated patient and doctor information labels
        for (Label label : patientAndDoctorLabels) {
            label.setText("");
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
