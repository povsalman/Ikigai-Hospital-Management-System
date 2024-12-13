package adminControllers;

import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ManageDoctorRequestController implements Initializable {

	
	@FXML
	private TableView<DoctorRequest> doctorInfoTableView;

	@FXML
	private TableColumn<DoctorRequest, Integer> reqIDCol;

	@FXML
	private TableColumn<DoctorRequest, String> docFirstNameCol;

	@FXML
	private TableColumn<DoctorRequest, String> docLastNameCol;

	@FXML
	private TableColumn<DoctorRequest, String> docEmailCol;

	@FXML
	private TableColumn<DoctorRequest, String> docSpecCol;

	@FXML
	private TableColumn<DoctorRequest, String> docGenderCol;

	@FXML
	private TableColumn<DoctorRequest, String> docPhoneCol;

	@FXML
	private ComboBox<String> searchCriteriaComboBox;

	@FXML
	private TextField searchTextField;

	   
	    

	    @FXML
	    private Button addNewDocButton;

	   
	    @FXML
	    private TextField docRequestID;    

	    @FXML
	    private Button doctorManagementButton;

	    @FXML
	    private Label headLabel;

	    @FXML
	    private Button homeButton;

	    @FXML
	    private Button inventoryButton;

	    @FXML
	    private Button logOutButton;

	    @FXML
	    private ImageView myImageView;

	    @FXML
	    private Button rejectNewDocButton;

	   

	    @FXML
	    private Button scheduleShiftButton;

	    @FXML
	    private Button scheduleWorkshopButton;

	   
	    @FXML
	    private Button supplyRequestButton;

	   private DoctorHandler doctorHandler;

    @FXML
    void addDoctor(ActionEvent event) {

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
    	doctorManagementButton.setOnAction(e -> onDoctorManagement());
		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
		inventoryButton.setOnAction(e->onInventory());
		supplyRequestButton.setOnAction(e->onRequestButton());
		logOutButton.setOnAction(e->onLogOutButton());
		rejectNewDocButton.setOnAction(e-> rejectRequest());
		addNewDocButton.setOnAction(e-> approveRequest());
    }
    
    
    
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	doctorHandler = new DoctorHandler();
    	initializeButtons();
    	setupTableColumns();
        initializeSearchCriteria();
        loadDoctorRequestData();
        
    	
    }
    
    private void setupTableColumns() {
        reqIDCol.setCellValueFactory(cellData -> cellData.getValue().requestIdProperty().asObject());
        docFirstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        docLastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        docEmailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        docSpecCol.setCellValueFactory(cellData -> cellData.getValue().specializationProperty());
        docGenderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        docPhoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        // Exclude password column to avoid displaying it
    }

  
    
    private void loadDoctorRequestData() {
        if (doctorHandler == null) {
            showAlert("Error", "Doctor handler is not initialized. Please try again.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<DoctorRequest> requestList = FXCollections.observableArrayList(doctorHandler.getAllDoctorRequests());

        // Wrap the list in a FilteredList for search functionality
        FilteredList<DoctorRequest> filteredData = new FilteredList<>(requestList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(doctorRequest -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedCriteria = searchCriteriaComboBox.getValue();

                if ("First Name".equals(selectedCriteria) && doctorRequest.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if ("Last Name".equals(selectedCriteria) && doctorRequest.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if ("Email".equals(selectedCriteria) && doctorRequest.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // No match
            });
        });

        doctorInfoTableView.setItems(filteredData);
    }

    
    private void initializeSearchCriteria() {
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Email"));
        searchCriteriaComboBox.setPromptText("Search By");
    }
    
    
    
    private void onLogOutButton() {
		//closing the stage
		Stage stage = (Stage) headLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showLoginWindow();
	}
    
    private void onHomeButton() {
   	    Stage stage = (Stage) headLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
   	}
	

   private void onDoctorManagement() {
	//closing the stage
	Stage stage = (Stage) headLabel.getScene().getWindow();
	AdminModel.getInstance().getViewFactory().closeStage(stage);
	AdminModel.getInstance().getViewFactory().showDoctorManagement();
   }

private void onScheduleShiftButton() {
	//closing the stage
	Stage stage = (Stage) headLabel.getScene().getWindow();
	AdminModel.getInstance().getViewFactory().closeStage(stage);
	AdminModel.getInstance().getViewFactory().showScheduleShift();
}

private void onScheduleWorkshopButton(){

	Stage stage = (Stage) headLabel.getScene().getWindow();
	AdminModel.getInstance().getViewFactory().closeStage(stage);
	AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
			
}
private void onInventory(){
	
	Stage stage = (Stage) headLabel.getScene().getWindow();
	AdminModel.getInstance().getViewFactory().closeStage(stage);
	AdminModel.getInstance().getViewFactory().showInventory();
			
}

private void onRequestButton()
{
	Stage stage = (Stage) headLabel.getScene().getWindow();
	AdminModel.getInstance().getViewFactory().closeStage(stage);
	AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest();
}



@FXML
void approveRequest() {
    String requestIdText = docRequestID.getText();

    if (requestIdText == null || requestIdText.trim().isEmpty()) {
        showAlert("Error", "Please enter a valid Request ID.", Alert.AlertType.ERROR);
        return;
    }

    try {
        int requestId = Integer.parseInt(requestIdText);
       

        if (doctorHandler.approveDoctorRequest(requestId)) {
            showAlert("Success", "Doctor request approved and added to the system!", Alert.AlertType.INFORMATION);
            loadDoctorRequestData(); // Refresh data
        } else {
            showAlert("Error", "Failed to approve the request. Please try again.", Alert.AlertType.ERROR);
        }
    } catch (NumberFormatException e) {
        showAlert("Error", "Request ID must be a number.", Alert.AlertType.ERROR);
    }
}

@FXML
void rejectRequest() {
    String requestIdText = docRequestID.getText();

    if (requestIdText == null || requestIdText.trim().isEmpty()) {
        showAlert("Error", "Please enter a valid Request ID.", Alert.AlertType.ERROR);
        return;
    }

    try {
        int requestId = Integer.parseInt(requestIdText);
        DoctorHandler doctorHandler = new DoctorHandler();

        if (doctorHandler.rejectDoctorRequest(requestId)) {
            showAlert("Success", "Doctor request rejected and removed.", Alert.AlertType.INFORMATION);
            loadDoctorRequestData(); // Refresh data
        } else {
            showAlert("Error", "Failed to reject the request. Please try again.", Alert.AlertType.ERROR);
        }
    } catch (NumberFormatException e) {
        showAlert("Error", "Request ID must be a number.", Alert.AlertType.ERROR);
    }
}


private void showAlert(String title, String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null); // No header text
    alert.setContentText(message);
    alert.showAndWait();
}



}
