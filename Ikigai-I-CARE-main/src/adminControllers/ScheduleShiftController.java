package adminControllers;

import java.net.URL;
import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import adminModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ScheduleShiftController  implements Initializable{

	
	

	@FXML
	private ComboBox<String> shiftDescPicker;
	
	@FXML
    private Button logOutButton;
	
    @FXML
    private Button assignShiftButton;

    @FXML
    private Label availableDoctorsLabel;

    @FXML
    private Label currentShiftLabel;

    @FXML
    private DatePicker datePickerField;
     
    @FXML
    private TableView<Shift> shiftScheduleTableView;

    @FXML
    private TableColumn<Shift, String> shiftDocIDCol;

    @FXML
    private TableColumn<Shift, String> shiftDocName;

    @FXML
    private TableColumn<Shift, String> shiftAdminID;
    
    @FXML
    private TableColumn<Shift, String> shiftAdminNameCol;

    @FXML
    private TableColumn<Shift, String> startTimeCol;
    
    @FXML
    private TableColumn<Shift, String> endTimeCol;

    @FXML
    private TableColumn<Shift, String> dateCol;

    @FXML
    private TableColumn<Shift, String> shiftSpecializationCol;
    
    @FXML
    private TableColumn<Shift, String> shiftScheduleStatusCol;
    
    @FXML
    private TableColumn<Shift, String> shiftDescriptionCol;

   
    // Available Doctors Table and Columns
    @FXML
    private TableView<Doctor> doctorInfoTableView;

    @FXML
    private TableColumn<Doctor, String> docIDCol;

    @FXML
    private TableColumn<Doctor, String> docNameCol;

    

    @FXML
    private TableColumn<Doctor, String> docEmailCol;

    @FXML
    private TableColumn<Doctor, String> docGenderCol;

    

    @FXML
    private TableColumn<Doctor, String> docSpecCol;
    
    @FXML
    private TableColumn<Doctor, String> docShiftStatus;
    
    
   

    
   
    @FXML
    private Button doctorManagementButton;


    @FXML
    private TextField docIDTextfield;
    
    @FXML
    private TextField endTimeTextField;

    @FXML
    private Button homeButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button scheduleShiftButton;

    @FXML
    private Button scheduleWorkshopButton;

    
    @FXML
    private TextField shiftScheduleSearchTextField;

   

    @FXML
    private TextField shiftSearchTextField;


    @FXML
    private TextField startTimeTextField;

    @FXML
    private Button supplyRequestButton;
    
    
    @FXML
    private ComboBox<String> searchCriteriaComboBox;

    @FXML
    private ComboBox<String> searchCriteriaComboBox1;

    
    @FXML
    private DatePicker updateDatePickerField;

    @FXML
    private TextField updateDocIDTextfield;

    @FXML
    private Button updateShiftButton;

    @FXML
    private ComboBox<String> updateShiftDescPicker;
    
    
    private ShiftHandler shiftHandler;
    private DoctorHandler doctorHandler;

    @FXML
    void availableDocSearch(ActionEvent event) {

    }

    @FXML
    void currentShiftSearch(ActionEvent event) {

    }

    @FXML
    void homePage(ActionEvent event) {

    }

    @FXML
    void medicalSupplyRequest(ActionEvent event) {

    }

    @FXML
    void scheduleWorkshop(ActionEvent event) {

    }

    @FXML
    void shiftSchedule(ActionEvent event) {

    }

    @FXML
    void viewDoctorManagement(ActionEvent event) {

    }

    @FXML
    void viewInventory(ActionEvent event) {

    }
    
    
    
    
    public void initializeButtons()
    {
        homeButton.setOnAction(e->onHomeButton());	
		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
		inventoryButton.setOnAction(e->onInventory());
		supplyRequestButton.setOnAction(e->onSupplyRequestButton());
		doctorManagementButton.setOnAction(e->onDoctorManagement());		
        logOutButton.setOnAction(e->onLogOutButton());
        assignShiftButton.setOnAction(e->assignShift());
        updateShiftButton.setOnAction(e->updateShift());
    }
    
    private void initializeComboBox() {
	    // Populate the ComboBox with options for update criteria
    	searchCriteriaComboBox.setItems(FXCollections.observableArrayList(
    	 "Doctor ID",
    	"Admin ID",
	        "Doctor Name",
	        "Admin Name",
	        "Start Time",
	        "End Time",
	        "Date",
	        "Department",
	        "Shift Type"
	    ));
    	searchCriteriaComboBox.setPromptText("Search by");
    	
    	searchCriteriaComboBox1.setItems(FXCollections.observableArrayList(
    			 "Doctor ID",
    			 "Doctor Name",
    	        "Email",
    	        "Specialization",
    	        "Gender"
    	        
    	    ));
        	searchCriteriaComboBox1.setPromptText("Search By");
        	
        	shiftDescPicker.setItems(FXCollections.observableArrayList(
       			 "Morning shift",
       			 "Evening shift",
       	         "Night shift"     	        
       	    ));
        	
        	updateShiftDescPicker.setItems(FXCollections.observableArrayList(
          			 "Morning shift",
           			 "Evening shift",
           	         "Night shift"          	              	        
           	    ));
            	
	}
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	shiftHandler = new ShiftHandler();
        doctorHandler = new DoctorHandler();
        initializeButtons();
       
        initializeComboBox();
     // Bind table columns
        setupShiftScheduleTableColumns();
        setupAvailableDoctorsTableColumns();
        loadShiftScheduleData();
        loadAvailableDoctorsData();
    }
    
    private void setupShiftScheduleTableColumns() {
        shiftDocIDCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        shiftDocName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        shiftAdminID.setCellValueFactory(new PropertyValueFactory<>("adminId"));
        shiftAdminNameCol.setCellValueFactory(new PropertyValueFactory<>("adminFullName"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("shiftDate"));
        shiftSpecializationCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        shiftScheduleStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        shiftDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }


    private void setupAvailableDoctorsTableColumns() {
        docIDCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        docNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        docEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        docSpecCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        docGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        docShiftStatus.setCellValueFactory(new PropertyValueFactory<>("shiftStatus"));
    }

    
   
    
    private void loadShiftScheduleData() {
        ObservableList<Shift> shiftList = FXCollections.observableArrayList(shiftHandler.getAllCurrentShifts());
        FilteredList<Shift> filteredShifts = new FilteredList<>(shiftList, b -> true);

        // Add search functionality with ComboBox for criteria
        shiftScheduleSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCriteria = searchCriteriaComboBox.getValue(); // Get selected criteria
            filteredShifts.setPredicate(shift -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                switch (selectedCriteria) {
                    case "Doctor ID":
                        return shift.getDoctorId().toLowerCase().contains(lowerCaseFilter);
                    case "Admin ID":
                        return shift.getAdminId().toLowerCase().contains(lowerCaseFilter);
                    case "Doctor Name":
                        return shift.getDoctorFirstName().toLowerCase().contains(lowerCaseFilter) ||
                               shift.getDoctorLastName().toLowerCase().contains(lowerCaseFilter);
                    case "Admin Name":
                        return shift.getAdminFullName().toLowerCase().contains(lowerCaseFilter);
                    case "Start Time":
                        return shift.getStartTime().toLowerCase().contains(lowerCaseFilter);
                    case "End Time":
                        return shift.getEndTime().toLowerCase().contains(lowerCaseFilter);
                    case "Date":
                        return shift.getShiftDate().toLowerCase().contains(lowerCaseFilter);
                    case "Department":
                        return shift.getDepartment().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return false;
                }
            });
        });

        shiftScheduleTableView.setItems(filteredShifts);
    }

    private void loadAvailableDoctorsData() {
        ObservableList<Doctor> doctorList = FXCollections.observableArrayList(doctorHandler.getAvailableDoctors());
        FilteredList<Doctor> filteredDoctors = new FilteredList<>(doctorList, b -> true);

        // Add search functionality with ComboBox for criteria
        shiftSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCriteria = searchCriteriaComboBox1.getValue(); // Get selected criteria
            filteredDoctors.setPredicate(doctor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                switch (selectedCriteria) {
                    case "Doctor ID":
                        return doctor.getDoctorIdAsString().toLowerCase().contains(lowerCaseFilter);
                    case "Doctor Name":
                        return doctor.getFullName().toLowerCase().contains(lowerCaseFilter);
                    case "Email":
                        return doctor.getEmail().toLowerCase().contains(lowerCaseFilter);
                    case "Specialization":
                        return doctor.getSpecialization().toLowerCase().contains(lowerCaseFilter);
                    case "Gender":
                        return doctor.getGender().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return false;
                }
            });
        });

        doctorInfoTableView.setItems(filteredDoctors);
    }

   

  
    private void assignShift() {
        try {
            // Step 1: Validate inputs
            String doctorIdText = docIDTextfield.getText();
            String shiftType = shiftDescPicker.getValue();
            LocalDate selectedDate = datePickerField.getValue();

            if (doctorIdText == null || doctorIdText.isEmpty()) {
                showAlert("Error", "Please enter a valid Doctor ID.");
                return;
            }
            if (shiftType == null || shiftType.isEmpty()) {
                showAlert("Error", "Please select a shift type.");
                return;
            }
            if (selectedDate == null) {
                showAlert("Error", "Please select a date.");
                return;
            }

            // Step 2: Check if the selected date is today or in the future
            LocalDate today = LocalDate.now();
            if (selectedDate.isBefore(today)) {
                showAlert("Error", "The selected date cannot be in the past. Please choose today or a future date.");
                return;
            }

            // Step 3: Set shift times based on the selected shift type
            String startTime;
            String endTime;
            switch (shiftType) {
                case "Morning shift":
                    startTime = "09:00";
                    endTime = "17:00";
                    break;
                case "Evening shift":
                    startTime = "17:00";
                    endTime = "01:00";
                    break;
                case "Night shift":
                    startTime = "23:00";
                    endTime = "07:00";
                    break;
                default:
                    showAlert("Error", "Invalid shift type selected.");
                    return;
            }

            // Parse doctor ID
            int doctorId;
            try {
                doctorId = Integer.parseInt(doctorIdText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Doctor ID must be a valid number.");
                return;
            }

            // Step 4: Check if the doctor has a record in the Shift table
            String currentStatus = shiftHandler.getShiftStatusForDoctor(doctorId);

            // Get Admin ID
            int adminId = AdminModel.getInstance().getLoggedInAdminId();

            boolean success;
            if (currentStatus == null) {
                // No record exists - Insert new shift
                success = shiftHandler.insertShift(
                    doctorId, adminId, selectedDate.toString(), shiftType, startTime, endTime, "Active"
                );
            } else if (currentStatus.equalsIgnoreCase("Available")) {
                // Record exists with 'Available' status - Update shift
                success = shiftHandler.updateShiftForAssignShift(
                    doctorId, adminId, selectedDate.toString(), shiftType, startTime, endTime, "Active"
                );
            } else {
                // Shift is already active, cannot assign
                showAlert("Error", "Shift cannot be assigned because the doctor is already assigned an active shift.");
                return;
            }

            // Step 5: Show result alerts
            if (success) {
                showAlert("Success", "Shift assigned successfully!");
                loadShiftScheduleData(); // Refresh the schedule table
                loadAvailableDoctorsData(); // Refresh available doctors table
            } else {
                showAlert("Error", "Failed to assign shift. Please try again.");
            }
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    private void updateShift() {
        try {
            // Step 1: Validate inputs
            String doctorIdText = updateDocIDTextfield.getText();
            String shiftType = updateShiftDescPicker.getValue();
            LocalDate selectedDate = updateDatePickerField.getValue();

            if (doctorIdText == null || doctorIdText.trim().isEmpty()) {
                showAlert("Error", "Please enter a valid Doctor ID.");
                return;
            }
            if (shiftType == null || shiftType.trim().isEmpty()) {
                showAlert("Error", "Please select a shift type.");
                return;
            }
            if (selectedDate == null) {
                showAlert("Error", "Please select a valid date.");
                return;
            }

            // Step 2: Check if the selected date is in the past
            LocalDate today = LocalDate.now();
            if (selectedDate.isBefore(today)) {
                showAlert("Error", "The selected date cannot be in the past.");
                return;
            }

            int doctorId;
            try {
                doctorId = Integer.parseInt(doctorIdText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Doctor ID must be a valid number.");
                return;
            }

            // Step 3: Validate existing shift status
            boolean isShiftAssigned = shiftHandler.isShiftAssignedForDoctor(doctorId, selectedDate.toString());
            if (!isShiftAssigned) {
                showAlert("Error", "The selected doctor is not assigned any shift on the given date.");
                return;
            }

            boolean isShiftActive = shiftHandler.isShiftActiveForDoctor(doctorId, selectedDate.toString());
            if (!isShiftActive) {
                showAlert("Error", "The selected doctor's shift is not active. Updates are only allowed for active shifts.");
                return;
            }

            // Step 4: Check for conflicting appointments or surgeries
            List<String> conflicts = shiftHandler.checkDoctorEventsForDate(doctorId, selectedDate.toString());
            if (!conflicts.isEmpty()) {
                StringBuilder conflictMessage = new StringBuilder("Conflicts found:\n");
                for (String conflict : conflicts) {
                    conflictMessage.append("- ").append(conflict).append("\n");
                }
                conflictMessage.append("Shift update cannot proceed due to existing appointments or surgeries.");
                showAlert("Conflict Detected", conflictMessage.toString());
                return;
            }

            // Step 5: Determine shift times based on the shift type
            String startTime;
            String endTime;
            switch (shiftType) {
                case "Morning shift":
                    startTime = "09:00";
                    endTime = "17:00";
                    break;
                case "Evening shift":
                    startTime = "17:00";
                    endTime = "01:00";
                    break;
                case "Night shift":
                    startTime = "23:00";
                    endTime = "07:00";
                    break;
                default:
                    showAlert("Error", "Invalid shift type selected.");
                    return;
            }

            // Step 6: Update the shift
            int adminId = AdminModel.getInstance().getLoggedInAdminId();
            boolean success = shiftHandler.updateShift(doctorId, adminId, selectedDate.toString(), shiftType, startTime, endTime, "Active");

            // Step 7: Notify admin of the result
            if (success) {
                showAlert("Success", "Shift updated successfully!");
                loadShiftScheduleData(); // Refresh the schedule table
            } else {
                showAlert("Error", "Failed to update shift. Please try again.");
            }
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    private void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
    private void onLogOutButton() {
		//closing the stage
		Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showLoginWindow();
	}
	
	private void onHomeButton() {
	    Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
	    stage.close(); // Close the current stage
	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
	}

	private void onSupplyRequestButton() {
	    Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
	    stage.close(); // Close the current stage
	    AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest(); // Open the dashboard
	}

	
	private void onDoctorManagement() {
		//closing the stage
		Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showDoctorManagement();
	}
	
	
	
	private void onScheduleShiftButton() {
		//closing the stage
		Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleShift();
	}
	
	private void onScheduleWorkshopButton(){
	
		Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
				
	}
	private void onInventory(){
		
		Stage stage = (Stage) availableDoctorsLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showInventory();
				
	}
	

}
