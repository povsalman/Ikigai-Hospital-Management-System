package patientModel;

public class Room {
    private int roomId;
    private String roomType;
    private Double price;
    private String availabilityStatus;

    public Room(int roomId, String roomType, Double price, String availabilityStatus) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public Double getPrice() {
        return price;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
}

