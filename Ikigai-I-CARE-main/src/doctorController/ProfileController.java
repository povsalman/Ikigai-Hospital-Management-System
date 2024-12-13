package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import doctorHandler.*;
import doctorModel.ProfileData;
import Doctorapplication.*;

public class ProfileController {


    // UI components
    public TextField profile_name, profile_email, profile_mobileNumber, profile_currentpass, profile_newpass, profile_doctorID;
    public ComboBox<String> profile_gender, profile_specialized;
    public Circle profile_circleImage, top_profile;
    public Label profile_label_doctorID, profile_label_name, profile_label_email, profile_label_totalRating;
    public Label top_username, nav_username, nav_docID;
    public Button profile_importBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    
    private final AlertMessage alert = new AlertMessage();
    
    private final ProfileHandler profileHandler;
    // Constructor to inject ShiftsHandler
    public ProfileController(ProfileHandler profileHandler) {
        this.profileHandler = profileHandler;
    }
    
    
    // Add a constructor or initialization method to set FXML variables
    public void setProfileComponents(TextField profile_name, TextField profile_email, TextField profile_mobileNumber,
            TextField profile_currentpass, TextField profile_newpass, TextField profile_doctorID,
            ComboBox<String> profile_gender, ComboBox<String> profile_specialized, Circle profile_circleImage,
            Circle top_profile, Label profile_label_doctorID, Label profile_label_name, Label profile_label_email,
            Label profile_label_totalRating, Label top_username, Label nav_username, Label nav_docID, Button profile_importBtn) {
    	
	this.profile_name = profile_name; this.profile_email = profile_email; this.profile_mobileNumber = profile_mobileNumber;
	this.profile_currentpass = profile_currentpass; this.profile_newpass = profile_newpass; this.profile_doctorID = profile_doctorID;
	this.profile_gender = profile_gender; this.profile_specialized = profile_specialized; this.profile_circleImage = profile_circleImage;
	this.top_profile = top_profile; this.profile_label_doctorID = profile_label_doctorID; this.profile_label_name = profile_label_name;
	this.profile_label_email = profile_label_email; this.profile_label_totalRating = profile_label_totalRating;
	this.top_username = top_username; this.nav_username = nav_username; this.nav_docID = nav_docID; this.profile_importBtn = profile_importBtn;
	}
    
    
    // Gender list initialization
    public void profileGenderList() {
        ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female", "Other");
        profile_gender.setItems(genderList);
    }

    // Specialization list initialization
    public void profileSpecializedList() {
        ObservableList<String> specializationList = FXCollections.observableArrayList("Allergist", "Dermatologist", "Ophthalmologist", "Gynecologist", "Cardiologist");
        profile_specialized.setItems(specializationList);
    }

    // Populate profile fields
    public void profileFields() {
        ProfileData profileData = profileHandler.getProfileData(Data.doctor_id);
        if (profileData != null) {
            profile_doctorID.setText(String.valueOf(profileData.getDoctorId()));
            profile_doctorID.setDisable(true);
            profile_name.setText(profileData.getFullName());
            profile_email.setText(profileData.getEmail());
            profile_gender.getSelectionModel().select(profileData.getGender());
            profile_mobileNumber.setText(profileData.getMobileNumber());
            profile_currentpass.setText(profileData.getPassword());
            profile_currentpass.setDisable(true);
            profile_specialized.getSelectionModel().select(profileData.getSpecialized());
        }
    }

    // Update profile
    public void profileUpdateBtn() {
        if (profile_name.getText().isEmpty() || profile_email.getText().isEmpty() ||
            profile_gender.getSelectionModel().isEmpty() || profile_mobileNumber.getText().isEmpty() ||
            profile_specialized.getSelectionModel().isEmpty()) {

            alert.errorMessage("Please fill all blank fields");
            return;
        }

        String imagePath = null;
        if (Data.path != null && !Data.path.isEmpty()) {
            try {
                imagePath = saveImageToDirectory();
            } catch (IOException e) {
                alert.errorMessage("Failed to save image. Please try again.");
                return;
            }
        }

        ProfileData profileData = new ProfileData(
                Data.doctor_id,
                profile_name.getText(),
                profile_email.getText(),
                profile_gender.getSelectionModel().getSelectedItem(),
                profile_mobileNumber.getText(),
                !profile_newpass.getText().isEmpty() ? profile_newpass.getText() : profile_currentpass.getText(),
                profile_specialized.getSelectionModel().getSelectedItem(),
                null // Image will be set if updated
                
        );

        boolean success = profileHandler.updateProfileData(profileData, imagePath, !profile_newpass.getText().isEmpty());
        if (success) {
            alert.successMessage("Profile updated successfully!");
            profileFields();
            profileDisplayImages(profileData);
        } else {
            alert.errorMessage("Failed to update profile. Please try again.");
        }
    }

