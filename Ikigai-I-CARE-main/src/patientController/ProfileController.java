package patientController;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import patientModel.Patient;
import javafx.scene.image.Image;

public class ProfileController {

    private final Connection con;
    private final Patient patient;
    private Image profileImage;

    public ProfileController(Connection con, Patient patient) {
        this.con = con;
        this.patient = patient;
    }

    public void loadProfileData(TextField fname, TextField lname, TextArea address, ComboBox<String> gender, TextField mobileNumber, TextField password, Circle profileCircle, Label patientID, Label name, Label mobileLabel, Label genderLabel, TextArea profile_label_address,Label topusrname, Circle top_profile) {
        patient.fetchPatientDetails(con);

        patientID.setText(Integer.toString(patient.getPatientId()));
        name.setText(patient.getName());
        topusrname.setText(patient.getName());
        mobileLabel.setText(patient.getPhoneNo());
        genderLabel.setText(patient.getGender());
        profile_label_address.setText(patient.getAddress());

        if (patient.getImage() != null && !patient.getImage().isEmpty()) {
            profileImage = new Image(patient.getImage(), 128, 103, false, true);
            profileCircle.setFill(new ImagePattern(profileImage));
            top_profile.setFill(new ImagePattern(profileImage));
        }
    }

    public void updateUserProfileImage(Circle profileCircle, Circle top_profile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            profileImage = new Image(file.toURI().toString(), 128, 103, false, true);
            profileCircle.setFill(new ImagePattern(profileImage));
            top_profile.setFill(new ImagePattern(profileImage));
            
            String imagePath = file.toURI().toString();
            String sql = "UPDATE Patient SET image = ? WHERE patient_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, imagePath);
                stmt.setInt(2, patient.getPatientId());
                stmt.executeUpdate();
                showAlert("Success", "Profile image updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update profile image.");
            }
        }
    }

    public Boolean updateProfile(TextField fname, TextField lname, TextArea address, ComboBox<String> gender, TextField mobileNumber, TextField password) {
        try {
            String firstName = fname.getText().trim();
            String lastName = lname.getText().trim();
            String addr = address.getText().trim();
            String gen = gender.getValue();
            String phone = mobileNumber.getText().trim();
            String pass = password.getText().trim();

            StringBuilder queryBuilder = new StringBuilder("UPDATE Patient SET ");
            boolean hasUpdates = false;

            if (!firstName.isEmpty()) {
                queryBuilder.append("first_name = ?, ");
                hasUpdates = true;
            }
            if (!lastName.isEmpty()) {
                queryBuilder.append("last_name = ?, ");
                hasUpdates = true;
            }
            if (!addr.isEmpty()) {
                queryBuilder.append("address = ?, ");
                hasUpdates = true;
            }
            if (gen != null) {
                queryBuilder.append("gender = ?, ");
                hasUpdates = true;
            }
            if (!phone.isEmpty()) {
                queryBuilder.append("mobile_number = ?, ");
                hasUpdates = true;
            }
            if (!pass.isEmpty()) {
                queryBuilder.append("password = ?, ");
                hasUpdates = true;
            }

            if (!hasUpdates) {
                showAlert("Error", "No fields to update. Please modify at least one field.");
                return false;
            }

            queryBuilder.setLength(queryBuilder.length() - 2);
            queryBuilder.append(" WHERE patient_id = ?");

            try (PreparedStatement stmt = con.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;

                if (!firstName.isEmpty()) stmt.setString(paramIndex++, firstName);
                if (!lastName.isEmpty()) stmt.setString(paramIndex++, lastName);
                if (!addr.isEmpty()) stmt.setString(paramIndex++, addr);
                if (gen != null) stmt.setString(paramIndex++, gen);
                if (!phone.isEmpty()) stmt.setString(paramIndex++, phone);
                if (!pass.isEmpty()) stmt.setString(paramIndex++, pass);

                stmt.setInt(paramIndex, patient.getPatientId());
                System.out.println(stmt);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Success", "Profile updated successfully!");
                    patient.fetchPatientDetails(con);
                    return true;
                } else {
                    showAlert("Error", "Profile update failed. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the profile.");
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = title.equals("Error") ? new Alert(Alert.AlertType.ERROR) : new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
