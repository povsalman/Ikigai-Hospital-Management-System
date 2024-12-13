package patientApplication;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.util.StringConverter;
//import org.controlsfx.control.Rating;
import patientController.*;
import patientModel.*;
import patientHandler.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PatientMainFormController implements Initializable{
	@FXML private TableView<Surgery> Surg_table;
	@FXML private TableColumn<Surgery, Integer> Surg_id;
	@FXML private TableColumn<Surgery, String> Surg_room, Surg_doc;
	@FXML private TableColumn<Surgery, Date> Surg_date;	
	@FXML private TableColumn<Surgery, Time> Surg_time;
	
    @FXML private ListView<String> BillList;
    @FXML private Label top_username,total_bill_amount,profile_label_gender,profile_label_mobileNumber,profile_label_name,profile_label_patientID,date_time,appointment_ad_address,appointment_ad_description,appointment_ad_doctorName,appointment_ad_gender,appointment_ad_mobileNumber,appointment_ad_name,appointment_ad_schedule,appointment_ad_specialization,home_patient_Status,home_patient_diagnosis,home_patient_startDate,home_patient_endDate,home_patient_price,home_doctor_name,home_doctor_specialization,home_doctor_email,home_doctor_mobileNumber;
    
    @FXML private TableView<PrescriptionItem> Prescrip_table;
    @FXML private TableColumn<PrescriptionItem, Double> Prescrip_table_UnitP,Prescrip_table_totalP;
    @FXML private TableColumn<PrescriptionItem, Integer> Prescrip_table_quantity,Prescrip_col_id;
    @FXML private TableColumn<PrescriptionItem, String> Prescrip_table_name,Prescrip_table_type;
    
    @FXML private TableColumn<Room, Integer> room_id;
    @FXML private TableColumn<Room, Double> room_price;
    @FXML private TableView<Room> room_table;
    @FXML private TableColumn<Room, String> room_type;

    @FXML private TableView<Appointment> home_appointment_tableView;
    @FXML private TableColumn<Appointment, Integer> home_appointment_col_appointmenID;
    @FXML private TableColumn<Appointment, String> home_appointment_col_description,home_appointment_col_doctor;
    @FXML private TableColumn<Appointment, Date> home_appointment_col_date;
    @FXML private TableColumn<Appointment, Double> home_appointment_col_price;

    @FXML private TableView<Doctor> doc_table1;
    @FXML private TableColumn<Doctor, Integer> doc_table_id;
    @FXML private TableColumn<Doctor, String> doc_table_name,doc_table_email,doc_table_gender,doc_table_phone,doc_table_specialize;

    @FXML private TextField profile_Fname,profile_Lname,profile_mobileNumber,profile_password,fb_doc_search_text,appointment_d_search,cancel_appointment_id,doc_search_text;
    @FXML private TextArea profile_address,profile_label_address,fb_description,appointment_d_description;
    @FXML private Circle profile_circle,home_doctor_circle,top_profile;
    @FXML private Button profile_importBtn,profile_updateBtn,fb_rating_btn,roomconfirm_btn,appointmentBookBtn,appointment_d_clearBtn,appointment_d_confirmBtn,cancel_appointment_btn,doc_select_btn,dashboard_btn,doctors_btn,appointments_btn,bookroom_btn,bill_btn,prescription_btn,profile_btn,feedbck_btn;
    //@FXML private Rating feedback_stars;
    @FXML private DatePicker room_book_date,appointment_d_schedule;

    @FXML private ComboBox<String> profile_gender,fb_doc_search_optn,appointment_d_search_optn,doc_search_optn;
    @FXML private ComboBox<Doctor> fb_doctor,appointment_d_doctor;
    @FXML private ComboBox<Integer> room_choose;
    @FXML private AnchorPane home_form, doctors_form,appointments_form,room_form,pres_form,profile_form,feedback_form,bill_form;

    private DashboardController dashboardController;
    private DoctorController doctorController;
    private AppointmentController  appointmentController; 
    private RoomController roomController;
    private FeedbackController feedbackController;
    private ProfileController profileController;
    private SurgeryController surgeryController;
    private BillingController billingController;
    private PrescriptionController prescriptionController; 
    private DBHandler dbHandler;
    
    @FXML
    void GiveFeedback(ActionEvent event) {
        // Get the selected doctor from the doctor ComboBox
        Doctor selectedDoctor = fb_doctor.getValue();

        // Ensure a doctor is selected and feedback description is provided
        if (selectedDoctor == null) {
            showAlert("Error", "Please select a doctor.");
            return;
        }

        String feedbackDescription = fb_description.getText().trim();
        int rating = 0;// = (int) feedback_stars.getRating(); // Rating should be between 1 and 5

        if (feedbackDescription.isEmpty()) {
            showAlert("Error", "Feedback description cannot be empty.");
            return;
        }

        if (rating <= 0) {
            showAlert("Error", "Please provide a valid rating.");
            return;
        }

        // Capture current date for feedback
        LocalDate feedbackDate = LocalDate.now();

        // Create a Feedback object with required details
        Feedback feedback = new Feedback(
            Patient.getInstance().getPatientId(),
            selectedDoctor.getDoctorId(),
            feedbackDate,
            feedbackDescription,
            rating,
            "Pending" // Default feedback status
        );

        // Submit the feedback using the FeedbackController
        boolean isSubmitted = feedbackController.submitFeedback(feedback);

        if (isSubmitted) {
            showAlert("Success", "Your feedback has been submitted successfully!");
            // Optionally, clear the input fields
            fb_description.clear();
            //feedback_stars.setRating(0); // Reset the rating
        } else {
            showAlert("Error", "Failed to submit feedback. Please try again.");
        }
    }
  
    @FXML
    void RoomConfirmBtn(ActionEvent event) {
        if (room_choose.getValue() == null || room_book_date.getValue() == null) {
            showAlert("Error", "Please select a room and a booking date.");
            return;
        }

        int roomId = room_choose.getValue();
        LocalDate bookingDate = room_book_date.getValue();
        boolean success = roomController.bookRoom(Patient.getInstance().getPatientId(), roomId, bookingDate);

        if (success) {
            showAlert("Success", "Room booked successfully!");
            roomController.loadRoomData(room_table, room_id, room_type, room_price); // Refresh room data
        } else {
            showAlert("Error", "Failed to book the room. Please try again.");
        }
    }

    
    ///////////////////////////////////////////   Appointment  //////////////////////////////////////////////////////
    @FXML
    void appointmentBookBtn(ActionEvent event) {
        appointmentController.bookAppointment(Patient.getInstance().getPatientId(),appointment_d_doctor, appointment_d_description, appointment_d_schedule, this::UpdateDashBoard);
    }
    @FXML
    void appointmentClearBtn(ActionEvent event) {
    	
		appointmentController.clearAppointment(appointment_d_description, appointment_d_doctor, home_patient_diagnosis, home_doctor_name, home_doctor_specialization, home_doctor_email, home_doctor_mobileNumber);
    }

    @FXML
    void appointmentConfirmBtn(ActionEvent event) {
        appointmentController.confirmAppointment(appointment_d_doctor, appointment_d_description, appointment_d_schedule, appointment_ad_description, appointment_ad_schedule, Patient.getInstance(), appointment_ad_name, appointment_ad_gender, appointment_ad_mobileNumber, appointment_ad_address, appointment_ad_doctorName, appointment_ad_specialization);
    }

    @FXML
    void CancelAppointment(ActionEvent event) {
        String appointmentIdText = cancel_appointment_id.getText().trim();
        // Check if the field is empty
        if (appointmentIdText.isEmpty()) {
            showAlert("Error", "Please enter an appointment ID to cancel.");
            return;
        }
        try {
            int appointmentId = Integer.parseInt(appointmentIdText);
            appointmentController.cancelAppointment(appointmentId); //deleting the appointment
            UpdateDashBoard();
        } catch (NumberFormatException e) {
            // If the text is not a valid integer
            showAlert("Error", "Please enter a valid numeric appointment ID.");
        }
    }    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void logoutBtn(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientApplication/PatientLogin.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	Stage stage = (Stage) profile_label_mobileNumber.getScene().getWindow();
        stage.close();
    }
    												
    @FXML
    void searchDoctor() {
        String searchField = appointment_d_search_optn.getValue(); // Get selected filter option
        String searchValue = appointment_d_search.getText().trim(); // Get user input

        if (searchField == null || searchValue.isEmpty()) {
            showAlert("Error", "Please select a search option and provide a search value.");
            return;
        }

        ObservableList<Doctor> filteredDoctors = doctorController.searchDoctors(searchField, searchValue);
        appointment_d_doctor.setItems(filteredDoctors);
    }
    
    @FXML
    void fbsearchDoctor() {
        String searchField = fb_doc_search_optn.getValue(); // Get selected filter option
        String searchValue = fb_doc_search_text.getText().trim(); // Get user input

        if (searchField == null || searchValue.isEmpty()) {
            showAlert("Error", "Please select a search option and provide a search value.");
            return;
        }

        ObservableList<Doctor> filteredDoctors = doctorController.searchDoctors(searchField, searchValue);
        fb_doctor.setItems(filteredDoctors);
    }
    
    @FXML
    void profileImportBtn(ActionEvent event) {
    	profileController.updateUserProfileImage(profile_circle,top_profile);
        Patient.getInstance().fetchPatientDetails(dbHandler.getConnection());
    }

    @FXML
    void profileUpdateBtn(ActionEvent event) {
        if(profileController.updateProfile(profile_Fname, profile_Lname, profile_address, profile_gender, profile_mobileNumber, profile_password)) {
            profileController.loadProfileData(profile_Fname, profile_Lname, profile_address, profile_gender, profile_mobileNumber, profile_password, profile_circle, profile_label_patientID, profile_label_name, profile_label_mobileNumber, profile_label_gender, profile_label_address,top_username,top_profile);
        }
    }

    private void loadDoctors() {
    	doctorController.loadDoctors(doc_table_id, doc_table_name,doc_table_email,doc_table_gender, doc_table_phone,doc_table_specialize,doc_table1);
    }

    @FXML
    void docSelectBtn(ActionEvent event) {
        String searchField = doc_search_optn.getValue();
        String searchValue = doc_search_text.getText().trim();

        if (searchField == null || searchValue.isEmpty()) {
            showAlert("Error", "Please select a field and enter a search value.");
            return;
        }

        ObservableList<Doctor> doctors = doctorController.searchDoctors(searchField, searchValue);
        doc_table1.setItems(doctors);
    }

    private void showAlert(String title, String content) {
		Alert alert;
		if(title.compareTo("Error") == 0) {
			alert = new Alert(Alert.AlertType.ERROR);
		   }
		else{
			alert = new Alert(Alert.AlertType.INFORMATION);
		}	
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void UpdateDashBoard() {
        dashboardController.patientTreatmentGetData(Patient.getInstance().getPatientId(), home_patient_Status, home_patient_diagnosis, home_patient_startDate, home_patient_endDate, home_patient_price);
        dashboardController.showAppointmentsInTable(home_appointment_tableView, home_appointment_col_appointmenID, home_appointment_col_description, home_appointment_col_doctor, home_appointment_col_date, home_appointment_col_price,Patient.getInstance().getPatientId());
        dashboardController.setDocDetailsHome(Patient.getInstance().getPatientId(), home_doctor_name, home_doctor_specialization, home_doctor_email, home_doctor_mobileNumber,home_doctor_circle);
    }

    
    @FXML
    void AddAppointmentToBill(ActionEvent event) {
        billingController.addAppointmentToBill(total_bill_amount, BillList);
    }

    @FXML
    void AddRoomToBill(ActionEvent event) {
        billingController.addRoomToBill(total_bill_amount, BillList);
    }

    @FXML
    void AddPrescriptionToBill(ActionEvent event) {
        billingController.addPrescriptionToBill(total_bill_amount, BillList);
    }

    @FXML
    void AddTreatmentToBill(ActionEvent event) {
        billingController.addTreatmentToBill(total_bill_amount, BillList);
    }

    @FXML
    void PayBill(ActionEvent event) {
        billingController.payBill(total_bill_amount, BillList);
    }


   
    @FXML
    void switchForm(ActionEvent event) {

        AnchorPane[] forms = {home_form, doctors_form, appointments_form, room_form, pres_form, profile_form, feedback_form,bill_form};

        for (AnchorPane form : forms) {
            form.setVisible(false);
        }

        if (event.getSource() == dashboard_btn) {
			home_form.setVisible(true);
		} else if (event.getSource() == doctors_btn) {
			doctors_form.setVisible(true);
		} else if (event.getSource() == appointments_btn) {
			appointments_form.setVisible(true);
		} else if (event.getSource() == bookroom_btn) {
			room_form.setVisible(true);
		} else if (event.getSource() == prescription_btn) {
			pres_form.setVisible(true);
		} else if (event.getSource() == profile_btn) {
			profile_form.setVisible(true);
		} else if (event.getSource() == feedbck_btn) {
			feedback_form.setVisible(true);
		} else if (event.getSource() == bill_btn) {
			bill_form.setVisible(true);
		}
    }
    
    public void showPatientPrescriptions() {
        prescriptionController.populatePrescriptionTable( Prescrip_table,Prescrip_col_id,Prescrip_table_name,Prescrip_table_quantity,Prescrip_table_type, Prescrip_table_UnitP,Prescrip_table_totalP, Patient.getInstance());
    }

    public void runTime() {
        new Thread() {
            @Override
			public void run() {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        date_time.setText(format.format(new Date()));
                    });
                }
            }
        }.start();
    }

    private void initializeCombos() {
        doc_search_optn.setItems(FXCollections.observableArrayList("Name", "Email", "Specialization", "Phone Number", "Gender"));
        appointment_d_search_optn.setItems(FXCollections.observableArrayList("Name", "Email", "Specialization", "Phone Number", "Gender"));
        fb_doc_search_optn.setItems(FXCollections.observableArrayList("Name", "Email", "Specialization", "Phone Number", "Gender"));
        profile_gender.setItems(FXCollections.observableArrayList("Female", "Male","Other"));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
        // Initialize ComboBox with search options
    	initializeCombos();
        // Database connection initialization
        try {
           
        	dbHandler = new DBHandler();
            // Pass the connection to the controllers
            dashboardController = new DashboardController();
            doctorController = new DoctorController();
            appointmentController = new AppointmentController();
            roomController = new RoomController(dbHandler.getConnection()); 
            roomController.loadRoomData(room_table, room_id, room_type, room_price);
            roomController.populateRoomComboBox(room_choose);
            feedbackController = new FeedbackController(dbHandler.getConnection());
            surgeryController = new SurgeryController(dbHandler.getConnection());
            prescriptionController = new PrescriptionController();
            billingController = new BillingController(Patient.getInstance()); // Pass connection and patient
            profileController = new ProfileController(dbHandler.getConnection(), Patient.getInstance());
            profileController.loadProfileData(profile_Fname, profile_Lname, profile_address, profile_gender, profile_mobileNumber, profile_password, profile_circle, profile_label_patientID, profile_label_name, profile_label_mobileNumber, profile_label_gender, profile_label_address,top_username,top_profile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        fb_doctor.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(doctor.getDocName()); // Display only the doctor's name
                }
            }
        });
        // Customize ComboBox to display doctor names only
        appointment_d_doctor.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(doctor.getDocName()); // Display only the doctor's name
                }
            }
        });

        fb_doctor.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : doctor.getDocName();
            }

            @Override
            public Doctor fromString(String string) {
                // Not needed for this implementation
                return null;
            }
        });

        // Set the displayed value when an item is selected
        appointment_d_doctor.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : doctor.getDocName();
            }

            @Override
            public Doctor fromString(String string) {
                // Not needed for this implementation
                return null;
            }
        });

        // Trigger search when search option changes
        appointment_d_search_optn.valueProperty().addListener((obs, oldVal, newVal) -> searchDoctor());
        surgeryController.populateSurgeryTable(Patient.getInstance().getPatientId(),Surg_table, Surg_id, Surg_room, Surg_date, Surg_time, Surg_doc);
        showPatientPrescriptions();
        
        // Load dashboard data and doctors
        UpdateDashBoard();
        loadDoctors();
        runTime();
    }
}