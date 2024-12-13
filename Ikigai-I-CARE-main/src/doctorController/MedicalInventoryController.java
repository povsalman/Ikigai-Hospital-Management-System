package doctorController;

import doctorModel.MedicalInventoryData;
import Doctorapplication.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MedicalInventoryController {

	@FXML private Button prescription_IIDyesBtn, prescription_IIDnoBtn;
	@FXML private TextField prescription_itemID;
	@FXML private TableView<MedicalInventoryData> medicalinventory_tableView;
	@FXML private TableColumn<MedicalInventoryData, String> 
	    medicalinventory_col_itemID, 
	    medicalinventory_col_item, 
	    medicalinventory_col_type, 
	    medicalinventory_col_price, 
	    medicalinventory_col_stock, 
	    medicalinventory_col_status;

	
	private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private final AlertMessage alert = new AlertMessage();
    
    public void setMedicalInventoryComponents(
            Button prescription_IIDyesBtn, Button prescription_IIDnoBtn, TextField prescription_itemID,
            TableView<MedicalInventoryData> medicalinventory_tableView,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_itemID,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_item,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_type,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_price,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_stock,
            TableColumn<MedicalInventoryData, String> medicalinventory_col_status) {

        this.prescription_IIDyesBtn = prescription_IIDyesBtn;
        this.prescription_IIDnoBtn = prescription_IIDnoBtn;
        this.prescription_itemID = prescription_itemID;
        this.medicalinventory_tableView = medicalinventory_tableView;
        this.medicalinventory_col_itemID = medicalinventory_col_itemID;
        this.medicalinventory_col_item = medicalinventory_col_item;
        this.medicalinventory_col_type = medicalinventory_col_type;
        this.medicalinventory_col_price = medicalinventory_col_price;
        this.medicalinventory_col_stock = medicalinventory_col_stock;
        this.medicalinventory_col_status = medicalinventory_col_status;
    }
   
    
    public ObservableList<MedicalInventoryData> medicalInventoryGetData() {

        ObservableList<MedicalInventoryData> listData = FXCollections.observableArrayList();
        
        
        String sql = """
        	    SELECT 
        	        si.item_id, 
        	        si.item_name AS item, 
        	        msd.medsupply_type AS type, 
        	        si.quantity_available AS stock,        -- Aggregate stock quantities
        	        si.unit_price AS price, 
        	        CASE 
        	            WHEN si.quantity_available > si.threshold THEN 'Available' 
        	            ELSE 'Low Stock' 
        	        END AS status, 
        	        MAX(pi.prescription_id) AS prescription_id,   -- Get the latest prescription_id (if available)
        	        p.patient_id AS patient_id,               -- Get the associated patient_id (if available)
        	        p.doctor_id AS doctor_id,                 -- Get the associated doctor_id (if available)
        	        p.appointment_id AS appointment_id        -- Get the associated appointment_id (if available)
        	    FROM 
        	        StockItem si 
        	    JOIN 
        	        MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id 
        	    LEFT JOIN 
        	        PrescriptionItem pi ON si.item_id = pi.item_id 
        	    LEFT JOIN 
        	        Prescription p ON pi.prescription_id = p.prescription_id
        	    GROUP BY 
        	        si.item_id, si.item_name, msd.medsupply_type, si.unit_price, si.threshold, si.quantity_available,p.patient_id,p.doctor_id, p.appointment_id 
        	""";

        

//        // SQL query to fetch medical inventory details along with patient_id and doctor_id
//        String sql = """
//            SELECT si.item_id, si.item_name AS item, msd.medsupply_type AS type, 
//                   si.quantity_available AS stock, si.unit_price AS price, 
//                   CASE WHEN si.quantity_available > si.threshold THEN 'Available' 
//                        ELSE 'Low Stock' END AS status, 
//                   pi.prescription_id, 
//                   p.patient_id, 
//                   p.doctor_id,
//                   p.appointment_id 
//            FROM StockItem si 
//            JOIN MedicalSupplyDescription msd ON si.medsupply_id = msd.medsupply_id 
//            LEFT JOIN PrescriptionItem pi ON si.item_id = pi.item_id 
//            LEFT JOIN Prescription p ON pi.prescription_id = p.prescription_id
//        """;

        Connection connect = Database.connectDB(); // Assuming a Database class with a connectDB method
        PreparedStatement prepare = null;
        ResultSet result = null;

        try {
            // Prepare the SQL statement
            prepare = connect.prepareStatement(sql);

            // Execute the query
            result = prepare.executeQuery();

            MedicalInventoryData medicalInventoryData;

            while (result.next()) {
                // Assuming the MedicalInventoryData constructor matches these parameters
                medicalInventoryData = new MedicalInventoryData(
                    result.getInt("item_id"),       // Item ID
                    result.getInt("stock"),         // Stock Quantity
                    result.getDouble("price"),      // Unit Price
                    result.getString("item"),       // Item Name
                    result.getString("type"),       // Item Type
                    result.getString("status"),     // Status (Available/Low Stock)
                    result.getInt("prescription_id"), // Prescription ID, may be null
                    result.getInt("patient_id"),    // Patient ID, may be null
                    result.getInt("doctor_id"),      // Doctor ID, may be null
                    result.getInt("appointment_id")
                );
                // Add the data to the list
                listData.add(medicalInventoryData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return listData;
    }



    public ObservableList<MedicalInventoryData> medicalInventoryListData;

    public void medicalInventoryShowData() {
        // Fetch the data for the Medical Inventory table
        medicalInventoryListData = medicalInventoryGetData();

        // Map MedicalInventoryData class fields to TableView columns
        medicalinventory_col_itemID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        medicalinventory_col_item.setCellValueFactory(new PropertyValueFactory<>("item"));
        medicalinventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        medicalinventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        medicalinventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        medicalinventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set data to the TableView
        medicalinventory_tableView.setItems(medicalInventoryListData);
    }

    public void medicalInventorySelect() {
        // Get the selected item from the TableView
        MedicalInventoryData medicalInventoryData = medicalinventory_tableView.getSelectionModel().getSelectedItem();
        int num = medicalinventory_tableView.getSelectionModel().getSelectedIndex();

        // Check for invalid selection
        if ((num - 1) < -1 || medicalInventoryData == null) {
            return;
        }

        // Set the item ID TextField with the selected record's item ID
        prescription_itemID.setText(String.valueOf(medicalInventoryData.getItemId()));
    }
    
	
    public void prescriptionaddtoYesBtn() {
        String itemIDText = prescription_itemID.getText();

        if (itemIDText.isEmpty()) {
            alert.errorMessage("Please enter a valid Item ID.");
            return;
        }

        int itemID = Integer.parseInt(itemIDText);

        MedicalInventoryData selectedItem = medicalinventory_tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            alert.errorMessage("Please select an item from the inventory.");
            return;
        }

        int availableStock = selectedItem.getStock();
        if (availableStock <= 0) {
            alert.errorMessage("Insufficient stock for the selected item.");
            return;
        }

        int patientID = selectedItem.getPatientId();
        if (patientID <= 0) {
            alert.errorMessage("Invalid patient ID.");
            return;
        }

        // Ensure patient_id exists in the Patient table
        try (Connection connect = Database.connectDB()) {
            String checkPatientSQL = "SELECT patient_id FROM Patient WHERE patient_id = ?";
            try (PreparedStatement checkPatientStmt = connect.prepareStatement(checkPatientSQL)) {
                checkPatientStmt.setInt(1, patientID);
                ResultSet patientResult = checkPatientStmt.executeQuery();
                if (!patientResult.next()) {
                    alert.errorMessage("Patient ID does not exist.");
                    return;
                }
            }

            // Ensure prescription_id is valid or create a new prescription if necessary
            int prescriptionID;
            String fetchPrescriptionSQL = """
                SELECT prescription_id 
                FROM Prescription 
                WHERE patient_id = ? AND doctor_id = ? AND status_ = 'Pending' AND appointment_id = ?
            """;
            try (PreparedStatement fetchStmt = connect.prepareStatement(fetchPrescriptionSQL)) {
                fetchStmt.setInt(1, patientID); // Patient ID
                fetchStmt.setInt(2, selectedItem.getDoctorId());  // Doctor ID
                fetchStmt.setInt(3, selectedItem.getAppointmentId()); // Appointment ID (new)

                ResultSet result = fetchStmt.executeQuery();
                if (result.next()) {
                    prescriptionID = result.getInt("prescription_id");
                } else {
                    // Create a new prescription if one doesn't exist
                    String createPrescriptionSQL = """
                        INSERT INTO Prescription (patient_id, doctor_id, date_issued, status_, appointment_id)
                        VALUES (?, ?, CURDATE(), 'Pending', ?)
                    """;
                    try (PreparedStatement createStmt = connect.prepareStatement(createPrescriptionSQL, Statement.RETURN_GENERATED_KEYS)) {
                        createStmt.setInt(1, patientID);
                        createStmt.setInt(2, selectedItem.getDoctorId());
                        createStmt.setInt(3, selectedItem.getAppointmentId()); // Pass Appointment ID

                        createStmt.executeUpdate();

                        ResultSet generatedKeys = createStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            prescriptionID = generatedKeys.getInt(1);
                        } else {
                            alert.errorMessage("Failed to create a new prescription.");
                            return;
                        }
                    }
                }
            }

            connect.setAutoCommit(false); // Disable auto-commit for transaction management

            // Insert into PrescriptionItem
            String insertPrescriptionItemSQL = """
                INSERT INTO PrescriptionItem (prescription_id, item_id, quantity) 
                VALUES (?, ?, ?)
            """;
            try (PreparedStatement insertStmt = connect.prepareStatement(insertPrescriptionItemSQL)) {
                insertStmt.setInt(1, prescriptionID);
                insertStmt.setInt(2, itemID);
                insertStmt.setInt(3, 1); // Assuming adding one unit

                insertStmt.executeUpdate();
            }

            // Update StockItem
            String updateStockSQL = """
                UPDATE StockItem 
                SET quantity_available = quantity_available - 1 
                WHERE item_id = ? AND quantity_available > 0
            """;
            try (PreparedStatement updateStmt = connect.prepareStatement(updateStockSQL)) {
                updateStmt.setInt(1, itemID);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    connect.rollback(); // Rollback on failure
                    alert.errorMessage("Failed to update stock. Insufficient stock available.");
                    return;
                }
            }

            // Commit the transaction
            connect.commit();
            alert.successMessage("Item successfully added to prescription and stock updated.");
            medicalInventoryShowData(); // Refresh data

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("An error occurred while adding the item to the prescription.");
        }
    }


	
    public void prescriptionaddtoNoBtn() {
    	prescription_itemID.clear();
    	medicalinventory_tableView.getSelectionModel().clearSelection();

        alert.errorMessage("No changes made. The prescription details were not added.");
    }

    
	
}
