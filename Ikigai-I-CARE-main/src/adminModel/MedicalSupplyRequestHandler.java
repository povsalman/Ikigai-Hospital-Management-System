package adminModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MedicalSupplyRequestHandler extends BaseDatabaseHandler{

	public List<MedicalSupplyRequest> getAllMedicalSupplyRequests() {
	    List<MedicalSupplyRequest> requests = new ArrayList<>();
	    String query = "SELECT " +
	                   "a.admin_id AS admin_id, " +
	                   "CONCAT(a.admin_Fname, ' ', a.admin_Lname) AS admin_fullname, " +
	                   "si.item_id AS item_id, " +
	                   "si.item_name, " +
	                   "msd.description AS item_description, " +
	                   "msd.medsupply_type AS item_type, " +
	                   "msr.quantity_requested AS quantity_requested, " +
	                   "msd.unit_price, " +
	                   "(msr.quantity_requested * msd.unit_price) AS total_price, " +
	                   "msr.request_date " +
	                   "FROM MedicalSupplyRequest msr " +
	                   "INNER JOIN Admin a ON msr.admin_id = a.admin_id " +
	                   "INNER JOIN StockItem si ON msr.item_id = si.item_id " +
	                   "INNER JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id";

	    try (ResultSet rs = executeQuery(query)) {
	        while (rs.next()) {
	            requests.add(new MedicalSupplyRequest(
	                rs.getString("admin_fullname"),
	                rs.getInt("item_id"),
	                rs.getString("item_name"),
	                rs.getString("item_type"),
	                rs.getInt("quantity_requested"),
	                rs.getDouble("unit_price"),
	                rs.getDouble("total_price"),
	                rs.getString("request_date")
	            ));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching medical supply requests: " + e.getMessage());
	    }
	    return requests;
	}
	
	
	public int addMedicalSupplyRequestAndGetId(int adminId, int itemId, int quantity) {
	    String insertQuery = "INSERT INTO MedicalSupplyRequest (admin_id, item_id, quantity_requested, request_date) VALUES (?, ?, ?, NOW())";
	    String selectQuery = "SELECT MAX(request_id) AS last_request_id FROM MedicalSupplyRequest";

	    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
	        // Insert the request
	        insertStmt.setInt(1, adminId);
	        insertStmt.setInt(2, itemId);
	        insertStmt.setInt(3, quantity);
	        int rowsAffected = insertStmt.executeUpdate();

	        if (rowsAffected > 0) {
	            // Fetch the most recent request_id
	            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
	                ResultSet rs = selectStmt.executeQuery();
	                if (rs.next()) {
	                    return rs.getInt("last_request_id");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error adding medical supply request: " + e.getMessage());
	    }
	    return -1; // Return -1 if the operation fails
	}



}