    // Import a profile image
    public void profileChangeProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(profile_importBtn.getScene().getWindow());
        if (file != null) {
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 128, 103, false, true);
            profile_circleImage.setFill(new ImagePattern(image));
        }
    }

    // Display profile images
    public void profileDisplayImages(ProfileData profileData) {
        String imagePath = profileData.getImage();
        if (imagePath != null) {
            Image profileImage = new Image("file:" + imagePath, 128, 103, false, true);
            profile_circleImage.setFill(new ImagePattern(profileImage));
            top_profile.setFill(new ImagePattern(profileImage));
        }
    }

    // Save the uploaded image to the directory
    private String saveImageToDirectory() throws IOException {
        Path directoryPath = Paths.get("src/Directory");
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        String imagePath = "src/Directory/" + Data.doctor_id + ".jpg";
        Files.copy(Paths.get(Data.path), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
        return Paths.get(imagePath).toAbsolutePath().toString();
    }
    
    public void populateProfileFields(ProfileData profileData) {
        profile_name.setText(profileData.getFullName());
        profile_email.setText(profileData.getEmail());
        profile_mobileNumber.setText(profileData.getMobileNumber());
        profile_gender.getSelectionModel().select(profileData.getGender());
        profile_specialized.getSelectionModel().select(profileData.getSpecialized());
        profile_currentpass.setText(profileData.getPassword());
        profile_currentpass.setDisable(true); // Disable editing current password
        profile_doctorID.setText(String.valueOf(profileData.getDoctorId()));
        profile_doctorID.setDisable(true); // Disable editing doctor ID
    }

    public void populateProfileLabels(ProfileData profileData) {
        profile_label_doctorID.setText(String.valueOf(profileData.getDoctorId()));
        profile_label_name.setText(profileData.getFullName());
        profile_label_email.setText(profileData.getEmail());
        profile_label_totalRating.setText(profileData.getRating() != null ? 
                                           profileData.getRating().toString() : "No Ratings Yet");
    }

    public void displayDoctorIDAndName(ProfileData profileData) {
        String fullName = profileData.getFullName();
        top_username.setText(fullName);
        nav_username.setText(fullName);
        nav_docID.setText(String.valueOf(profileData.getDoctorId()));
    }
    

    
}

