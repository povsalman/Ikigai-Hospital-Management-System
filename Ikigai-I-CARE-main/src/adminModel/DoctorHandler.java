package adminModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoctorHandler extends BaseDatabaseHandler {

    public DoctorHandler() {
        super();
    }

    public Doctor getAvailableDoctorById(int doctorId) {
        String query = "SELECT d.doctor_id AS doctor_ID, " +
                       "d.doc_Fname AS firstName, d.doc_Lname AS lastName, " +
                       "d.email AS email, d.specialized AS specialization, " +
                       "d.gender AS gender,s.status AS shift_status " +
                       "FROM Doctor d " +
                       "LEFT JOIN Shift s ON d.doctor_id = s.doctor_id " +
                       "WHERE d.doctor_id = ? AND (s.status = 'Available' OR s.status IS NULL)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId); // Set the doctor ID in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return a Doctor object with data from the database
                    return new Doctor(
                        rs.getInt("doctor_ID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getString("gender"),
                        null, // Phone is not included in this query
                        rs.getString("shift_status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor by ID: " + e.getMessage());
        }

        // Return null if no doctor is found or an error occurs
        return null;
    }

    //Appointment checking to see if a doctor has appointment or not
    
    public boolean hasAppointments(int doctorId) {
        String query = "SELECT COUNT(a.appointment_id) AS appointment_count " +
                       "FROM Doctor d " +
                       "LEFT JOIN Appointment a ON d.doctor_id = a.doctor_id " +
                       "WHERE d.doctor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("appointment_count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking appointments for doctor: " + e.getMessage());
        }
        return false;
    }

    public List<Appointment> getDoctorAppointments(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT a.appointment_id, a.appointment_date, a.status_, a.description " +
                       "FROM Appointment a " +
                       "WHERE a.doctor_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("appointment_id"),
                    doctorId,
                    rs.getDate("appointment_date").toString(),
                    rs.getString("status_"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor appointments: " + e.getMessage());
        }
        return appointments;
    }

    
    
 // Fetch all doctors from the database
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT doctor_id, doc_Fname, doc_Lname, email, specialized, gender, mobile_number FROM Doctor";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                doctors.add(new Doctor(
                    resultSet.getInt("doctor_id"),
                    resultSet.getString("doc_Fname"),
                    resultSet.getString("doc_Lname"),
                    resultSet.getString("email"),
                    resultSet.getString("specialized"),
                    resultSet.getString("gender"),
                    resultSet.getString("mobile_number"),
                    null // No shiftStatus for this query
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor data: " + e.getMessage());
        }

        return doctors;
    }
    
    public boolean deleteDoctorById(int doctorId) {
        String query = "DELETE FROM Doctor WHERE doctor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, doctorId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if a row was deleted
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
        }
        return false;
    }
    
    
    public boolean updateDoctorById(int doctorId, String field, String newValue) {
        // Mapping of user-friendly names to database column names
        Map<String, String> fieldMapping = Map.of(
            "First Name", "doc_Fname",
            "Last Name", "doc_Lname",
            "Email", "email",
            "Specialization", "specialized",
            "Gender", "gender",
            "Phone", "mobile_number"
        );

        // Get the corresponding column name
        String columnName = fieldMapping.get(field);

        if (columnName == null) {
            System.err.println("Invalid field: " + field);
            return false;
        }

        // Build the query with the validated column name
        String query = "UPDATE Doctor SET " + columnName + " = ? WHERE doctor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newValue);
            statement.setInt(2, doctorId);
            return statement.executeUpdate() > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    ///////////////////// DOCTOR REQUEST QUERIES //////////////////////
    
    public List<DoctorRequest> getAllDoctorRequests() {
        List<DoctorRequest> requests = new ArrayList<>();
        String query = "SELECT request_id, doc_Fname, doc_Lname, email, password, specialized, gender, mobile_number FROM DoctorRequest";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Add request to list, setting password to null to avoid displaying it in the admin view
                requests.add(new DoctorRequest(
                    rs.getInt("request_id"),
                    rs.getString("doc_Fname"),
                    rs.getString("doc_Lname"),
                    rs.getString("email"),
                    null, // Password is excluded for admin view
                    rs.getString("specialized"),
                    rs.getString("gender"),
                    rs.getString("mobile_number")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor requests: " + e.getMessage());
        }

        return requests;
    }


    
    public boolean approveDoctorRequest(int requestId) {
        // Query to fetch all details including the password
        String selectQuery = "SELECT doc_Fname, doc_Lname, email, password, specialized, gender, mobile_number " +
                             "FROM DoctorRequest WHERE request_id = ?";
        String insertQuery = "INSERT INTO Doctor (doc_Fname, doc_Lname, email, password, specialized, gender, mobile_number) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String deleteQuery = "DELETE FROM DoctorRequest WHERE request_id = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {

            // Fetch the doctor request details
            selectStmt.setInt(1, requestId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Use the fetched password directly for insertion
                String password = rs.getString("password");

                // Set parameters for the insert query
                insertStmt.setString(1, rs.getString("doc_Fname"));
                insertStmt.setString(2, rs.getString("doc_Lname"));
                insertStmt.setString(3, rs.getString("email"));
                insertStmt.setString(4, password); // Insert the password
                insertStmt.setString(5, rs.getString("specialized"));
                insertStmt.setString(6, rs.getString("gender"));
                insertStmt.setString(7, rs.getString("mobile_number"));

                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    // Delete the request from the DoctorRequest table
                    deleteStmt.setInt(1, requestId);
                    deleteStmt.executeUpdate();
                    return true;
                }
            } else {
                System.err.println("No request found for ID: " + requestId);
            }
        } catch (SQLException e) {
            System.err.println("Error approving doctor request with ID: " + requestId);
            e.printStackTrace();
        }

        return false;
    }


    public boolean rejectDoctorRequest(int requestId) {
        String query = "DELETE FROM DoctorRequest WHERE request_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    
    //////////////////// Available Doctor for shift
 // Fetch available doctors
    public List<Doctor> getAvailableDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT d.doctor_id AS doctor_ID, " +
                       "CONCAT(d.doc_Fname, ' ', d.doc_Lname) AS doctor_fullname, " +
                       "d.email AS email, d.specialized AS specialized, " +
                       "d.gender AS gender, s.status AS shift_status " +
                       "FROM Doctor d " +
                       "LEFT JOIN Shift s ON d.doctor_id = s.doctor_id " +
                       "WHERE s.status = 'Available' OR s.status IS NULL";

        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                int doctorId = rs.getInt("doctor_ID");
                String fullName = rs.getString("doctor_fullname");
                String email = rs.getString("email");
                String specialization = rs.getString("specialized");
                String gender = rs.getString("gender");
                String shiftStatus = rs.getString("shift_status");

                String[] nameParts = fullName.split(" ", 2);
                String firstName = nameParts.length > 0 ? nameParts[0] : "";
                String lastName = nameParts.length > 1 ? nameParts[1] : "";

                doctors.add(new Doctor(
                    doctorId,
                    firstName,
                    lastName,
                    email,
                    specialization,
                    gender,
                    null, // Phone not included in this query
                    shiftStatus
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available doctors: " + e.getMessage());
        }
        return doctors;
    }}


  





