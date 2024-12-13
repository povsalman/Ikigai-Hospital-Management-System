package patientHandler;

import patientModel.PrescriptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrescriptionDBHandler {

    private final Connection con;

    public PrescriptionDBHandler() {
        this.con = DBHandler.getInstance().getConnection();
    }

    public ObservableList<PrescriptionItem> getPrescriptionItemsForPatient(int patientId) {
        ObservableList<PrescriptionItem> prescriptionItems = FXCollections.observableArrayList();
        String query = """
                SELECT 
                    pi.prescription_item_id, 
                    si.medsupply_name, 
                    pi.quantity, 
                    msd.medsupply_type, 
                    msd.unit_price, 
                    (pi.quantity * msd.unit_price) AS total_price
                FROM 
                    PrescriptionItem pi
                JOIN 
                    MedicalSupply si 
                ON 
                    pi.item_id = si.medsupply_id
                JOIN 
                    MedicalSupplyDescription msd 
                ON 
                    si.medsupply_id = msd.medsupply_id
                WHERE 
                    pi.prescription_id IN (
                        SELECT prescription_id 
                        FROM Prescription 
                        WHERE patient_id = ?
                    )
                """;
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("prescription_item_id");
                String name = rs.getString("medsupply_name");
                int quantity = rs.getInt("quantity");
                String type = rs.getString("medsupply_type");
                double unitPrice = rs.getDouble("unit_price");
                double totalPrice = rs.getDouble("total_price");

                prescriptionItems.add(new PrescriptionItem(id, name, quantity, type, unitPrice, totalPrice, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prescriptionItems;
    }
}
