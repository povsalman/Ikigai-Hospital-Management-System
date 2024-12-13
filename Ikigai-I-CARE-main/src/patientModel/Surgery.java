package patientModel;

import java.sql.Date;
import java.sql.Time;

public class Surgery {
    private final int surgeryId;
    private final String doctorName;
    private final String roomId;
    private final Date operationDate;
    private final Time operationTime;
    private final String status;

    // Constructor
    public Surgery(int surgeryId, String doctorName, String roomId, Date operationDate, Time operationTime, String status) {
        this.surgeryId = surgeryId;
        this.doctorName = doctorName;
        this.roomId = roomId;
        this.operationDate = operationDate;
        this.operationTime = operationTime;
        this.status = status;
    }

    // Getters
    public int getSurgeryId() {
        return surgeryId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getRoomId() {
        return roomId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public Time getOperationTime() {
        return operationTime;
    }

    public String getStatus() {
        return status;
    }
}
