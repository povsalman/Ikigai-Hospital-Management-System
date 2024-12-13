package adminModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodOutHandler extends BaseDatabaseHandler {

    public List<GoodOut> getAllGoodOutEntries() {
        List<GoodOut> goodOutEntries = new ArrayList<>();
        String query = "SELECT go.item_id AS stock_item_id, si.item_name, msd.medsupply_type AS item_type, " +
                       "go.quantity AS quantity_removed, gi.purchase_price AS purchase_price, go.sale_price, " +
                       "go.issued_date AS good_out_date " +
                       "FROM GoodOut go " +
                       "INNER JOIN StockItem si ON go.item_id = si.item_id " +
                       "LEFT JOIN GoodIn gi ON go.item_id = gi.item_id " +
                       "INNER JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id " +
                       "WHERE gi.received_date = (SELECT MAX(received_date) FROM GoodIn WHERE GoodIn.item_id = go.item_id)";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                goodOutEntries.add(new GoodOut(
                        rs.getInt("stock_item_id"),
                        rs.getString("item_name"),
                        rs.getString("item_type"),
                        rs.getInt("quantity_removed"),
                        rs.getDouble("purchase_price"),
                        rs.getDouble("sale_price"),
                        rs.getString("good_out_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching good-out entries: " + e.getMessage());
        }
        return goodOutEntries;
    }
}
