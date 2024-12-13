package patientModel;

import java.util.Date;

public class Appointment {
    private int appointmentId;
    private String description;
    private String doctorName;
    private Date appointmentDate;
    private double price;

    public Appointment(int appointmentId, String description, String doctorName, Date appointmentDate, double price) {
        this.appointmentId = appointmentId;
        this.description = description;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.price = price;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

