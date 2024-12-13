package doctorModel;

public class SurgeryData {

    private int surgeryId;
    private String patientName;
    private String diagnosis;
    private String room;
    private String date;
    private String operation_time;
    private String status;
    private int patient_id;

    // Constructor
    public SurgeryData(int surgeryId, String patientName, String diagnosis, 
                       String room, String date, String operation_time, String status, int patient_id) {
        this.surgeryId = surgeryId;
        this.patientName = patientName;
        this.diagnosis = diagnosis;
        this.room = room;
        this.date = date;
        this.operation_time = operation_time;
        this.status = status;
        this.patient_id = patient_id;
    }

    // Getter and Setter for surgeryId
    public int getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(int surgeryId) {
        this.surgeryId = surgeryId;
    }
    
    // for patient_id
    public int getPatientId() {
        return patient_id;
    }

    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    // Getter and Setter for patientName
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    // Getter and Setter for diagnosis
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    // Getter and Setter for room
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    // Getter and Setter for date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter and Setter for operation_time
    public String getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(String operation_time) {
        this.operation_time = operation_time;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

