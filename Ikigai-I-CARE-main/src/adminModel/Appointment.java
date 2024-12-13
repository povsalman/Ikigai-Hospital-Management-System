package adminModel;

public class Appointment {
    private final int appointmentId;
    private final int doctorId;
    private final String appointmentDate;
    private final String status;
    private final String description;

    public Appointment(int appointmentId, int doctorId, String appointmentDate, String status, String description) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.description = description;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
