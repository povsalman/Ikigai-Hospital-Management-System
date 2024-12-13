package adminControllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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

public class ScheduleWorkshopController implements Initializable{

	@FXML
	private TableColumn<Workshop, String> adminCol;

	@FXML
	private Button confirmWorkshopButton;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button doctorManagementButton;

	@FXML
	private TableColumn<Workshop, String> endTimeCol;

	@FXML
	private ComboBox<String> endTimePicker;

	@FXML
	private Button homeButton;

	@FXML
	private Button inventoryButton;

	@FXML
	private Button logOutButton;

	@FXML
	private ImageView myImageView;

	@FXML
	private Button scheduleShiftButton;

	@FXML
	private Button scheduleWorkshopButton;

	@FXML
	private ComboBox<String> searchComboBox;

	@FXML
	private TextField searchTextField;

	@FXML
	private TableColumn<Workshop, String> startTimeCol;

	@FXML
	private ComboBox<String> startTimePicker;

	@FXML
	private Button supplyRequestButton;

	@FXML
	private TableColumn<Workshop, String> venueCol;

	@FXML
	private ComboBox<String> venuePicker;

	@FXML
	private TableColumn<Workshop, String> workshopDateCol;

	@FXML
	private TextField workshopDescTextField;

	@FXML
	private TextField workshopSpeakerTextField;

	@FXML
	private TableColumn<Workshop, String> workshopDescriptionCol;

	@FXML
	private Label workshopLabel;

	@FXML
	private TableColumn<Workshop, String> workshopNameCol;

	@FXML
	private TextField workshopNameTextField;

	@FXML
	private TableView<Workshop> workshopScheduleTableView;

	@FXML
	private TableColumn<Workshop, String> workshopSpeakerCol;

