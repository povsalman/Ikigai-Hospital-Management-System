package doctorModel;

import java.sql.Date;

public class FeedbackData {

    private int feedbackId;
    private String patient;
    private String treatment;
    private String rating;
    private String description;
    private Date date;
    private String status;
    

    // Constructor to initialize all fields
    public FeedbackData(int feedbackId, String patient, String treatment, String rating, String description, Date date, String status) {
        this.feedbackId = feedbackId;
        this.patient = patient;
        this.treatment = treatment;
        this.rating = rating;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    // Getter and Setter for feedbackId
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    // Getter and Setter for patient
    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    // Getter and Setter for treatment
    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    // Getter and Setter for rating
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

