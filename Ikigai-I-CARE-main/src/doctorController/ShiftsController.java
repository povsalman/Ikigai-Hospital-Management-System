package doctorController;

import doctorModel.ShiftsData;
import Doctorapplication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import doctorHandler.ShiftsHandler;

public class ShiftsController {

	@FXML
	public TableView<ShiftsData> shiftsschedule_tableView;
	@FXML
	public TableColumn<ShiftsData, Integer> shiftsschedule_col_shiftID;
	@FXML
	public TableColumn<ShiftsData, String> shiftsschedule_col_doctor, shiftsschedule_col_ward, shiftsschedule_col_description,
	                                       shiftsschedule_col_date, shiftsschedule_col_starttime, shiftsschedule_col_endtime, shiftsschedule_col_status;
	@FXML
	public TextField shiftsschedule_shiftID;

    
    private ShiftsHandler shiftsHandler;
    // Constructor to inject ShiftsHandler
    public ShiftsController(ShiftsHandler shiftsHandler) {
        this.shiftsHandler = shiftsHandler;
    }
    
    
    public void setShiftsComponents(TableView<ShiftsData> shiftsschedule_tableView, TableColumn<ShiftsData, Integer> shiftsschedule_col_shiftID,
            TableColumn<ShiftsData, String> shiftsschedule_col_doctor, TableColumn<ShiftsData, String> shiftsschedule_col_ward,
            TableColumn<ShiftsData, String> shiftsschedule_col_description, TableColumn<ShiftsData, String> shiftsschedule_col_date,
            TableColumn<ShiftsData, String> shiftsschedule_col_starttime, TableColumn<ShiftsData, String> shiftsschedule_col_endtime,
            TableColumn<ShiftsData, String> shiftsschedule_col_status, TextField shiftsschedule_shiftID) {
    	
	this.shiftsschedule_tableView = shiftsschedule_tableView; this.shiftsschedule_col_shiftID = shiftsschedule_col_shiftID;
	this.shiftsschedule_col_doctor = shiftsschedule_col_doctor; this.shiftsschedule_col_ward = shiftsschedule_col_ward;
	this.shiftsschedule_col_description = shiftsschedule_col_description; this.shiftsschedule_col_date = shiftsschedule_col_date;
	this.shiftsschedule_col_starttime = shiftsschedule_col_starttime; this.shiftsschedule_col_endtime = shiftsschedule_col_endtime;
	this.shiftsschedule_col_status = shiftsschedule_col_status; this.shiftsschedule_shiftID = shiftsschedule_shiftID;
	}

    public void shiftShowData() {
        ObservableList<ShiftsData> shiftListData = shiftsHandler.getAllShifts();

        shiftsschedule_col_shiftID.setCellValueFactory(new PropertyValueFactory<>("shiftId"));
        shiftsschedule_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctorAssigned"));
        shiftsschedule_col_ward.setCellValueFactory(new PropertyValueFactory<>("department"));
        shiftsschedule_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        shiftsschedule_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        shiftsschedule_col_starttime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        shiftsschedule_col_endtime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        shiftsschedule_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        shiftsschedule_tableView.setItems(shiftListData);
    }

    public void shiftSelect() {
        ShiftsData selectedShift = shiftsschedule_tableView.getSelectionModel().getSelectedItem();
        if (selectedShift != null) {
            shiftsschedule_shiftID.setText(String.valueOf(selectedShift.getShiftId()));
        }
    }

    public void shiftscheduleYesBtn() {
        ShiftsData selectedShift = shiftsschedule_tableView.getSelectionModel().getSelectedItem();
        if (selectedShift != null) {
            int shiftId = selectedShift.getShiftId();
            int doctorId = Data.doctor_id;

            boolean success = shiftsHandler.updateShiftStatus(shiftId, doctorId);
            AlertMessage alert = new AlertMessage();
            if (success) {
                alert.successMessage("Shift status updated successfully.");
                shiftShowData();
            } else {
                alert.errorMessage("Failed to update shift status. Please try again.");
            }
        }
    }

    public void shiftscheduleNoBtn() {
        AlertMessage alert = new AlertMessage();
        alert.successMessage("Shift status was NOT updated.");
    }
}
