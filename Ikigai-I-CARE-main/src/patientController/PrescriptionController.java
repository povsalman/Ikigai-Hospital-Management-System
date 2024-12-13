package patientController;

import patientHandler.PrescriptionDBHandler;
import patientModel.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PrescriptionController {

    private final PrescriptionDBHandler dbHandler;

    public PrescriptionController() {
        this.dbHandler = new PrescriptionDBHandler();
    }

    public void populatePrescriptionTable(TableView<PrescriptionItem> table,
                                          TableColumn<PrescriptionItem, Integer> colId,
                                          TableColumn<PrescriptionItem, String> colName,
                                          TableColumn<PrescriptionItem, Integer> colQuantity,
                                          TableColumn<PrescriptionItem, String> colType,
                                          TableColumn<PrescriptionItem, Double> colUnitPrice,
                                          TableColumn<PrescriptionItem, Double> colTotalPrice,
                                          Patient patient) {
        try {
            // Fetch prescription items from the database
            ObservableList<PrescriptionItem> prescriptionItems = dbHandler.getPrescriptionItemsForPatient(patient.getPatientId());

            // Bind table columns to PrescriptionItem properties
            colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
            colUnitPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()).asObject());
            colTotalPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject());

            // Set the table items
            table.setItems(prescriptionItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
