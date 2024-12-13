
package doctorModel;

import java.math.BigDecimal;
import java.sql.Date;


public class AppointmentData {
    
    private Integer appointmentID;
    private Integer patientID;
    private Integer doctorID;
    private Date appointmentDate;
    private Date modfied_date;
    private String status;
    private BigDecimal price;
    private String firstName;
    private String gender;
    private String phoneNo;
    private String medicalDetails;
    private String diagnosis;
    private String treatment;
    
    // Constructor for Appointment Dashboard Table
    public AppointmentData(Integer appointmentID, String firstName,
            String medicalDetails, Date appointmentDate, String status) {
        this.appointmentID = appointmentID;
        this.firstName = firstName;
        this.medicalDetails = medicalDetails;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

        
    // Constructor for Appointment Tab Table
    public AppointmentData(Integer appointmentId, Integer patientId, Integer doctorId, 
                           Date appointmentDate, Date modifiedDate, String status, BigDecimal price,
                           String firstName, String gender, String phoneNo, String medicalDetails) {
        this.appointmentID = appointmentId;
        this.patientID = patientId;
        this.doctorID = doctorId;
        this.appointmentDate = appointmentDate;
        this.modfied_date = modifiedDate;
        this.status = status;
        this.price = price;
        this.firstName = firstName;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.medicalDetails = medicalDetails;
    }

    // Getters and Setters
    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public Date getModifiedDate() {
        return modfied_date;
    }

    public void setModifiedDate(Date modfied_date) {
        this.appointmentDate = modfied_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMedicalDetails() {
        return medicalDetails;
    }

    public void setMedicalDetails(String medicalDetails) {
        this.medicalDetails = medicalDetails;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }
    

    
    
    
    
}
