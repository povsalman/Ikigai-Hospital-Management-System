package doctorModel;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrescriptionData {
    private int prescriptionId;
    private String patient;
    private String treatment;
    private String item;
    private final StringProperty status_;
    private double price;
    private Date date_issued;
    private int quantity;

    public PrescriptionData(int prescriptionId, String patient, String treatment, String item, String status_, double price, int quantity, Date date_issued) {
        this.prescriptionId = prescriptionId;
        this.patient = patient;
        this.treatment = treatment;
        this.item = item;
        this.status_ = new SimpleStringProperty(status_);  // Initialize the status_ field
        this.price = price;
        this.quantity = quantity;
        this.date_issued = date_issued;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStatus_() {
        return status_.get();
    }

    public void setStatus_(String status_) {
        this.status_.set(status_);
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public Date getDateIssued() {
        return date_issued;
    }

    public void setDateIssued(Date date_issued) {
        this.date_issued = date_issued;
    }
    
}
