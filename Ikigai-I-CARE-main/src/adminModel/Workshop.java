package adminModel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Workshop {
    private final IntegerProperty workshopId;
    private final StringProperty adminName; // Display Admin Name from admin_id
    private final StringProperty workshopName;
    private final StringProperty speaker;
    private final StringProperty description;
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty location;

    public Workshop(int workshopId, String adminName, String workshopName, String speaker, 
                    String description, String date, String startTime, String endTime, String location) {
        this.workshopId = new SimpleIntegerProperty(workshopId);
        this.adminName = new SimpleStringProperty(adminName);
        this.workshopName = new SimpleStringProperty(workshopName);
        this.speaker = new SimpleStringProperty(speaker);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.location = new SimpleStringProperty(location);
    }

    // Workshop ID
    public int getWorkshopId() {
        return workshopId.get();
    }

    public String getWorkshopIdAsString() {
        return String.valueOf(workshopId.get());
    }

    public IntegerProperty workshopIdProperty() {
        return workshopId;
    }

    // Admin Name
    public String getAdminName() {
        return adminName.get();
    }

    public StringProperty adminNameProperty() {
        return adminName;
    }

    // Workshop Name
    public String getWorkshopName() {
        return workshopName.get();
    }

    public StringProperty workshopNameProperty() {
        return workshopName;
    }

    // Speaker
    public String getSpeaker() {
        return speaker.get();
    }

    public StringProperty speakerProperty() {
        return speaker;
    }

    // Description
    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // Date
    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    // Start Time
    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    // End Time
    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    // Location
    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }
}
