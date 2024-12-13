package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import doctorHandler.WorkshopHandler;
import doctorModel.WorkshopData;
import Doctorapplication.*;

public class WorkshopController {

    // FXML variables from DoctorMainFormController
    @FXML public TableView<WorkshopData> workshops_tableView;
    @FXML public TableColumn<WorkshopData, Integer> workshops_col_workshopID;
    @FXML public TableColumn<WorkshopData, String> workshops_col_topic;
    @FXML public TableColumn<WorkshopData, String> workshops_col_speaker;
    @FXML public TableColumn<WorkshopData, String> workshops_col_description;
    @FXML public TableColumn<WorkshopData, String> workshops_col_date;
    @FXML public TableColumn<WorkshopData, String> workshops_col_location;
    @FXML public TableColumn<WorkshopData, String> workshops_col_starttime;
    @FXML public TableColumn<WorkshopData, String> workshops_col_endtime;
    @FXML public TableColumn<WorkshopData, String> workshops_col_status;
    @FXML public TableColumn<WorkshopData, String> workshops_col_confirm;

    @FXML public TextField workshop_workshopID;

    // ObservableList to hold workshop data
    private ObservableList<WorkshopData> workshopListData;

    private WorkshopHandler workshopHandler; // DAO for database operations

    // Constructor with DAO dependency injection
    public WorkshopController(WorkshopHandler workshopHandler) {
        this.workshopHandler = workshopHandler;
    }
    
    
    public void setWorkshopComponents(TableView<WorkshopData> workshops_tableView, TableColumn<WorkshopData, Integer> workshops_col_workshopID, 
            TableColumn<WorkshopData, String> workshops_col_topic, TableColumn<WorkshopData, String> workshops_col_speaker, 
            TableColumn<WorkshopData, String> workshops_col_description, TableColumn<WorkshopData, String> workshops_col_date, 
            TableColumn<WorkshopData, String> workshops_col_location, TableColumn<WorkshopData, String> workshops_col_starttime, 
            TableColumn<WorkshopData, String> workshops_col_endtime, TableColumn<WorkshopData, String> workshops_col_status, 
            TableColumn<WorkshopData, String> workshops_col_confirm, TextField workshop_workshopID) {
	
    this.workshops_tableView = workshops_tableView; 
	this.workshops_col_workshopID = workshops_col_workshopID; 
	this.workshops_col_topic = workshops_col_topic; 
	this.workshops_col_speaker = workshops_col_speaker; 
	this.workshops_col_description = workshops_col_description; 
	this.workshops_col_date = workshops_col_date; 
	this.workshops_col_location = workshops_col_location; 
	this.workshops_col_starttime = workshops_col_starttime; 
	this.workshops_col_endtime = workshops_col_endtime; 
	this.workshops_col_status = workshops_col_status; 
	this.workshops_col_confirm = workshops_col_confirm; 
	this.workshop_workshopID = workshop_workshopID;
	}

   
    // Method to display workshop data in the TableView
    public void workshopShowData() {
        // Fetch data from DAO
        workshopListData = FXCollections.observableArrayList(workshopHandler.getAllWorkshopsForDoctor(Data.doctor_id));

        // Map the data to the TableView columns
        workshops_col_workshopID.setCellValueFactory(new PropertyValueFactory<>("workshopId"));
        workshops_col_topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
        workshops_col_speaker.setCellValueFactory(new PropertyValueFactory<>("speaker"));
        workshops_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        workshops_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        workshops_col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        workshops_col_starttime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        workshops_col_endtime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        workshops_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        workshops_col_confirm.setCellValueFactory(new PropertyValueFactory<>("regstatus"));

        // Populate the TableView
        workshops_tableView.setItems(workshopListData);
    }

    // Method to select a workshop from the TableView
    public void workshopSelect() {
        WorkshopData selectedWorkshop = workshops_tableView.getSelectionModel().getSelectedItem();
        if (selectedWorkshop != null) {
            workshop_workshopID.setText(String.valueOf(selectedWorkshop.getWorkshopId()));
        }
    }

    // Method to handle "Yes" button (register for the workshop)
    public void workshopYesBtn() {
        WorkshopData selectedWorkshop = workshops_tableView.getSelectionModel().getSelectedItem();
        if (selectedWorkshop != null) {
            int workshopId = selectedWorkshop.getWorkshopId();
            int doctorId = Data.doctor_id;

            // Call DAO to register for the workshop
            boolean success = workshopHandler.registerForWorkshop(doctorId, workshopId);


            AlertMessage alertMessage = new AlertMessage();
            if (success) {
                alertMessage.successMessage("You have successfully registered for the workshop.");
            } else {
                alertMessage.errorMessage("An error occurred while registering for the workshop.");
            }

            // Refresh the workshop data in the TableView
            workshopShowData();
        }
    }


    // Method to handle "No" button (unregister from the workshop)
    public void workshopNoBtn() {
        WorkshopData selectedWorkshop = workshops_tableView.getSelectionModel().getSelectedItem();
        if (selectedWorkshop != null) {
            int workshopId = selectedWorkshop.getWorkshopId();
            int doctorId = Data.doctor_id;

            // Call DAO to unregister from the workshop
            boolean success = workshopHandler.unregisterFromWorkshop(doctorId, workshopId);

            // Display success or error message
            AlertMessage alertMessage = new AlertMessage();
            if (success) {
                alertMessage.successMessage("You have successfully unregistered from the workshop.");
            } else {
                alertMessage.errorMessage("An error occurred while unregistering from the workshop.");
            }

            // Refresh the workshop data in the TableView
            workshopShowData();
        }
    }

}