	private WorkshopHandler workshopHandler;


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
        confirmWorkshopButton.setOnAction(e->scheduleWorkshop());
        
    }
    
    
    
    private void initializeComboBoxes() {
        // Time options
        ObservableList<String> timeOptions = FXCollections.observableArrayList();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                timeOptions.add(String.format("%02d:%02d", hour, minute));
            }
        }
        startTimePicker.setItems(timeOptions);
        endTimePicker.setItems(timeOptions);

        // Venue options
        venuePicker.setItems(FXCollections.observableArrayList(
        		"Auditorium",
        		"Auditorium B",
        		"Auditorium C",
        		"Conference Hall",
        		"Conference Hall B",
        		"Conference Hall C",
        		"Main Hall",
        		"Room A",
        		"Room B",
        		"Room C"));

        // Search criteria options
        searchComboBox.setItems(FXCollections.observableArrayList(
            "Admin Name",
            "Workshop Name",
            "Speaker Name",
            "Date",
            "Start Time",
            "End Time",
            "Venue"
        ));
        searchComboBox.setValue("Workshop Name"); // Set a default value
    }

    
    @Override
   	public void initialize(URL arg0, ResourceBundle arg1) {
   		
    	 workshopHandler = new WorkshopHandler(); // Initialize the handler

    	 setupWorkshopTableColumns();
    	    initializeComboBoxes();
    	    initializeButtons();
    	    loadWorkshopData();
    	
    }
    
    private void loadWorkshopData() {
        ObservableList<Workshop> workshopList = FXCollections.observableArrayList(workshopHandler.getAllWorkshops());
        FilteredList<Workshop> filteredData = new FilteredList<>(workshopList, b -> true);

        // Add a listener to the searchTextField to update the filter predicate
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCriteria = searchComboBox.getValue(); // Get selected criteria
            filteredData.setPredicate(workshop -> {
                // If the search field is empty, display all workshops
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Apply filter based on the selected criteria
                switch (selectedCriteria) {
                    case "Admin Name":
                        return workshop.getAdminName().toLowerCase().contains(lowerCaseFilter);
                    case "Workshop Name":
                        return workshop.getWorkshopName().toLowerCase().contains(lowerCaseFilter);
                    case "Speaker Name":
                        return workshop.getSpeaker().toLowerCase().contains(lowerCaseFilter);
                    case "Date":
                        return workshop.getDate().toLowerCase().contains(lowerCaseFilter);
                    case "Start Time":
                        return workshop.getStartTime().toLowerCase().contains(lowerCaseFilter);
                    case "End Time":
                        return workshop.getEndTime().toLowerCase().contains(lowerCaseFilter);
                    case "Venue":
                        return workshop.getLocation().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return false;
                }
            });
        });

        // Bind the filtered data to the TableView
        workshopScheduleTableView.setItems(filteredData);
    }


    
    private void setupWorkshopTableColumns() {
        workshopNameCol.setCellValueFactory(new PropertyValueFactory<>("workshopName"));
        workshopSpeakerCol.setCellValueFactory(new PropertyValueFactory<>("speaker"));
        workshopDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        workshopDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        venueCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        adminCol.setCellValueFactory(new PropertyValueFactory<>("adminName"));
    }
    
    private void onLogOutButton() {
		//closing the stage
		Stage stage = (Stage) workshopLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showLoginWindow();
	}
   	private void onHomeButton() {
   	    Stage stage = (Stage) workshopLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
   	}

   	private void onSupplyRequestButton() {
   	    Stage stage = (Stage) workshopLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest(); // Open the dashboard
   	}

   	private void onDoctorManagement() {
   		//closing the stage
   		Stage stage = (Stage) workshopLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showDoctorManagement();
   	}
   	
   	private void onScheduleShiftButton() {
   		//closing the stage
   		Stage stage = (Stage) workshopLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleShift();
   	}

   	private void onScheduleWorkshopButton(){
   	
   		Stage stage = (Stage) workshopLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
   				
   	}
   	private void onInventory(){
   		
   		Stage stage = (Stage) workshopLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showInventory();
   				
   	}
   	
   	private boolean validateWorkshopInputs() {
   	    if (workshopNameTextField.getText() == null || workshopNameTextField.getText().isEmpty()) {
   	        showAlert("Error", "Workshop name cannot be empty.");
   	        return false;
   	    }
   	    if (workshopSpeakerTextField.getText() == null || workshopSpeakerTextField.getText().isEmpty()) {
   	        showAlert("Error", "Speaker name cannot be empty.");
   	        return false;
   	    }
   	    if (workshopDescTextField.getText() == null || workshopDescTextField.getText().isEmpty()) {
   	        showAlert("Error", "Description cannot be empty.");
   	        return false;
   	    }
   	    if (datePicker.getValue() == null) {
   	        showAlert("Error", "Date must be selected.");
   	        return false;
   	    }
   	    if (startTimePicker.getValue() == null || endTimePicker.getValue() == null) {
   	        showAlert("Error", "Start and End times must be selected.");
   	        return false;
   	    }
   	    if (venuePicker.getValue() == null || venuePicker.getValue().toString().isEmpty()) {
   	        showAlert("Error", "Venue must be selected.");
   	        return false;
   	    }
   	    return true;
   	}
   	
   	private String generateSuggestions(String date, String venue, String startTime, String endTime) {
   	    StringBuilder suggestion = new StringBuilder("Suggestions:\n");

   	    // Fetch existing workshops for the same date and venue
   	    List<Workshop> existingWorkshops = workshopHandler.getWorkshopsByDateAndVenue(date, venue);

   	    if (!existingWorkshops.isEmpty()) {
   	        suggestion.append("1. Alternative times at ").append(venue).append(":\n");

   	        // Sort workshops by start time to ensure proper order
   	        existingWorkshops.sort((w1, w2) -> w1.getStartTime().compareTo(w2.getStartTime()));

   	        // Suggest time slots before, between, and after existing workshops
   	        String earliestStart = "09:00"; // Assuming venue opens at 8 AM
   	        String latestEnd = "20:00"; // Assuming venue closes at 8 PM

   	        // Time slot before the first workshop
   	        if (earliestStart.compareTo(existingWorkshops.get(0).getStartTime()) < 0) {
   	            suggestion.append("   - ").append(earliestStart)
   	                    .append(" to ").append(existingWorkshops.get(0).getStartTime()).append("\n");
   	        }

   	        // Time slots between consecutive workshops
   	        for (int i = 0; i < existingWorkshops.size() - 1; i++) {
   	            Workshop current = existingWorkshops.get(i);
   	            Workshop next = existingWorkshops.get(i + 1);

   	            if (current.getEndTime().compareTo(next.getStartTime()) < 0) {
   	                suggestion.append("   - ").append(current.getEndTime())
   	                        .append(" to ").append(next.getStartTime()).append("\n");
   	            }
   	        }

   	        // Time slot after the last workshop
   	        if (existingWorkshops.get(existingWorkshops.size() - 1).getEndTime().compareTo(latestEnd) < 0) {
   	            suggestion.append("   - ").append(existingWorkshops.get(existingWorkshops.size() - 1).getEndTime())
   	                    .append(" to ").append(latestEnd).append("\n");
   	        }
   	    } else {
   	        suggestion.append("1. No existing workshops at ").append(venue).append(". Venue is free for the whole day.\n");
   	    }

   	    // Suggest other venues for the same time
   	    suggestion.append("2. Other venues available for the same time:\n");
   	    List<String> availableVenues = workshopHandler.getAvailableVenues(date, startTime, endTime);

   	    if (!availableVenues.isEmpty()) {
   	        for (String altVenue : availableVenues) {
   	            // Exclude the current venue from the alternative suggestions
   	            if (!altVenue.equalsIgnoreCase(venue)) {
   	                suggestion.append("   - ").append(altVenue).append("\n");
   	            }
   	        }
   	    } else {
   	        suggestion.append("   - No other venues available for the same time.\n");
   	    }

   	    return suggestion.toString();
   	}


   	
   	private void scheduleWorkshop() {
   	    try {
   	        // Step 1: Validate inputs
   	        if (!validateWorkshopInputs()) {
   	            return; // Exit if inputs are invalid
   	        }

   	        // Step 2: Get user inputs
   	        String workshopName = workshopNameTextField.getText().trim();
   	        String speaker = workshopSpeakerTextField.getText().trim();
   	        String description = workshopDescTextField.getText().trim();
   	        LocalDate date = datePicker.getValue(); // Use LocalDate for comparison
   	        String startTime = startTimePicker.getValue();
   	        String endTime = endTimePicker.getValue();
   	        String venue = venuePicker.getValue();
   	        int adminId = AdminModel.getInstance().getLoggedInAdminId(); // Fetch admin ID

   	        // Step 3: Validate date and time
   	        if (date == null || date.isBefore(LocalDate.now())) {
   	            showAlert("Error", "The selected date cannot be in the past. Please choose today or a future date.");
   	            return;
   	        }

   	        LocalTime start = LocalTime.parse(startTime);
   	        LocalTime end = LocalTime.parse(endTime);

   	        if (start.isAfter(end)) {
   	            showAlert("Error", "Start time cannot be later than end time.");
   	            return;
   	        }

   	        if (end.equals(start) || end.isBefore(start)) {
   	            showAlert("Error", "End time must be greater than start time.");
   	            return;
   	        }

   	        // Step 4: Check for clashes
   	        List<Workshop> clashes = workshopHandler.checkForClashes(date.toString(), startTime, endTime, venue);

   	        if (!clashes.isEmpty()) {
   	            // Step 5: Generate suggestions
   	            String suggestion = generateSuggestions(date.toString(), venue, startTime, endTime);
   	            showAlert("Clash Detected", "A workshop is already scheduled at this time and venue. "
   	                    + "Please try a different time or venue.\n" + suggestion);
   	            return;
   	        }

   	        // Step 6: Insert workshop into the database
   	        boolean success = workshopHandler.addWorkshop(
   	                adminId, workshopName, speaker, description, date.toString(), startTime, endTime, venue);

   	        if (success) {
   	            showAlert("Success", "Workshop scheduled successfully!");
   	            loadWorkshopData(); // Refresh the table view
   	        } else {
   	            showAlert("Error", "Failed to schedule the workshop. Please try again.");
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
   	
}
