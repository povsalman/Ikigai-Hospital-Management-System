package doctorModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import doctorHandler.DBHandler;


public class ProfileData {
	private static ProfileData instance;
    private int doctorId;
    private String fullName; // Combines first name and last name
    private String email;
    private String gender;
    private String mobileNumber;
    private String password;
    private String specialized;
    private String image; // Path to the profile image
    private Double rating;

    public static ProfileData getInstance() {
        if (instance == null) {
            synchronized (DBHandler.class) {
                if (instance == null) {
                    instance = new ProfileData(1,DBHandler.getInstance().getConnection());
                }
            }
        }
        return instance;
    }
    
    // Constructor
    public ProfileData(int doctorId, Connection con) {
    	this.doctorId = doctorId;
        fetchDoctorDetails(con);
    }
    
    // Fetch patient details from the database
    public void fetchDoctorDetails(Connection con) {
        String query = "SELECT * FROM Doctor WHERE doctor_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {

            statement.setInt(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.fullName = resultSet.getString("doc_Fname")+' '+resultSet.getString("doc_Lname");
                this.email = resultSet.getString("email");
                this.gender = resultSet.getString("gender");
                this.mobileNumber = resultSet.getString("mobile_number");
                this.email = resultSet.getString("email");
                this.password = resultSet.getString("password");
                this.specialized = resultSet.getString("specialized");
                this.image = resultSet.getString("image");
            } else {
                throw new IllegalArgumentException("No patient found with ID: " + doctorId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching patient details: " + e.getMessage());
        }
    }
    
    public ProfileData(int doctorId, String fullName, String email, String gender, 
                       String mobileNumber, String password, String specialized, String image, Double rating) {
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.specialized = specialized;
        this.image = image;
        this.rating = rating; // Initialize the rating
    }

    public ProfileData(int doctorId, String fullName, String email, String gender, 
            String mobileNumber, String password, String specialization, 
            String imagePath) {
		this.doctorId = doctorId;
		this.fullName = fullName;
		this.email = email;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.specialized = specialization;
		this.image = imagePath;
		}
    
    
    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
        fetchDoctorDetails(DBHandler.getInstance().getConnection());
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialized() {
        return specialized;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRating(Double rating) { // Setter for rating
        this.rating = rating;
    }
    
    public Double getRating() { // Getter for rating
        return rating;
    }
    
    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return "ProfileData{" +
                "doctorId=" + doctorId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", password='" + password + '\'' +
                ", specialized='" + specialized + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
