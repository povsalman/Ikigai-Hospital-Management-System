package adminModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DoctorRequest {
    private final SimpleIntegerProperty requestId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty specialization;
    private final StringProperty gender;
    private final StringProperty phone;

    public DoctorRequest(int requestId, String firstName, String lastName, String email, String password, String specialization, String gender, String phone) {
        this.requestId = new SimpleIntegerProperty(requestId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.specialization = new SimpleStringProperty(specialization);
        this.gender = new SimpleStringProperty(gender);
        this.phone = new SimpleStringProperty(phone);
    }

    // Getters for properties
    public SimpleIntegerProperty requestIdProperty() {
        return requestId;
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

    public StringProperty passwordProperty() {
        return password;
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

    // Getters and Setters for values
    public int getRequestId() {
        return requestId.get();
    }

    public void setRequestId(int requestId) {
        this.requestId.set(requestId);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getSpecialization() {
        return specialization.get();
    }

    public void setSpecialization(String specialization) {
        this.specialization.set(specialization);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
}
