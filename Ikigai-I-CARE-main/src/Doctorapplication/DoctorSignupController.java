package Doctorapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoctorSignupController {

    @FXML
    private TextField email;

    @FXML
    private TextField f_name;

    @FXML
    private TextField l_name;

    @FXML
    private TextField ispassword;

    @FXML
    private TextField phoneno;

    @FXML
    private Button signupBtn;
    
    private Connection connect;
    private PreparedStatement prepare;

    @FXML
    void signup(ActionEvent event) {
        // Collect user inputs
        String firstName = f_name.getText();
        String lastName = l_name.getText();
        String emailAddress = email.getText();
        String passwordValue = ispassword.getText();
        String phoneNumber = phoneno.getText();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || passwordValue.isEmpty() || phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        // Hash the password (Replace with a secure hash function in production)
        String hashedPassword = passwordValue;

        try {
            // Establish database connection
            connect = Database.connectDB(); // Replace with your actual database connection method

            // SQL query to insert data into DoctorRequest table
            String sql = """
                INSERT INTO DoctorRequest (doc_Fname, doc_Lname, email, password, mobile_number)
                VALUES (?, ?, ?, ?, ?)
            """;

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, firstName); // doc_Fname
            prepare.setString(2, lastName);  // doc_Lname
            prepare.setString(3, emailAddress); // email
            prepare.setString(4, passwordValue); // hashed password
            prepare.setString(5, phoneNumber); // mobile_number

            int rowsInserted = prepare.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Signup request submitted successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Signup request failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Clear the input fields after submission
    private void clearFields() {
        f_name.clear();
        l_name.clear();
        email.clear();
        ispassword.clear();
        phoneno.clear();
    }
    
    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

