package adminControllers;

import java.net.URL;
import java.util.ResourceBundle;
import adminModel.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController implements Initializable {

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button signInButton;
    
    @FXML
    private Button signUpButton;

    @FXML
    private TextField userIDInput;

    @FXML
    private Label userIDLabel;
    
    private AdminHandler adminHandler;


    @FXML
    void userLogin(ActionEvent event) {

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		adminHandler = new AdminHandler();  // Initialize AdminHandler
		signInButton.setOnAction(event -> onLogin());
		signUpButton.setOnAction(e->onSignUp());
	
	}
	
	private void onLogin() {
        // Fetch input values
        String userIdText = userIDInput.getText();
        String password = passwordInput.getText();

        // Validate input
        if (userIdText.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both User ID and Password.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);  // Convert user ID to integer

            // Validate credentials using AdminHandler
            boolean isValid = adminHandler.validateAdminLogin(userId, password);

            if (isValid) {
                // If valid, open the dashboard
                Stage stage = (Stage) userIDLabel.getScene().getWindow();
                AdminModel.getInstance().getViewFactory().closeStage(stage);
                AdminModel.getInstance().getViewFactory().showDashboardWindow();
            } else {
                // Show error if credentials are invalid
                showAlert("Login Error", "Invalid User ID or Password. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "User ID must be a number.", Alert.AlertType.ERROR);
        }
    }

	
	private void onSignUp() {
		
		Stage stage = (Stage) userIDLabel.getScene().getWindow();
        AdminModel.getInstance().getViewFactory().closeStage(stage);
        AdminModel.getInstance().getViewFactory().showSignUpWindow();
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




