package adminModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockItemHandler extends BaseDatabaseHandler {

    public List<StockItem> getAllStockItems() {
        List<StockItem> stockItems = new ArrayList<>();
        String query = "SELECT si.item_id, si.item_name, msd.description, msd.medsupply_type, " +
                       "si.quantity_available, si.threshold, si.unit_price " +
                       "FROM StockItem si " +
                       "LEFT JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stockItems.add(new StockItem(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("description"),
                        rs.getString("medsupply_type"),
                        rs.getInt("quantity_available"),
                        rs.getInt("threshold"),
                        rs.getDouble("unit_price")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching stock items: " + e.getMessage());
        }
        return stockItems;
    }
    
    
    public List<StockItem> getLowStockItems() {
        List<StockItem> lowStockItems = new ArrayList<>();
        String query = "SELECT item_id, item_name, quantity_available, threshold FROM StockItem WHERE quantity_available <= threshold";

        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                lowStockItems.add(new StockItem(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity_available"),
                    rs.getInt("threshold")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching low stock items: " + e.getMessage());
        }
        return lowStockItems;
    }
    
    public StockItem getStockItemById(int itemId) {
    	String query = "SELECT si.item_id, si.item_name, si.quantity_available, si.threshold, si.unit_price, msd.description, msd.medsupply_type " +
                "FROM StockItem si " +
                "LEFT JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id " +
                "WHERE si.item_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new StockItem(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getString("medsupply_type"),
                    rs.getInt("quantity_available"),
                    rs.getInt("threshold"),
                    rs.getDouble("unit_price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStockQuantity(int itemId, int quantityOrdered) {
        String query = "UPDATE StockItem SET quantity_available = quantity_available + ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantityOrdered);
            stmt.setInt(2, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock quantity: " + e.getMessage());
        }
        return false;
    }


}
