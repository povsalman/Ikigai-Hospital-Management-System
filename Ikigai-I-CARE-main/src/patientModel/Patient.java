package patientModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import patientHandler.DBHandler;

public class Patient {
	private static Patient instance;
    private int patientId;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String phoneNo;
    private String email;
    private String password;
    private String medicalDetails;
    private String image;

    // Constructor to initialize using patientId
    public Patient(int patientId, Connection con) {
        this.patientId = patientId;
        fetchPatientDetails(con);
    }
    
    public static Patient getInstance() {
        if (instance == null) {
            synchronized (DBHandler.class) {
                if (instance == null) {
                    instance = new Patient(1,DBHandler.getInstance().getConnection());
                }
            }
        }
        return instance;
    }

    // Fetch patient details from the database
    public void fetchPatientDetails(Connection con) {
        String query = "SELECT * FROM Patient WHERE patient_id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {

            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.firstName = resultSet.getString("first_name");
                this.lastName = resultSet.getString("last_name");
                this.gender = resultSet.getString("gender");
                this.address = resultSet.getString("address");
                this.phoneNo = resultSet.getString("phone_no");
                this.email = resultSet.getString("email");
                this.password = resultSet.getString("_password");
                this.medicalDetails = resultSet.getString("medical_details");
                this.image = resultSet.getString("image");
            } else {
                throw new IllegalArgumentException("No patient found with ID: " + patientId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching patient details: " + e.getMessage());
        }
    }

    // Getters for the patient attributes
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int id) {
        this.patientId = id;
        fetchPatientDetails(DBHandler.getInstance().getConnection());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMedicalDetails() {
        return medicalDetails;
    }

    public String getImage() {
        return image;
    }
    
    public String getName() {
    	return firstName+' '+lastName;
    }
}
