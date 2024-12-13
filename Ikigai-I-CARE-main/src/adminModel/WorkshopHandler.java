package adminModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkshopHandler extends BaseDatabaseHandler {

	public List<Workshop> getAllWorkshops() {
	    List<Workshop> workshops = new ArrayList<>();
	    String query = "SELECT w.workshop_id, CONCAT(a.admin_Fname, ' ', a.admin_Lname) AS admin_name, " +
	                   "w.workshop_name, w.speaker, w.description, w.workshop_date, w.start_time, w.end_time, w.location " +
	                   "FROM Workshop w INNER JOIN Admin a ON w.admin_id = a.admin_id";

	    try (ResultSet rs = executeQuery(query)) {
	        while (rs.next()) {
	            workshops.add(new Workshop(
	                rs.getInt("workshop_id"),
	                rs.getString("admin_name"),
	                rs.getString("workshop_name"),
	                rs.getString("speaker"),
	                rs.getString("description"),
	                rs.getString("workshop_date"),
	                rs.getString("start_time"),
	                rs.getString("end_time"),
	                rs.getString("location")
	            ));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching workshop data: " + e.getMessage());
	    }
	    return workshops;
	}

	public boolean addWorkshop(int adminId, String workshopName, String speaker, String description, 
            String date, String startTime, String endTime, String location) {
          String query = "INSERT INTO Workshop (admin_id, workshop_name, speaker, description, workshop_date, start_time, end_time, location) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, adminId);
        stmt.setString(2, workshopName);
        stmt.setString(3, speaker);
        stmt.setString(4, description);
        stmt.setString(5, date);
        stmt.setString(6, startTime);
        stmt.setString(7, endTime);
        stmt.setString(8, location);

       return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
      System.err.println("Error adding workshop: " + e.getMessage());
      return false;
}
        
}

	public List<Workshop> checkForClashes(String date, String startTime, String endTime, String venue) {
	    List<Workshop> clashes = new ArrayList<>();
	    String query = "SELECT workshop_id, admin_id, workshop_name, speaker, description, workshop_date, start_time, end_time, location " +
	                   "FROM Workshop " +
	                   "WHERE workshop_date = ? AND location = ? AND " +
	                   "(" +
	                   "(? >= start_time AND ? < end_time) OR " +  // Start time overlaps
	                   "(? > start_time AND ? <= end_time) OR " +  // End time overlaps
	                   "(? <= start_time AND ? >= end_time)" +     // Complete overlap
	                   ")";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, date); // Workshop date
	        stmt.setString(2, venue); // Workshop venue
	        stmt.setString(3, startTime); // New workshop start time
	        stmt.setString(4, startTime); // New workshop start time
	        stmt.setString(5, endTime); // New workshop end time
	        stmt.setString(6, endTime); // New workshop end time
	        stmt.setString(7, startTime); // New workshop start time
	        stmt.setString(8, endTime); // New workshop end time

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            clashes.add(new Workshop(
	                rs.getInt("workshop_id"),
	                null, // No need to fetch admin name if you're not using it
	                rs.getString("workshop_name"),
	                rs.getString("speaker"),
	                rs.getString("description"),
	                rs.getString("workshop_date"),
	                rs.getString("start_time"),
	                rs.getString("end_time"),
	                rs.getString("location")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return clashes;
	}



	
	public List<Workshop> getWorkshopsByDateAndVenue(String date, String venue) {
	    List<Workshop> workshops = new ArrayList<>();
	    String query = "SELECT * FROM Workshop WHERE workshop_date = ? AND location = ? ORDER BY start_time";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, date);
	        stmt.setString(2, venue);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            workshops.add(new Workshop(
	                    rs.getInt("workshop_id"),
	                    String.valueOf(rs.getInt("admin_id")),
	                    rs.getString("workshop_name"),
	                    rs.getString("speaker"),
	                    rs.getString("description"),
	                    rs.getString("workshop_date"),
	                    rs.getString("start_time"),
	                    rs.getString("end_time"),
	                    rs.getString("location")));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return workshops;
	}

	public List<String> getAvailableVenues(String date, String startTime, String endTime) {
	    List<String> venues = new ArrayList<>();
	    String query = "SELECT DISTINCT location FROM Workshop WHERE workshop_date != ? OR "
	                 + "(? >= end_time OR ? <= start_time)";

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, date);
	        stmt.setString(2, startTime);
	        stmt.setString(3, endTime);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            venues.add(rs.getString("location"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return venues;
	}

     }
