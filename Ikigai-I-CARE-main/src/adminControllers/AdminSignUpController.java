package adminControllers;

import adminModel.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminSignUpController {

	@FXML
    private Button signUpButton;

    @FXML
    private TextField signUpEmail;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpPassword;

    @FXML
    private TextField signUpPhoneNo;

    private AdminHandler adminHandler;

    @FXML
    public void initialize() {
        adminHandler = new AdminHandler();
        signUpButton.setOnAction(e -> handleSignUp());
    }

    private void handleSignUp() {
        try {
            // Step 1: Get user inputs
            String firstName = signUpFirstName.getText().trim();
            String lastName = signUpLastName.getText().trim();
            String email = signUpEmail.getText().trim();
            String password = signUpPassword.getText().trim();
            String phoneNo = signUpPhoneNo.getText().trim();

            // Step 2: Validate inputs
            if (!validateInputs(firstName, lastName, email, password, phoneNo)) {
                return; // Exit if validation fails
            }

            // Step 3: Insert admin into the database
            int adminId = adminHandler.addAdmin(firstName, lastName, email, password, phoneNo);

            // Step 4: Show success message with admin ID
            if (adminId > 0) {
                showAlert("Success", "Admin registered successfully!\nYour Admin ID is: " + adminId, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to register admin. Please try again.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validateInputs(String firstName, String lastName, String email, String password, String phoneNo) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNo.isEmpty()) {
            showAlert("Error", "All fields are required.", Alert.AlertType.ERROR);
            return false;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            showAlert("Error", "Invalid email format.", Alert.AlertType.ERROR);
            return false;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long.", Alert.AlertType.ERROR);
            return false;
        }

        if (!phoneNo.matches("^\\d{10,15}$")) {
            showAlert("Error", "Invalid phone number format. It should be 10-15 digits.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
