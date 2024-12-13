package patientModel;

import java.time.LocalDate;

public class Feedback {
    private int patientId;
    private int doctorId;
    private LocalDate feedbackDate;
    private String description;
    private int rating;
    private String status;

    // Constructor
    public Feedback(int patientId, int doctorId, LocalDate feedbackDate, String description, int rating, String status) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.feedbackDate = feedbackDate;
        this.description = description;
        this.rating = rating;
        this.status = status;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
