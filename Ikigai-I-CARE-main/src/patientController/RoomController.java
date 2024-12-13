package patientController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import patientModel.Room;

public class RoomController {

    private Connection con;

    public RoomController(Connection con) {
        this.con = con;
    }

    public void loadRoomData(TableView<Room> room_table, TableColumn<Room, Integer> room_id, 
                               TableColumn<Room, String> room_type, TableColumn<Room, Double> room_price) {
        try {
            ObservableList<Room> roomList = FXCollections.observableArrayList();
            String query = "SELECT * FROM Room";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                roomList.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getString("availability_status")
                ));
            }

            room_id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRoomId()).asObject());
            room_type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomType()));
            room_price.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

            room_table.setItems(roomList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateRoomComboBox(ComboBox<Integer> room_choose) {
        try {
            ObservableList<Integer> availableRooms = FXCollections.observableArrayList();
            String query = "SELECT room_id FROM Room WHERE availability_status = 'Available'";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                availableRooms.add(rs.getInt("room_id"));
            }

            room_choose.setItems(availableRooms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean bookRoom(int patientId, int roomId, LocalDate bookingDate) {
        try {
            // Insert booking into the Booking table
            String bookingQuery = "INSERT INTO Booking (patient_id, room_id, booking_date) VALUES (?, ?, ?)";
            PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, patientId);
            bookingStmt.setInt(2, roomId);
            bookingStmt.setDate(3, java.sql.Date.valueOf(bookingDate));
            bookingStmt.executeUpdate();

            // Update the room availability
            String updateRoomQuery = "UPDATE Room SET availability_status = 'Occupied' WHERE room_id = ?";
            PreparedStatement updateRoomStmt = con.prepareStatement(updateRoomQuery);
            updateRoomStmt.setInt(1, roomId);
            updateRoomStmt.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
