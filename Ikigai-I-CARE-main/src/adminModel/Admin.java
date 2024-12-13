package adminModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
    private final StringProperty adminId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty phoneNo;

    // Constructor
    public Admin(int adminId, String firstName, String lastName, String email, String password, String phoneNo) {
        this.adminId = new SimpleStringProperty(String.valueOf(adminId)); // Convert int to StringProperty
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phoneNo = new SimpleStringProperty(phoneNo);
    }

    // Getters and setters for StringProperty
    public StringProperty adminIdProperty() {
        return adminId;
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

    public StringProperty phoneNoProperty() {
        return phoneNo;
    }

    // Plain getters
    public String getAdminId() {
        return adminId.get();
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

    public String getPassword() {
        return password.get();
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    // Setters for StringProperty
    public void setAdminId(String adminId) {
        this.adminId.set(adminId);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo.set(phoneNo);
    }
}
