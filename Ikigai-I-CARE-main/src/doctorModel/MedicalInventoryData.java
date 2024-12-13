package doctorModel;

public class MedicalInventoryData {
    private int itemId;
    private int stock;
    private double price;
    private String item;
    private String type;
    private String status;
    private int prescriptionId; // Prescription ID
    private int patientId;      // Patient ID
    private int doctorId;       // Doctor ID
    private int appointment_id;

    // Constructor
    public MedicalInventoryData(int itemId, int stock, double price, String item, String type, 
                                 String status, int prescriptionId, int patientId, int doctorId, int appointment_id) {
        this.itemId = itemId;
        this.stock = stock;
        this.price = price;
        this.item = item;
        this.type = type;
        this.status = status;
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointment_id = appointment_id;
    }

    // Getters and setters for all fields
    public int getItemId() { return itemId; }
    public int getStock() { return stock; }
    public double getPrice() { return price; }
    public String getItem() { return item; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getPrescriptionId() { return prescriptionId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public int getAppointmentId() { return appointment_id; }

    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrice(double price) { this.price = price; }
    public void setItem(String item) { this.item = item; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setAppointmentId(int appointment_id) { this.appointment_id = appointment_id; }
}


