package doctorHandler;

import Doctorapplication.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import doctorModel.ProfileData;

public class ProfileHandler {

	private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    
    public ProfileHandler(DBHandler dbHandler) {
        this.connect = dbHandler.getConnection();
    }
    
    // Fetch profile information
    public ProfileData getProfileData(int doctorId) {
    	ProfileData profileData = null;
        prepare = null;
        result = null;

        try {
            String query = "SELECT d.doctor_id, CONCAT(doc_Fname, ' ', doc_Lname) AS full_name, email, mobile_number, " +
                           "gender, specialized, password, image, AVG(f.rating_stars) AS rating " +
                           "FROM Doctor d LEFT JOIN Feedback f ON d.doctor_id = f.doctor_id " +
                           "WHERE d.doctor_id = ? GROUP BY d.doctor_id";

            prepare = connect.prepareStatement(query);
            prepare.setInt(1, doctorId);
            result = prepare.executeQuery();

            if (result.next()) {
                int docId = result.getInt("doctor_id");
                String fullName = result.getString("full_name");
                String email = result.getString("email");
                String mobileNumber = result.getString("mobile_number");
                String gender = result.getString("gender");
                String specialization = result.getString("specialized");
                String password = result.getString("password");
                String imagePath = result.getString("image");
                Double rating = result.getDouble("rating"); // Retrieve the average rating

                // Create a ProfileData object with all data including rating
                profileData = new ProfileData(docId, fullName, email, mobileNumber, gender, 
                                              specialization, password, imagePath, rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return profileData;
    }

    // Update profile information
    public boolean updateProfileData(ProfileData profileData, String imagePath, boolean isPasswordChanged) {
        StringBuilder updateQuery = new StringBuilder("UPDATE Doctor SET ");
        List<Object> params = new ArrayList<>();

        // First name and last name split
        String[] nameParts = profileData.getFullName().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = (nameParts.length > 1) ? nameParts[1] : "";

        updateQuery.append("doc_Fname = ?, doc_Lname = ?, email = ?, gender = ?, mobile_number = ?, specialized = ?");
        params.add(firstName);
        params.add(lastName);
        params.add(profileData.getEmail());
        params.add(profileData.getGender());
        params.add(profileData.getMobileNumber());
        params.add(profileData.getSpecialized());

        if (isPasswordChanged) {
            updateQuery.append(", password = ?");
            params.add(profileData.getPassword());
        }

        if (imagePath != null) {
            updateQuery.append(", image = ?");
            params.add(imagePath);
        }

        updateQuery.append(" WHERE doctor_id = ?");
        params.add(profileData.getDoctorId());

        try {
            connect = Database.connectDB();
            prepare = connect.prepareStatement(updateQuery.toString());

            for (int i = 0; i < params.size(); i++) {
                prepare.setObject(i + 1, params.get(i));
            }

            return prepare.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Fetch profile image path
    public String getProfileImagePath(int doctorId) {
        String query = "SELECT image FROM Doctor WHERE doctor_id = ?";
        String imagePath = null;

        try {
            connect = Database.connectDB();
            prepare = connect.prepareStatement(query);
            prepare.setInt(1, doctorId);
            result = prepare.executeQuery();

            if (result.next()) {
                imagePath = result.getString("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return imagePath;
    }

    // Close resources
    private void closeResources() {
        try {
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