////Initialize gender options
//public void profileGenderList() {
//    List<String> genders = List.of("Male", "Female", "Other");
//    ObservableList<String> genderList = FXCollections.observableArrayList(genders);
//    profile_gender.setItems(genderList);
//}
//
//// Initialize specialization options
//private final String[] specialization = {"Allergist", "Dermatologist", "Ophthalmologist", "Gynecologist", "Cardiologist"};
//
//public void profileSpecializedList() {
//    ObservableList<String> specializationList = FXCollections.observableArrayList(specialization);
//    profile_specialized.setItems(specializationList);
//}
//
//// Update profile information
//public void profileUpdateBtn() {
//    connect = Database.connectDB();
//
//    if (profile_name.getText().isEmpty() ||
//        profile_email.getText().isEmpty() ||
//        profile_gender.getSelectionModel().getSelectedItem() == null ||
//        profile_mobileNumber.getText().isEmpty() ||
//        profile_specialized.getSelectionModel().getSelectedItem() == null) {
//
//        alert.errorMessage("Please fill all blank fields");
//        return;
//    }
//
//    StringBuilder updateQuery = new StringBuilder("UPDATE Doctor SET ");
//    List<Object> params = new ArrayList<>();
//
//    String[] nameParts = profile_name.getText().split(" ", 2);
//    String firstName = nameParts[0];
//    String lastName = (nameParts.length > 1) ? nameParts[1] : "";
//
//    updateQuery.append("doc_Fname = ?, doc_Lname = ?, ");
//    params.add(firstName);
//    params.add(lastName);
//
//    updateQuery.append("email = ?, gender = ?, mobile_number = ?, specialized = ?, ");
//    params.add(profile_email.getText());
//    params.add(profile_gender.getSelectionModel().getSelectedItem());
//    params.add(profile_mobileNumber.getText());
//    params.add(profile_specialized.getSelectionModel().getSelectedItem());
//
//    if (!profile_newpass.getText().isEmpty()) {
//        updateQuery.append("password = ?, ");
//        params.add(profile_newpass.getText());
//    }
//
//    String imagePath = null;
//    if (Data.path != null && !Data.path.isEmpty()) {
//        Path directoryPath = Paths.get("src/Directory");
//        if (!Files.exists(directoryPath)) {
//            try {
//                Files.createDirectories(directoryPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        imagePath = "src/Directory/" + Data.doctor_id + ".jpg";
//        try {
//            Files.copy(Paths.get(Data.path), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        updateQuery.append("image = ?, ");
//        params.add(Paths.get(imagePath).toAbsolutePath().toString());
//    }
//
//    if (updateQuery.toString().endsWith(", ")) {
//        updateQuery.setLength(updateQuery.length() - 2);
//    }
//
//    updateQuery.append(" WHERE doctor_id = ?");
//    params.add(Data.doctor_id);
//
//    try {
//        prepare = connect.prepareStatement(updateQuery.toString());
//        for (int i = 0; i < params.size(); i++) {
//            prepare.setObject(i + 1, params.get(i));
//        }
//
//        if (prepare.executeUpdate() > 0) {
//            alert.successMessage("Profile updated successfully!");
//            profileFields();
//            profileDisplayImages();
//            profileLabels();
//            displayDoctorIDNumberName();
//        } else {
//            alert.errorMessage("Failed to update profile. Please try again.");
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        closeDatabaseResources();
//    }
//}
//
//// Import a profile image
//public void profileChangeProfile() {
//    FileChooser fileChooser = new FileChooser();
//    fileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image", "*.png", "*.jpg", "*.jpeg"));
//
//    File file = fileChooser.showOpenDialog(profile_importBtn.getScene().getWindow());
//    if (file != null) {
//        Data.path = file.getAbsolutePath();
//        image = new Image(file.toURI().toString(), 128, 103, false, true);
//        profile_circleImage.setFill(new ImagePattern(image));
//    }
//}
//
//// Populate profile labels
//public void profileLabels() {
//    String query = "SELECT d.doctor_id, CONCAT(doc_Fname, ' ', doc_Lname) AS full_name, email, " +
//                   "ROUND(AVG(f.rating_stars), 1) AS rating FROM Doctor d " +
//                   "LEFT JOIN Feedback f ON d.doctor_id = f.doctor_id " +
//                   "WHERE d.doctor_id = ? GROUP BY d.doctor_id, full_name, email";
//
//    try {
//        connect = Database.connectDB();
//        prepare = connect.prepareStatement(query);
//        prepare.setInt(1, Data.doctor_id);
//        result = prepare.executeQuery();
//
//        if (result.next()) {
//            profile_label_doctorID.setText(result.getString("doctor_id"));
//            profile_label_name.setText(result.getString("full_name"));
//            profile_label_email.setText(result.getString("email"));
//            profile_label_totalRating.setText(result.getString("rating") != null ? result.getString("rating") : "No Ratings Yet");
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        closeDatabaseResources();
//    }
//}
//
//// Populate profile fields
//public void profileFields() {
//    String query = "SELECT *, CONCAT(doc_Fname, ' ', doc_Lname) AS full_name FROM Doctor WHERE doctor_id = ?";
//
//    try {
//        connect = Database.connectDB();
//        prepare = connect.prepareStatement(query);
//        prepare.setInt(1, Data.doctor_id);
//        result = prepare.executeQuery();
//
//        if (result.next()) {
//            profile_doctorID.setText(result.getString("doctor_id"));
//            profile_doctorID.setDisable(true);
//            profile_name.setText(result.getString("full_name"));
//            profile_email.setText(result.getString("email"));
//            profile_gender.getSelectionModel().select(result.getString("gender"));
//            profile_mobileNumber.setText(result.getString("mobile_number"));
//            profile_currentpass.setText(result.getString("password"));
//            profile_currentpass.setDisable(true);
//            profile_specialized.getSelectionModel().select(result.getString("specialized"));
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//
//// Display profile images
//public void profileDisplayImages() {
//    String query = "SELECT image FROM Doctor WHERE doctor_id = ?";
//    try {
//        connect = Database.connectDB();
//        prepare = connect.prepareStatement(query);
//        prepare.setInt(1, Data.doctor_id);
//        result = prepare.executeQuery();
//
//        if (result.next()) {
//            String imagePath = result.getString("image");
//            if (imagePath != null) {
//                image = new Image("File:" + imagePath, 1012, 22, false, true);
//                top_profile.setFill(new ImagePattern(image));
//                image = new Image("File:" + imagePath, 128, 103, false, true);
//                profile_circleImage.setFill(new ImagePattern(image));
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//
//// Display doctor ID and full name
//public void displayDoctorIDNumberName() {
//    String query = "SELECT CONCAT(doc_Fname, ' ', doc_Lname) AS full_name FROM Doctor WHERE doctor_id = ?";
//    try {
//        connect = Database.connectDB();
//        prepare = connect.prepareStatement(query);
//        prepare.setInt(1, Data.doctor_id);
//        result = prepare.executeQuery();
//
//        if (result.next()) {
//            String fullName = result.getString("full_name");
//            top_username.setText(fullName);
//            nav_username.setText(fullName);
//            nav_docID.setText(String.valueOf(Data.doctor_id));
//        } else {
//            nav_username.setText("Unknown Doctor");
//            nav_docID.setText("N/A");
//            top_username.setText("Unknown Doctor");
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        closeDatabaseResources();
//    }
//}
//
//// Close database resources
//private void closeDatabaseResources() {
//    try {
//        if (result != null) result.close();
//        if (prepare != null) prepare.close();
//        if (connect != null) connect.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}




