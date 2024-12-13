package doctorModel;

public class PatientTreatmentData {

	private int treatmentId;
    private String patientName;
    private String treatment;
    private String startDate;
    private String endDate;
    private String status;
    private int patient_id;

    // Constructor
    public PatientTreatmentData(int treatmentId, String patientName, String treatment, 
                            String startDate, 
                            String endDate, String status, int patient_id) {
        this.treatmentId = treatmentId;
        this.patientName = patientName;
        this.treatment = treatment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.patient_id = patient_id;
    }

    // Getter and Setter for treatmentId
    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }
    
    // Getter and Setter for patient_id
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

    // Getter and Setter for treatment
    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    // Getter and Setter for startDate
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // Getter and Setter for endDate
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
	
	
}
