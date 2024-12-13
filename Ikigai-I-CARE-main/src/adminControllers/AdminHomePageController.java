package adminControllers;

import adminModel.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AdminHomePageController implements Initializable{
         
	 private AdminHandler adminHandler; // Instance of AdminHandler
	 @FXML
	    private TableView<Admin> adminDetailsTable;

	    @FXML
	    private TableColumn<Admin, String> adminIDCol;

	    @FXML
	    private TableColumn<Admin, String> adminFirstNameCol;
	    
	    @FXML
	    private TableColumn<Admin, String> adminLastNameCol;

	    @FXML
	    private TableColumn<Admin, String> adminEmailCol;

	    @FXML
	    private TableColumn<Admin, String> adminPasswordCol;

	    @FXML
	    private TableColumn<Admin, String> adminPhoneNoCol;


   

    @FXML
    private Button logOutButton;
    
    @FXML
    private Button doctorManagementButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button scheduleShiftButton;

    @FXML
    private Button scheduleWorkshopButton;

    @FXML
    private Button supplyRequestButton;

    @FXML
    private Label usernameLabel;

    

    @FXML
    private Label welcomeUsernameLabel;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		// Initialize AdminHandler
        adminHandler = new AdminHandler();

        // Setup the TableView columns to bind to the Admin model properties
        setupTableColumns();

        // Load data into TableView
        loadAdminData();

		
		doctorManagementButton.setOnAction(e -> onDoctorManagement());
		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
		inventoryButton.setOnAction(e->onInventory());
		supplyRequestButton.setOnAction(e->onRequestButton());
		logOutButton.setOnAction(e->onLogOutButton());
		
		
	}
	
	//Loadind data into table for admin details
	private void setupTableColumns() {
	    // Set the property value factories to link TableView columns to Admin model properties
	    adminIDCol.setCellValueFactory(new PropertyValueFactory<>("adminId"));
	    adminFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
	    adminLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
	    adminEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
	    adminPasswordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
	    adminPhoneNoCol.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
	}

	    private void loadAdminData() {
	        // Fetch data from AdminHandler (database)
	        ObservableList<Admin> adminList = FXCollections.observableArrayList(adminHandler.getLoggedInAdminData());

	        // Set the TableView's items to the list of Admin objects
	        adminDetailsTable.setItems(adminList);
	    }
	    
	    private void onLogOutButton() {
			//closing the stage
			Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
			AdminModel.getInstance().getViewFactory().closeStage(stage);
			AdminModel.getInstance().getViewFactory().showLoginWindow();
		}
		
	
	private void onDoctorManagement() {
		//closing the stage
		Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showDoctorManagement();
	}
	
	private void onScheduleShiftButton() {
		//closing the stage
		Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleShift();
	}
	
	private void onScheduleWorkshopButton(){
	
		Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
				
	}
	private void onInventory(){
		
		Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showInventory();
				
	}
	
	private void onRequestButton()
	{
		Stage stage = (Stage) welcomeUsernameLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest();
	}

}
