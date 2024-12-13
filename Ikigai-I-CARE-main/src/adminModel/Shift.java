package adminModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shift {

    private final StringProperty adminId;
    private final StringProperty doctorId;
    private final StringProperty shiftDate;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty doctorFirstName;
    private final StringProperty doctorLastName;
    private final StringProperty adminFullName;
    private final StringProperty department;
    private final StringProperty status;
    private final StringProperty description;

    public Shift(int doctorId, String doctorFullName, int adminId, String adminFullName,
                 String startTime, String endTime, String shiftDate, String department, String status, String description) {

        this.doctorId = new SimpleStringProperty(String.valueOf(doctorId));
        this.doctorFirstName = new SimpleStringProperty(doctorFullName.split(" ")[0]); // First Name
        this.doctorLastName = new SimpleStringProperty(doctorFullName.split(" ")[1]);  // Last Name
        this.adminId = new SimpleStringProperty(String.valueOf(adminId));
        this.adminFullName = new SimpleStringProperty(adminFullName);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.shiftDate = new SimpleStringProperty(shiftDate);
        this.department = new SimpleStringProperty(department);
        this.status = new SimpleStringProperty(status);
        this.description = new SimpleStringProperty(description);
    }

    
    public String getFullName() {
        return getDoctorFirstName() + " " + getDoctorLastName();
    }
    // Getter for adminId
    public String getAdminId() {
        return adminId.get();
    }

    public StringProperty adminIdProperty() {
        return adminId;
    }

    // Getter for adminFullName
    public String getAdminFullName() {
        return adminFullName.get();
    }

    public StringProperty adminFullNameProperty() {
        return adminFullName;
    }
    
    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty doctorIdProperty() {
        return doctorId;
    }

    public StringProperty shiftDateProperty() {
        return shiftDate;
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public StringProperty doctorFirstNameProperty() {
        return doctorFirstName;
    }

    public StringProperty doctorLastNameProperty() {
        return doctorLastName;
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getDoctorId() {
        return doctorId.get();
    }

    public String getShiftDate() {
        return shiftDate.get();
    }

    public String getStartTime() {
        return startTime.get();
    }

    public String getEndTime() {
        return endTime.get();
    }

    public String getDoctorFirstName() {
        return doctorFirstName.get();
    }

    public String getDoctorLastName() {
        return doctorLastName.get();
    }

    public String getDepartment() {
        return department.get();
    }

    public String getStatus() {
        return status.get();
    }
}
