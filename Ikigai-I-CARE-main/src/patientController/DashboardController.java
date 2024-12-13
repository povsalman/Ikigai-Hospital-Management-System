package patientController;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.Date;

import patientHandler.DashBoardDBHandler;
import patientModel.*;

public class DashboardController {

    private DashBoardDBHandler dbController;

    public DashboardController() {
        this.dbController = new DashBoardDBHandler();
    }

    public void patientTreatmentGetData(int patientId, Label home_patient_Status, Label home_patient_diagnosis, Label home_patient_startDate, Label home_patient_endDate, Label home_patient_price) {
        ObservableList<PatientTreatment> treatments = dbController.getPatientTreatmentData(patientId);

        if (!treatments.isEmpty()) {
            PatientTreatment treatmentData = treatments.get(0);
            home_patient_Status.setText(treatmentData.getStatus());
            home_patient_diagnosis.setText(treatmentData.getDescription());
            home_patient_startDate.setText(treatmentData.getStartDate().toString());
            home_patient_endDate.setText(treatmentData.getEndDate().toString());
            home_patient_price.setText(Double.toString(treatmentData.getPrice()));
        }
    }
    
    public void showAppointmentsInTable(TableView<Appointment> tableView, TableColumn<Appointment, Integer> colId,TableColumn<Appointment, String> colDescription, TableColumn<Appointment, String> colDoctor,
            TableColumn<Appointment, Date> colDate, TableColumn<Appointment, Double> colPrice,int patientId) {
		
    	ObservableList<Appointment> appointments = dbController.getAppointmentsForPatient(patientId);
			
		colId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		tableView.setItems(appointments);
	}	

    public void setDocDetailsHome(int patientId, Label home_doctor_name, Label home_doctor_specialization, Label home_doctor_email, Label home_doctor_mobileNumber, Circle home_doctor_circle) {
        ObservableList<Doctor> doctorDetails = dbController.getDoctorDetails(patientId);

        if (!doctorDetails.isEmpty()) {
            Doctor details = doctorDetails.get(0);
            home_doctor_name.setText(details.getDocName());
            home_doctor_specialization.setText(details.getSpecialization());
            home_doctor_email.setText(details.getEmail());
            home_doctor_mobileNumber.setText(details.getPhone());
            if (details.getImage() != null && !details.getImage().isEmpty()) {
                Image image = new Image(details.getImage(), 128, 103, false, true);
                home_doctor_circle.setFill(new ImagePattern(image));
            }
        }
    }
}
