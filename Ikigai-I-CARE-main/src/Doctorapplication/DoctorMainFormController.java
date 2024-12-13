package Doctorapplication;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import doctorController.*;
import doctorHandler.*;
import doctorModel.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class DoctorMainFormController implements Initializable {

    // General UI Elements
    @FXML
    private AnchorPane main_form;
    @FXML
    private Circle top_profile;
    @FXML
    private Label top_username, date_time, current_form, nav_docID, nav_username;
    @FXML
    private Button logout_btn, dashboard_btn, patients_btn, appointments_btn, profile_btn, workshops_btn, shiftsschedule_btn;

    // Dashboard
    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private Label dashboard_IP, dashboard_TP, dashboard_AP, dashboard_tA;
    @FXML
    private AreaChart<?, ?> dashboad_chart_PD;
    @FXML
    private BarChart<?, ?> dashboad_chart_DD;
    @FXML
    private TableView<AppointmentData> dashboad_tableView;
    @FXML
    private TableColumn<AppointmentData, String> dashboad_col_appointmentID, dashboad_col_name, dashboad_col_description, 
                                                 dashboad_col_appointmentDate, dashboad_col_status;

    // Appointments
    @FXML
    private AnchorPane appointments_form;
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

    // Profile
    @FXML
    private AnchorPane profile_form;
    @FXML
    private Circle profile_circleImage;
    @FXML
    private Label profile_label_doctorID, profile_label_name, profile_label_email, profile_label_totalRating;
    @FXML
    private TextField profile_doctorID, profile_name, profile_email, profile_mobileNumber, profile_currentpass, profile_newpass;
    @FXML
    private ComboBox<String> profile_gender, profile_specialized;
    @FXML
    private Button profile_importBtn, profile_updateBtn, profile_showfeedbacksBtn;

    // Patients
    @FXML
    private AnchorPane patients_form;
    @FXML
    private ComboBox<String> patients_gender;
    @FXML
    private Button patienttreatment_yesBtn, patienttreatment_noBtn;
    @FXML
    private TextField patienttreatment_treatmentID, patienttreatment_newtreatment;
    @FXML
    private TableView<PatientTreatmentData> patienttreatment_tableView;
    @FXML
    private TableColumn<PatientTreatmentData, String> patienttreatment_col_treatmentID, patienttreatment_col_patient, 
                                                      patienttreatment_col_treatment, patienttreatment_col_startdate, 
                                                      patienttreatment_col_exenddate, patienttreatment_col_status;

    // Surgeries
    @FXML
    private Button surgerylist_yesBtn, surgerylist_noBtn;
    @FXML
    private TextField surgerylist_surgeryID;
    @FXML
    private TableView<SurgeryData> surgerylist_tableView;
    @FXML
    private TableColumn<SurgeryData, String> surgerylist_col_surgeryID, surgerylist_col_patient, surgerylist_col_diagnosis, 
                                              surgerylist_col_room, surgerylist_col_date, surgerylist_col_time, surgerylist_col_status;

    // Workshops
    @FXML
    private AnchorPane workshops_form;
    @FXML
    private Button workshop_yesBtn, workshop_noBtn;
    @FXML
    private TableView<WorkshopData> workshops_tableView;
    @FXML
    private TableColumn<WorkshopData, Integer> workshops_col_workshopID;
    @FXML
    private TableColumn<WorkshopData, String> workshops_col_topic, workshops_col_speaker, workshops_col_description, workshops_col_date, 
                                               workshops_col_location, workshops_col_starttime, workshops_col_endtime, 
                                               workshops_col_status, workshops_col_confirm;
    @FXML
    private TextField workshop_workshopID;

    // Shift Schedules
    @FXML
    private AnchorPane shiftsschedule_form;
    @FXML
    private Button shiftsschedule_yesBtn, shiftsschedule_noBtn;
    @FXML
    private TextField shiftsschedule_shiftID;
    @FXML
    private TableView<ShiftsData> shiftsschedule_tableView;
    @FXML
    private TableColumn<ShiftsData, Integer> shiftsschedule_col_shiftID;
    @FXML
    private TableColumn<ShiftsData, String> shiftsschedule_col_doctor, shiftsschedule_col_description, shiftsschedule_col_date, 
                                             shiftsschedule_col_ward, shiftsschedule_col_starttime, shiftsschedule_col_endtime, 
                                             shiftsschedule_col_status;

    // Feedbacks
    @FXML
    private AnchorPane feedbacks_form;
    @FXML
    private Button feedbacks_yesBtn, feedbacks_noBtn;
    @FXML
    private TextField feedbacks_feedbackID;
    @FXML
    private TableView<FeedbackData> feedbacks_tableView;
    @FXML
    private TableColumn<FeedbackData, Integer> feedbacks_col_feedbackID;
    @FXML
    private TableColumn<FeedbackData, String> feedbacks_col_patient, feedbacks_col_treatmentreceived, feedbacks_col_rating, 
                                               feedbacks_col_description, feedbacks_col_date, feedbacks_col_status;

    // Prescriptions
    @FXML
    private AnchorPane prescription_form;
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

	
    // Medical Inventory
	@FXML private Button prescription_IIDyesBtn, prescription_IIDnoBtn;
	@FXML private TextField prescription_itemID;
	@FXML private TableView<MedicalInventoryData> medicalinventory_tableView;
	@FXML private TableColumn<MedicalInventoryData, String> 
	    medicalinventory_col_itemID, 
	    medicalinventory_col_item, 
	    medicalinventory_col_type, 
	    medicalinventory_col_price, 
	    medicalinventory_col_stock, 
	    medicalinventory_col_status;

	
	// ProfileController instance
    private ProfileController profileController;
    
    // WorkshopController instance
    private WorkshopController workshopController;
    
    // ShiftsController instance
    private ShiftsController shiftsController;
	
    // Initialize FeedbackController
    private FeedbackController feedbackController;
    
	// Initialize SurgeryController
    private SurgeryController surgeryController;
    
    // Initialize PatientTreatmentController
    private PatientTreatmentController patientTreatmentController;
    
    // Initialize MedicalInventoryController
    private MedicalInventoryController medicalInventoryController;
    
    // Initialize PrescriptionController
    private PrescriptionController prescriptionsController;
    
    // Initialize AppointmentController
    private AppointmentController appointmentController;

	
    
    
	
	//Database Tools
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;
    private final AlertMessage alert = new AlertMessage();
    
    
    ///////////////////////// Dashboard Page ////////////////////////////
    
    public void dashbboardDisplayIP() {
        String sql = "SELECT COUNT(patient_id) AS inactivePatients " +
                     "FROM Appointment " +
                     "WHERE status_ = 'Completed' AND doctor_id = ?";
        connect = Database.connectDB();
        int getIP = 0;

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            if (result.next()) {
                getIP = result.getInt("inactivePatients");
            }
            dashboard_IP.setText(String.valueOf(getIP));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    public void dashbboardDisplayTP() {
        String sql = "SELECT COUNT(patient_id) AS totalPatients " +
                     "FROM Patient " +
                     "WHERE EXISTS (SELECT 1 FROM Appointment WHERE Patient.patient_id = Appointment.patient_id AND doctor_id = ?)";
        connect = Database.connectDB();
        int getTP = 0;

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            if (result.next()) {
                getTP = result.getInt("totalPatients");
            }
            dashboard_TP.setText(String.valueOf(getTP));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void dashbboardDisplayAP() {
        String sql = "SELECT COUNT(appointment_id) AS activeAppointments " +
                     "FROM Appointment " +
                     "WHERE status_ IN ('On Treatment', 'Surgery', 'Prescribed') AND doctor_id = ?";
        connect = Database.connectDB();
        int getAP = 0;

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            if (result.next()) {
                getAP = result.getInt("activeAppointments");
            }
            dashboard_AP.setText(String.valueOf(getAP));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    public void dashbboardDisplayTA() {
        String sql = "SELECT COUNT(appointment_id) AS totalAppointments " +
                     "FROM Appointment " +
                     "WHERE doctor_id = ?";
        connect = Database.connectDB();
        int getTA = 0;

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            if (result.next()) {
                getTA = result.getInt("totalAppointments");
            }
            dashboard_tA.setText(String.valueOf(getTA));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    
    // Utility method to close resources
    private void closeResources() {
        try {
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<AppointmentData> dashboardAppointmentTableView() {

        ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointment WHERE doctor = '"
                + Data.doctor_id + "'";

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            AppointmentData aData;
            while (result.next()) {
                aData = new AppointmentData(result.getInt("appointment_id"),
                        result.getString("name"), result.getString("description"),
                        result.getDate("date"), result.getString("status"));

                listData.add(aData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<AppointmentData> dashboardGetData;

    public void dashboardDisplayData() {
    	
    	// Fetch the data for the Appointment table
        dashboardGetData = appointmentController.appointmentGetData();

        
        dashboad_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        dashboad_col_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        dashboad_col_description.setCellValueFactory(new PropertyValueFactory<>("medicalDetails"));
        dashboad_col_appointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        dashboad_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        dashboad_tableView.setItems(dashboardGetData);
    }

    
    public void dashboardNOP() {
        // Clear existing data from the chart
        dashboad_chart_PD.getData().clear();

        // SQL query to count patients grouped by their booking date
        String sql = "SELECT DATE(appointment_date) AS appointmentDate, COUNT(patient_id) AS totalPatients " +
                     "FROM Appointment " +
                     "WHERE doctor_id = ? " +
                     "GROUP BY DATE(appointment_date) " +
                     "ORDER BY appointmentDate ASC " +
                     "LIMIT 8";

        connect = Database.connectDB();

        try {
            // Create a new chart series
            XYChart.Series chart = new XYChart.Series<>();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            // Populate the chart with data from the result set
            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString("appointmentDate"), result.getInt("totalPatients")));
            }

            // Add the series to the chart
            dashboad_chart_PD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void dashboardNOA() {
        // Clear existing data from the chart
        dashboad_chart_DD.getData().clear();

        // SQL query to count appointments grouped by their date
        String sql = "SELECT DATE(appointment_date) AS appointmentDate, COUNT(appointment_id) AS totalAppointments " +
                     "FROM Appointment " +
                     "WHERE doctor_id = ? " +
                     "GROUP BY DATE(appointment_date) " +
                     "ORDER BY appointmentDate ASC " +
                     "LIMIT 7";

        connect = Database.connectDB();

        try {
            // Create a new chart series
            XYChart.Series chart = new XYChart.Series<>();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Data.doctor_id); 
            result = prepare.executeQuery();

            // Populate the chart with data from the result set
            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString("appointmentDate"), result.getInt("totalAppointments")));
            }

            // Add the series to the chart
            dashboad_chart_DD.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    
    //////////////////////// Appointments Page ///////////////////////////////
    
    // Method to handle selection
    @FXML
    public void appointmentSelect() {
        appointmentController.appointmentSelect();
    }

    // Method to handle clearing form fields
    @FXML
    public void appointmentClearBtn() {
        appointmentController.appointmentClearBtn();
    }

    // Method to handle generating a prescription
    @FXML
    public void appointmentprescriptionBtn(ActionEvent event) {
        appointmentController.appointmentprescriptionBtn(event);
    }

    // Method to handle treatment functionality
    @FXML
    public void appointmenttreatmentBtn() {
        appointmentController.appointmenttreatmentBtn();
    }

    // Method to handle scheduling surgery
    @FXML
    public void appointmentsurgeryBtn() {
        appointmentController.appointmentsurgeryBtn();
    }

    // Method to handle updating an appointment
    @FXML
    public void appointmentUpdateBtn() {
        appointmentController.appointmentUpdateBtn();
    }

    

    
    ////////////////////////// Workshop Page ///////////////////////////////////

    // Method to handle selection
    @FXML
    public void workshopSelect() {
        workshopController.workshopSelect();
    }

    // Method to handle "Yes" button (register)
    @FXML
    public void workshopYesBtn() {
        workshopController.workshopYesBtn();
    }

    // Method to handle "No" button (unregister)
    @FXML
    public void workshopNoBtn() {
        workshopController.workshopNoBtn();
    }
    
    
    //////////////////// Shift Schedule Page /////////////////////////////////

    @FXML
    public void shiftSelect() {
        shiftsController.shiftSelect();
    }

    @FXML
    public void shiftscheduleYesBtn() {
        shiftsController.shiftscheduleYesBtn();
    }

    @FXML
    public void shiftscheduleNoBtn() {
        shiftsController.shiftscheduleNoBtn();
    }

    
    ////////////////////// Patient Panel that holds   /////////////////////////////////////
    ////////////////////// Patient Treatment Plan Page ////////////////////////////////////

    @FXML
    public void patientTreatmentSelect() {
    	patientTreatmentController.patientTreatmentSelect();
    }
    
    @FXML
	public void patienttreatmentYesBtn() {
    	patientTreatmentController.patienttreatmentYesBtn();
    }
	
    @FXML
	public void patienttreatmentNoBtn() {
    	patientTreatmentController.patienttreatmentNoBtn();
	}

    
    /////////////////// Patient Panel that holds  ///////////////////
    /////////////////// Surgery List Table //////////////////////////
    
    @FXML
    public void surgerySelect() {
    	surgeryController.surgerySelect();
    }
    
    @FXML
	public void surgerylistYesBtn() {
    	surgeryController.surgerylistYesBtn();
    }
	
    @FXML
	public void surgerylistNoBtn() {
		surgeryController.surgerylistNoBtn();
	}


    //////////////////// Prescription Panel //////////////////////////
    // Prescription Table resides here
    
    /////////////////// Prescription Table //////////////////////////

    
    
    
    
    
    
    


    ////////////////// Prescription Panel //////////////////////////////////
    ////////////////// Medical Inventory Table //////////////////////////////
    
    @FXML
    public void medicalInventorySelect() {
    	medicalInventoryController.medicalInventorySelect();
    }
    
    @FXML
	public void prescriptionaddtoYesBtn() {
    	medicalInventoryController.prescriptionaddtoYesBtn();
    }
	
    @FXML
	public void prescriptionaddtoNoBtn() {
		medicalInventoryController.prescriptionaddtoNoBtn();
	}

    
    
    @FXML
    public void prescriptionSelect() {
    	prescriptionsController.prescriptionSelect();
    }
    
    @FXML
	public void prescriptionconfirmYesBtn() {
    	prescriptionsController.prescriptionconfirmYesBtn();
    }
	
    @FXML
	public void prescriptionconfirmNoBtn() {
		prescriptionsController.prescriptionconfirmNoBtn();
	}
    
   
    
    ///////////////////// Feedback Table /////////////////////////////
    
    @FXML
    public void feedbackSelect() {
    	feedbackController.feedbackSelect();
    }
    
    @FXML
	public void feedbackYesBtn() {
    	feedbackController.feedbackYesBtn();
    }
	
    @FXML
	public void feedbackNoBtn() {
		feedbackController.feedbackNoBtn();
	}
    
    ///////////////////// Profile Page /////////////////////////////
    
    
	 @FXML
    public void profileUpdateBtn() {
        profileController.profileUpdateBtn();
    }

    @FXML
    public void profileChangeProfile() {
        profileController.profileChangeProfile();
    }


    ///////////////// Forms Switching ///////////////////////
    
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == patients_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == appointments_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(true);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == profile_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(true);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == workshops_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(true);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == shiftsschedule_btn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(true);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == profile_showfeedbacksBtn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(true);
            prescription_form.setVisible(false);
        } else if (event.getSource() == appointment_prescriptionBtn) {
            dashboard_form.setVisible(false);
            patients_form.setVisible(false);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(true);
        } else if (event.getSource() == appointment_surgeryBtn) {
        	dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        } else if (event.getSource() == appointment_treatmentBtn) {
        	dashboard_form.setVisible(false);
            patients_form.setVisible(true);
            appointments_form.setVisible(false);
            workshops_form.setVisible(false);
            shiftsschedule_form.setVisible(false);
            profile_form.setVisible(false);
            feedbacks_form.setVisible(false);
            prescription_form.setVisible(false);
        }
        
    }

    public void logoutBtn() {

        try {
            if (alert.confirmationMessage("Are you sure you want to logout?")) {
                Data.doctor_id = 0;
                Data.doctor_name = "";
                Parent root = FXMLLoader.load(getClass().getResource("/Doctorapplication/DoctorLogin.fxml"));
                Stage stage = new Stage();

                stage.setScene(new Scene(root));
                stage.show();

                // TO HIDE YOUR MAIN FORM
                logout_btn.getScene().getWindow().hide();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void runTime() {
        new Thread() {
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

    
    
    
    
    
    
    
    
    /////////////// Main Starts /////////////////////////
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime();						// clock
       
        
        // Appointments data on dashboard
        appointmentController = new AppointmentController();
        appointmentController.setAppointmentsComponents(
            appointments_tableView, appointments_col_appointmentID, appointments_col_name, appointments_col_gender,
            appointments_col_contactNumber, appointments_col_description, appointments_col_date, appointments_col_dateModify,
            appointments_col_status, appointments_col_action, appointment_appointmentID, appointment_diagnosis, 
            appointment_treatment, appointment_time, appointment_status, appointment_startdate, appointment_enddate, 
            appointment_updateBtn, appointment_clearBtn, appointment_treatmentBtn, appointment_prescriptionBtn, appointment_surgeryBtn);
        
        appointmentController.appointmentShowData();
        appointmentController.appointmentStatusList();
        
        
        //Dashboard cards
        dashbboardDisplayIP();
        dashbboardDisplayTP();
        dashbboardDisplayAP();
        dashbboardDisplayTA();
        dashboardDisplayData();
        //Dashboard charts
        dashboardNOP();
        dashboardNOA();
       
        
        //Patient Treatment List (Patient page)
        patientTreatmentController = new PatientTreatmentController();
        patientTreatmentController.setPatientTreatmentComponents(
                patienttreatment_treatmentID, patienttreatment_newtreatment, patienttreatment_tableView,
                patienttreatment_col_treatmentID, patienttreatment_col_patient, patienttreatment_col_treatment,
                patienttreatment_col_startdate, patienttreatment_col_exenddate, patienttreatment_col_status);
        patientTreatmentController.patientTreatmentShowData();
        
        //Surgery List (Patient page)
        surgeryController = new SurgeryController();
        surgeryController.setSurgeryListComponents(surgerylist_surgeryID, surgerylist_tableView, surgerylist_col_surgeryID, 
                                                   surgerylist_col_patient, surgerylist_col_diagnosis, surgerylist_col_room, 
                                                   surgerylist_col_date, surgerylist_col_time, surgerylist_col_status);
        surgeryController.surgeryShowData();


        //Prescription Panel data
        prescriptionsController = new PrescriptionController();
        prescriptionsController.setPrescriptionsComponents(
            prescription_PIDyesBtn, prescription_PIDnoBtn, 
            prescription_prescriptionID, prescription_totalPriceLabel, prescriptionpanel_tableView, 
            prescriptionpanel_col_prescriptionID, prescriptionpanel_col_patient, 
            prescriptionpanel_col_treatment, prescriptionpanel_col_item, 
            prescriptionpanel_col_status, prescriptionpanel_col_uprice, prescriptionpanel_col_quantity
        );
        prescriptionsController.prescriptionShowData();

        
        
        //Medical Inventory Table data
        medicalInventoryController = new MedicalInventoryController();
        medicalInventoryController.setMedicalInventoryComponents(
            prescription_IIDyesBtn, prescription_IIDnoBtn, prescription_itemID,
            medicalinventory_tableView, medicalinventory_col_itemID, medicalinventory_col_item,
            medicalinventory_col_type, medicalinventory_col_price, medicalinventory_col_stock,
            medicalinventory_col_status
        );
        medicalInventoryController.medicalInventoryShowData();
        //prescriptionsController.prescriptionShowData();
        
        //Feedback Inventory Table data
        feedbackController = new FeedbackController();
        feedbackController.setFeedbackComponents(feedbacks_feedbackID, feedbacks_tableView, feedbacks_col_feedbackID, 
                feedbacks_col_patient, feedbacks_col_treatmentreceived, feedbacks_col_rating, 
                feedbacks_col_description, feedbacks_col_date, feedbacks_col_status);

        feedbackController.feedbackShowData();
        
        //Workshop Page
//        workshopController = new WorkshopController();
//        workshopController.setWorkshopComponents(workshops_tableView, workshops_col_workshopID, workshops_col_topic, 
//                                                 workshops_col_speaker, workshops_col_description, workshops_col_date, 
//                                                 workshops_col_location, workshops_col_starttime, workshops_col_endtime, 
//                                                 workshops_col_status, workshops_col_confirm, workshop_workshopID);
//        workshopController.workshopShowData();
        
        
        // Workshop Page
        WorkshopHandler workshopHandler = new WorkshopHandler(DBHandler.getInstance()); // DAO instance with DB connection
        workshopController = new WorkshopController(workshopHandler); // Controller initialized with the DAO
        workshopController.setWorkshopComponents(workshops_tableView, workshops_col_workshopID, workshops_col_topic, 
											      workshops_col_speaker, workshops_col_description, workshops_col_date, 
											      workshops_col_location, workshops_col_starttime, workshops_col_endtime, 
											      workshops_col_status, workshops_col_confirm, workshop_workshopID);
		workshopController.workshopShowData();

        
        
        
        // Shifts Page
//        shiftsController = new ShiftsController();
//        shiftsController.setShiftsComponents(shiftsschedule_tableView, shiftsschedule_col_shiftID, shiftsschedule_col_doctor, 
//                                             shiftsschedule_col_ward, shiftsschedule_col_description, shiftsschedule_col_date, 
//                                             shiftsschedule_col_starttime, shiftsschedule_col_endtime, shiftsschedule_col_status, 
//                                             shiftsschedule_shiftID);
//        shiftsController.shiftShowData();
        
		// Shifts Page
        ShiftsHandler shiftsHandler = new ShiftsHandler(DBHandler.getInstance());
        shiftsController = new ShiftsController(shiftsHandler);
        shiftsController.setShiftsComponents(shiftsschedule_tableView, shiftsschedule_col_shiftID, shiftsschedule_col_doctor, 
							                shiftsschedule_col_ward, shiftsschedule_col_description, shiftsschedule_col_date, 
							                shiftsschedule_col_starttime, shiftsschedule_col_endtime, shiftsschedule_col_status, 
							                shiftsschedule_shiftID);
		shiftsController.shiftShowData();

                  
        
        //Profile page
//        profileController = new ProfileController();
//        profileController.setProfileComponents(profile_name, profile_email, profile_mobileNumber, profile_currentpass,
//                                               profile_newpass, profile_doctorID, profile_gender, profile_specialized, 
//                                               profile_circleImage, top_profile, profile_label_doctorID, profile_label_name, 
//                                               profile_label_email, profile_label_totalRating, top_username, nav_username, 
//                                               nav_docID, profile_importBtn);
//
//        profileController.profileGenderList();			// gender combobox
//        profileController.profileSpecializedList();		// specialization combobox
//        profileController.profileFields();				// autofills textfields in profile page
//        profileController.profileLabels();				// labels on left in profile page
//        profileController.profileDisplayImages();		// Profile picture display at both top bar and profile page
//        profileController.displayDoctorIDNumberName();	// Display name,ID
        
        //Profile page
		// Profile Page Integration
		ProfileHandler profileHandler = new ProfileHandler(DBHandler.getInstance());

		// Load doctor profile data into a ProfileData object
		ProfileData profileData = profileHandler.getProfileData(ProfileData.getInstance().getDoctorId());

		// Initialize ProfileController with ProfileHandler
		profileController = new ProfileController(profileHandler);

		// Set all profile-related UI components
		profileController.setProfileComponents(profile_name, profile_email, profile_mobileNumber, profile_currentpass,
		                                        profile_newpass, profile_doctorID, profile_gender, profile_specialized, 
		                                        profile_circleImage, top_profile, profile_label_doctorID, profile_label_name, 
		                                        profile_label_email, profile_label_totalRating, top_username, nav_username, 
		                                        nav_docID, profile_importBtn);

		// Populate gender combobox
		profileController.profileGenderList(); // Fill gender options (e.g., Male, Female, Other)

		// Populate specialization combobox
		profileController.profileSpecializedList(); // Fill specialization options (e.g., Cardiologist, Dermatologist)

		// Populate profile fields using ProfileData
		profileController.populateProfileFields(profileData); // A new method for autofilling text fields in ProfileController

		// Update labels on the left in the profile page using ProfileData
		profileController.populateProfileLabels(profileData); // A new method for labels in ProfileController

		// Display profile pictures
		profileController.profileDisplayImages(profileData); // Updated to handle images from ProfileData

		// Display the doctor's full name and ID in the navigation area
		profileController.displayDoctorIDAndName(profileData); // Updated for navigation bar updates

        
       
        

    }

}
