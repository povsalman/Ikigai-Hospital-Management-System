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

import doctorModel.FeedbackData;
import Doctorapplication.*;


public class FeedbackController {

	
	@FXML
	private TextField feedbacks_feedbackID;

	@FXML
	private TableView<FeedbackData> feedbacks_tableView;

	@FXML
	private TableColumn<FeedbackData, Integer> feedbacks_col_feedbackID;
	@FXML
	private TableColumn<FeedbackData, String> feedbacks_col_patient, feedbacks_col_treatmentreceived, 
	                                         feedbacks_col_rating, feedbacks_col_description, 
	                                         feedbacks_col_date, feedbacks_col_status;

	public ObservableList<FeedbackData> feedbackListData;

	
	
    // Add a constructor or initialization method to set FXML variables
    public void setFeedbackComponents(
        TextField feedbacks_feedbackID,
        TableView<FeedbackData> feedbacks_tableView,
        TableColumn<FeedbackData, Integer> feedbacks_col_feedbackID,
        TableColumn<FeedbackData, String> feedbacks_col_patient,
        TableColumn<FeedbackData, String> feedbacks_col_treatmentreceived,
        TableColumn<FeedbackData, String> feedbacks_col_rating,
        TableColumn<FeedbackData, String> feedbacks_col_description,
        TableColumn<FeedbackData, String> feedbacks_col_date,
        TableColumn<FeedbackData, String> feedbacks_col_status
    ) {
        // Initialize the fields
        this.feedbacks_feedbackID = feedbacks_feedbackID;
        this.feedbacks_tableView = feedbacks_tableView;
        this.feedbacks_col_feedbackID = feedbacks_col_feedbackID;
        this.feedbacks_col_patient = feedbacks_col_patient;
        this.feedbacks_col_treatmentreceived = feedbacks_col_treatmentreceived;
        this.feedbacks_col_rating = feedbacks_col_rating;
        this.feedbacks_col_description = feedbacks_col_description;
        this.feedbacks_col_date = feedbacks_col_date;
        this.feedbacks_col_status = feedbacks_col_status;
    }

   
    
    public ObservableList<FeedbackData> feedbackGetData() {
        ObservableList<FeedbackData> listData = FXCollections.observableArrayList();
        String sql = """
            SELECT f.feedback_id, pa.first_name AS patient, a.treatment, f.rating_stars AS rating, 
                   f.description_, f.feedback_date AS date, f.status_
            FROM Feedback f
            JOIN Patient pa ON pa.patient_id = f.patient_id
            JOIN Appointment a ON a.patient_id = f.patient_id
            WHERE f.doctor_id = ?
        """;

        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, Data.doctor_id);
            try (ResultSet result = prepare.executeQuery()) {
                while (result.next()) {
                    listData.add(new FeedbackData(
                        result.getInt("feedback_id"),
                        result.getString("patient"),
                        result.getString("treatment"),
                        result.getString("rating"),
                        result.getString("description_"),
                        result.getDate("date"),
                        result.getString("status_")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    
    public void feedbackShowData() {
        feedbackListData = feedbackGetData();

        feedbacks_col_feedbackID.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        feedbacks_col_patient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        feedbacks_col_treatmentreceived.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        feedbacks_col_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        feedbacks_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        feedbacks_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        feedbacks_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        feedbacks_tableView.setItems(feedbackListData);
    }
    
    public void feedbackSelect() {
        FeedbackData feedbackData = feedbacks_tableView.getSelectionModel().getSelectedItem();
        int num = feedbacks_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1 || feedbackData == null) {
            return;
        }

        feedbacks_feedbackID.setText(String.valueOf(feedbackData.getFeedbackId()));
    }

    
    public void feedbackYesBtn() {
        FeedbackData selectedFeedback = feedbacks_tableView.getSelectionModel().getSelectedItem();
        int num = feedbacks_tableView.getSelectionModel().getSelectedIndex();

        if (num < 0 || selectedFeedback == null) {
            return; // No item selected
        }

        int feedbackId = selectedFeedback.getFeedbackId();

        String sqlUpdate = "UPDATE Feedback SET status_ = 'Reviewed' WHERE feedback_id = ?";

        try (Connection connect = Database.connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sqlUpdate)) {
            prepare.setInt(1, feedbackId);

            int rowsAffected = prepare.executeUpdate();
            
            if (rowsAffected > 0) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.successMessage("Feedback status updated to 'Reviewed'.");

            } else {
                System.out.println("Failed to update feedback status.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        feedbackShowData();
    }

    public void feedbackNoBtn() {
        FeedbackData selectedFeedback = feedbacks_tableView.getSelectionModel().getSelectedItem();
        int num = feedbacks_tableView.getSelectionModel().getSelectedIndex();

        if (num < 0 || selectedFeedback == null) {
            return; 
        }

        int feedbackId = selectedFeedback.getFeedbackId();

        String sqlUpdate = "UPDATE Feedback SET status_ = 'Pending' WHERE feedback_id = ?";

        try (Connection connect = Database.connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sqlUpdate)) {
            prepare.setInt(1, feedbackId);

            int rowsAffected = prepare.executeUpdate();
            
            if (rowsAffected > 0) {
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.successMessage("Feedback status updated to 'Pending'.");

            } else {
                System.out.println("Failed to update feedback status.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        feedbackShowData();
    }
	
}
