package adminModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
    private final SimpleIntegerProperty doctorId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty specialization;
    private final StringProperty gender;
    private final StringProperty phone;
    private final StringProperty shiftStatus;

    // Constructor
    public Doctor(int doctorId, String firstName, String lastName, String email, 
                  String specialization, String gender, String phone, String shiftStatus) {
        this.doctorId = new SimpleIntegerProperty(doctorId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.specialization = new SimpleStringProperty(specialization);
        this.gender = new SimpleStringProperty(gender);
        this.phone = new SimpleStringProperty(phone);
        this.shiftStatus = new SimpleStringProperty(shiftStatus);
    }

    // Full name (computed)
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getShiftStatus() {
        return shiftStatus.get();
    }

    public StringProperty shiftStatusProperty() {
        return shiftStatus;
    }

    public int getDoctorId() {
        return doctorId.get();
    }

    public String getDoctorIdAsString() {
        return String.valueOf(doctorId.get());
    }

    public IntegerProperty doctorIdProperty() {
        return doctorId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty specializationProperty() {
        return specialization;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getSpecialization() {
        return specialization.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getPhone() {
        return phone.get();
    }
}
