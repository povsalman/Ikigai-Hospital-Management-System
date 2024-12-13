package patientApplication;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import adminModel.AdminModel;
import doctorHandler.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import patientModel.Patient;

public class PatientLoginController {

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button signInButton;

    @FXML
    private TextField userIDInput;

    @FXML
    private Label userIDLabel;

    @FXML
    void RegisterUser(MouseEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientApplication/SignUp.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	Stage stage = (Stage) userIDLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void userLogin(ActionEvent event) {
    	String userIdText = userIDInput.getText();
        String password = passwordInput.getText();

        // Validate input
        if (userIdText.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both User ID and Password.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);  // Convert user ID to integer

            // Validate credentials
            boolean isValid = validateAdminLogin(userId, password);

            if (isValid) {
                // If valid, open the dashboard
            	try {
        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientApplication/PatientMainForm.fxml"));
        			Parent root = loader.load();
        	        Stage stage = new Stage();
        	        stage.setScene(new Scene(root));
        	        stage.show();
        	    } catch (IOException e) {
        	        e.printStackTrace();
        	    }
            	
            	Stage stage = (Stage) userIDLabel.getScene().getWindow();
                stage.close();
                
            } else {
                // Show error if credentials are invalid
                showAlert("Login Error", "Invalid User ID or Password. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "User ID must be a number.", Alert.AlertType.ERROR);
        }

    }
    
    public boolean validateAdminLogin(int userId, String password) {
        String query = "SELECT * FROM Patient WHERE patient_id = ? AND _password = ?";
        try (PreparedStatement stmt = DBHandler.getInstance().getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);  // Set userId in query
            stmt.setString(2, password);  // Set password in query
            try (ResultSet resultSet = stmt.executeQuery()) {
                // If a result exists, the login is valid
                if (resultSet.next()) {
                    Patient.getInstance().setPatientId(resultSet.getInt("patient_id"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating Patient login: " + e.getMessage());
        }
        return false;  // Return false if login is invalid
    }
    
    // Method to display alert dialogs
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

