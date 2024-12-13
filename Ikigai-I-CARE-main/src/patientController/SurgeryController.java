package patientController;

import patientModel.Surgery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

public class SurgeryController {
    private final Connection con;

    // Constructor that accepts the database connection
    public SurgeryController(Connection con) {
        this.con = con;
    }

    public ObservableList<Surgery> loadSurgeryData(int patientId) {
        ObservableList<Surgery> surgeries = FXCollections.observableArrayList();

        // SQL query to get surgery details including doctor name and other relevant data
        String query = "SELECT s.surgery_id, CONCAT(d.doc_Fname, ' ', d.doc_Lname) AS doc_Name, s.room_id, "
                     + "s.operation_date, s.operation_time, s.status "
                     + "FROM Surgery s "
                     + "JOIN Doctor d ON s.doctor_id = d.doctor_id WHERE patient_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query) ) {
        	stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            // Loop through the result set and map to Surgery model
            while (rs.next()) {
                int surgeryId = rs.getInt("surgery_id");
                String doctorName = rs.getString("doc_Name");
                String roomId = rs.getString("room_id");
                Date operationDate = rs.getDate("operation_date");
                Time operationTime = rs.getTime("operation_time");
                String status = rs.getString("status");

                // Create a Surgery object and add it to the list
                Surgery surgery = new Surgery(surgeryId, doctorName, roomId, operationDate, operationTime, status);
                surgeries.add(surgery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return surgeries;
    }
   

	public void populateSurgeryTable(int patientId,TableView<Surgery> surg_table, TableColumn<Surgery, Integer> surg_id,
			TableColumn<Surgery, String> surg_room, TableColumn<Surgery, java.util.Date> surg_date,
			TableColumn<Surgery, Time> surg_time, TableColumn<Surgery, String> surg_doc) {
		// Set the cell value factories for each column based on the Surgery model properties
        surg_id.setCellValueFactory(new PropertyValueFactory<>("surgeryId"));
        surg_room.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        surg_date.setCellValueFactory(new PropertyValueFactory<>("operationDate"));
        surg_time.setCellValueFactory(new PropertyValueFactory<>("operationTime"));
        surg_doc.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        // Load surgery data and set it to the table view
        ObservableList<Surgery> surgeries = loadSurgeryData(patientId);
        surg_table.setItems(surgeries);
	}
}
