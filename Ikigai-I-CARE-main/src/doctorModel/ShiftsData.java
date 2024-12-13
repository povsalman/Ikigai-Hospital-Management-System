package doctorModel;

public class ShiftsData {
    private int shiftId;
    private String doctorAssigned;
    private String department;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String status;
    private int doctor_id;

    public ShiftsData(int shiftId, String doctorAssigned, String department, String description, String date, String startTime, String endTime, String status, int doctor_id) {
        this.shiftId = shiftId;
        this.doctorAssigned = doctorAssigned;
        this.department = department;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.doctor_id = doctor_id; 
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getDoctorAssigned() {
        return doctorAssigned;
    }

    public void setDoctorAssigned(String doctorAssigned) {
        this.doctorAssigned = doctorAssigned;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getDoctorId() {
        return doctor_id;
    }

    public void setDoctorId(int doctor_id) {
        this.doctor_id = doctor_id;
    }
    
    

}
