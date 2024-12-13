package adminModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodInHandler extends BaseDatabaseHandler {

    public List<GoodIn> getAllGoodInEntries() {
        List<GoodIn> goodInEntries = new ArrayList<>();
        String query = "SELECT gi.item_id AS stock_item_id, si.item_name, msd.medsupply_type AS item_type, " +
                       "gi.quantity AS quantity_purchased, gi.purchase_price, msr.request_date AS medical_supply_request_date, " +
                       "gi.received_date AS good_in_date " +
                       "FROM GoodIn gi " +
                       "INNER JOIN StockItem si ON gi.item_id = si.item_id " +
                       "INNER JOIN MedicalSupplyRequest msr ON gi.request_id = msr.request_id " +
                       "INNER JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                goodInEntries.add(new GoodIn(
                        rs.getInt("stock_item_id"),
                        rs.getString("item_name"),
                        rs.getString("item_type"),
                        rs.getInt("quantity_purchased"),
                        rs.getDouble("purchase_price"),
                        rs.getString("medical_supply_request_date"),
                        rs.getString("good_in_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching good-in entries: " + e.getMessage());
        }
        return goodInEntries;
    }
    
    
    public double getLatestPurchasePrice(int itemId) {
        String query = "SELECT purchase_price FROM GoodIn WHERE item_id = ? ORDER BY received_date DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("purchase_price");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching purchase price: " + e.getMessage());
        }
        return 0.0; // Return 0.0 if no record is found
    }
    public boolean insertGoodInEntry(int requestId, int itemId, int quantity) {
        double purchasePrice = getLatestPurchasePrice(itemId); // Fetch the latest purchase price
        
        String query = "INSERT INTO GoodIn (request_id, item_id, quantity, purchase_price, received_date) " +
                       "VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, purchasePrice); // Use the fetched purchase price
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting into GoodIn table: " + e.getMessage());
        }
        return false;
    }


}
