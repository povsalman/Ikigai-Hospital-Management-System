package patientController;

import patientHandler.DoctorDBHandler;
import patientModel.Doctor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DoctorController {

    private DoctorDBHandler dbHandler;

    public DoctorController() {
        this.dbHandler = new DoctorDBHandler();
    }

    public void loadDoctors(TableColumn<Doctor, Integer> doc_table_id,
                            TableColumn<Doctor, String> doc_table_name,
                            TableColumn<Doctor, String> doc_table_email,
                            TableColumn<Doctor, String> doc_table_gender,
                            TableColumn<Doctor, String> doc_table_phone,
                            TableColumn<Doctor, String> doc_table_specialize,
                            TableView<Doctor> doc_table1) {

        ObservableList<Doctor> doctors = dbHandler.loadAllDoctors();

        doc_table_id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDoctorId()).asObject());
        doc_table_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDocName()));
        doc_table_email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        doc_table_gender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        doc_table_phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        doc_table_specialize.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecialization()));

        doc_table1.setItems(doctors);
    }

    public ObservableList<Doctor> searchDoctors(String field, String value) {
        ObservableList<Doctor> doctors = dbHandler.searchDoctors(field, value);
        return doctors;
    }
}
