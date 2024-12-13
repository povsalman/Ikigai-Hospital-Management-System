package adminControllers;

import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class AdminDoctorManagementController implements Initializable{

	
	 @FXML
	private ComboBox<String> searchCriteriaComboBox;
	 
	 @FXML
	 private ComboBox<String> updateCriteriaComboBox;

	
    @FXML
    private Button addDocButton;

    @FXML
    private TableView<Doctor> doctorInfoTableView;

    @FXML
    private TableColumn<Doctor, String> docIDCol;

    @FXML
    private TableColumn<Doctor, String> docFirstNameCol;

    @FXML
    private TableColumn<Doctor, String> docLastNameCol;

    @FXML
    private TableColumn<Doctor, String> docEmailCol;

    @FXML
    private TableColumn<Doctor, String> docSpecCol;

    @FXML
    private TableColumn<Doctor, String> docGenderCol;

    @FXML
    private TableColumn<Doctor, String> docPhoneCol;

    @FXML
    private Button logOutButton;

    @FXML
    private Button doctorManagementButton;

    @FXML
    private Button homeButton;

    @FXML
    private TextField idTextField;
    
    @FXML
    private TextField searchTextField;

    @FXML
    private Button inventoryButton;

    @FXML
    private Label labelDoctor;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button removeButton;

    @FXML
    private Button scheduleShiftButton;

    @FXML
    private Button scheduleWorkshopButton;

    @FXML
    private Button supplyRequestButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField updateValueTextField;
    
    private DoctorHandler doctorHandler; // Instance to handle doctor-related DB operations

    public void initializeButtons()
    {
        homeButton.setOnAction(e->onHomeButton());
		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
		inventoryButton.setOnAction(e->onInventory());
		supplyRequestButton.setOnAction(e->onSupplyRequestButton());
		addDocButton.setOnAction(e -> onAddDocButton());
		doctorManagementButton.setOnAction(e->onDoctorManagement());
		logOutButton.setOnAction(e->onLogOutButton());
		removeButton.setOnAction(e->onRemoveButton());
		updateButton.setOnAction(e->onUpdateButton());
    }
   
    public void initializeCombobox() {
        // Populate the ComboBox with options
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList(
            "ID", 
            "First Name", 
            "Last Name", 
            "Email", 
            "Specialization", 
            "Gender", 
            "Phone"
        ));
        
        // Set a default prompt text or initial value 
        searchCriteriaComboBox.setPromptText("Search By"); // Displays a placeholder
       
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		initializeButtons();
		
		initializeUpdateComboBox();
		
		initializeCombobox();
		doctorHandler = new DoctorHandler(); // Initialize the handler

        // Bind columns to Doctor model properties
        setupTableColumns();

        // Load doctor data into the TableView
        loadDoctorData();
	}
	
	
	
	private void setupTableColumns() {
        docIDCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        docFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        docLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        docEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        docSpecCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        docGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        docPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

	private void loadDoctorData() {
	    // Fetch data from the DoctorHandler
	    ObservableList<Doctor> doctorList = FXCollections.observableArrayList(doctorHandler.getAllDoctors());

	    // Wrap the doctor list in a FilteredList
	    FilteredList<Doctor> filteredData = new FilteredList<>(doctorList, b -> true);

	    // Add a listener to the searchTextField to update the filter predicate
	    searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
	        filteredData.setPredicate(doctor -> {
	            // If the search field is empty, display all doctors
	            if (newValue == null || newValue.isEmpty()) {
	                return true;
	            }

	            // Get the selected search field
	            String selectedCriteria = searchCriteriaComboBox.getValue();
	            String lowerCaseFilter = newValue.toLowerCase();

	            // Filter based on the selected column using switch
	            if (selectedCriteria == null) {
	                return false; // No criteria selected, no filtering
	            }

	            switch (selectedCriteria) {
	                case "ID":
	                    return doctor.getDoctorIdAsString().toLowerCase().contains(lowerCaseFilter);
	                case "First Name":
	                    return doctor.getFirstName().toLowerCase().contains(lowerCaseFilter);
	                case "Last Name":
	                    return doctor.getLastName().toLowerCase().contains(lowerCaseFilter);
	                case "Email":
	                    return doctor.getEmail().toLowerCase().contains(lowerCaseFilter);
	                case "Specialization":
	                    return doctor.getSpecialization() != null && doctor.getSpecialization().toLowerCase().contains(lowerCaseFilter);
	                case "Gender":
	                    return doctor.getGender().toLowerCase().contains(lowerCaseFilter);
	                case "Phone":
	                    return doctor.getPhone() != null && doctor.getPhone().contains(lowerCaseFilter);
	                default:
	                    return false; // No match
	            }
	        });
	    });

	    // Set the FilteredList to the TableView
	    doctorInfoTableView.setItems(filteredData);
	}

	
	private void onLogOutButton() {
			//closing the stage
			Stage stage = (Stage) labelDoctor.getScene().getWindow();
			AdminModel.getInstance().getViewFactory().closeStage(stage);
			AdminModel.getInstance().getViewFactory().showLoginWindow();
		}
			
	private void onHomeButton() {
	    Stage stage = (Stage) labelDoctor.getScene().getWindow();
	    stage.close(); // Close the current stage
	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
	}

	private void onSupplyRequestButton() {
	    Stage stage = (Stage) labelDoctor.getScene().getWindow();
	    stage.close(); // Close the current stage
	    AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest(); // Open the dashboard
	}
		
	private void onScheduleShiftButton() {
		//closing the stage
		Stage stage = (Stage) labelDoctor.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleShift();
	}
	
	private void onScheduleWorkshopButton(){
	
		Stage stage = (Stage) labelDoctor.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
				
	}
	
	private void onInventory(){
		
		Stage stage = (Stage) labelDoctor.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showInventory();
				
	}
	
	private void onAddDocButton()
	{
		Stage stage = (Stage) labelDoctor.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showManageDoctorRequest();
	}
	
	private void onDoctorManagement() {
		//closing the stage
		Stage stage = (Stage) labelDoctor.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showDoctorManagement();
	}
	
	
	
	
	//CRUD OPERATIONS
	
	
	private void initializeUpdateComboBox() {
	    // Populate the ComboBox with options for update criteria
	    updateCriteriaComboBox.setItems(FXCollections.observableArrayList(
	        "First Name",
	        "Last Name",
	        "Email",
	        "Specialization",
	        "Gender",
	        "Phone"
	    ));
	    updateCriteriaComboBox.setPromptText("Select field to update");
	}


	private void onUpdateButton() {
	    // Get values from the input fields
	    String doctorId = idTextField.getText();
	    String selectedCriteria = updateCriteriaComboBox.getValue();
	    String newValue = updateValueTextField.getText();

	    // Validate inputs
	    if (doctorId == null || doctorId.trim().isEmpty()) {
	        showAlert("Error", "Please enter a valid Doctor ID.");
	        return;
	    }
	    if (selectedCriteria == null) {
	        showAlert("Error", "Please select a field to update.");
	        return;
	    }
	    if (newValue == null || newValue.trim().isEmpty()) {
	        showAlert("Error", "Please enter a new value.");
	        return;
	    }

	    try {
	        int id = Integer.parseInt(doctorId); // Convert ID to integer

	        // Call the DoctorHandler to update the doctor details
	        boolean success = doctorHandler.updateDoctorById(id, selectedCriteria, newValue);

	        if (success) {
	            showAlert("Success", "Doctor details updated successfully!");
	            loadDoctorData(); // Refresh the TableView to show updated data
	        } else {
	            showAlert("Error", "Failed to update doctor details. Please check the Doctor ID.");
	        }
	    } catch (NumberFormatException e) {
	        showAlert("Error", "Doctor ID must be a number.");
	    }
	}
	
	private void onRemoveButton() {
	    // Get the doctor ID from the TextField
	    String doctorId = idTextField.getText();

	    // Validate the input
	    if (doctorId == null || doctorId.trim().isEmpty()) {
	        showAlert("Error", "Please enter a valid Doctor ID.");
	        return;
	    }

	    // Convert to an integer
	    try {
	        int id = Integer.parseInt(doctorId);

	        // Check if the doctor has appointments
	        if (doctorHandler.hasAppointments(id)) {
	            List<Appointment> appointments = doctorHandler.getDoctorAppointments(id);
	            StringBuilder message = new StringBuilder("Doctor cannot be removed because they have the following appointments:\n");
	            for (Appointment appointment : appointments) {
	                message.append("Appointment ID: ").append(appointment.getAppointmentId())
	                       .append(", Date: ").append(appointment.getAppointmentDate())
	                       .append(", Status: ").append(appointment.getStatus())
	                       .append(", Description: ").append(appointment.getDescription()).append("\n");
	            }
	            showAlert("Cannot Remove Doctor", message.toString());
	            return;
	        }

	        // Call the DoctorHandler to remove the doctor
	        boolean success = doctorHandler.deleteDoctorById(id);

	        if (success) {
	            showAlert("Success", "Doctor removed successfully!");
	            loadDoctorData(); // Refresh the TableView
	        } else {
	            showAlert("Error", "No doctor found with the given ID.");
	        }
	    } catch (NumberFormatException e) {
	        showAlert("Error", "Doctor ID must be a number.");
	    }
	}

	
	private void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

}
