package patientController;

import patientHandler.BillingDBHandler;
import patientModel.*;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class BillingController {

    private BillingDBHandler dbHandler;
    private Patient patient;
    private Set<Integer> addedAppointments = new HashSet<>();
    private Set<Integer> addedRooms = new HashSet<>();
    private Set<Integer> addedPrescriptions = new HashSet<>();
    private Set<Integer> addedTreatments = new HashSet<>();
    private ObservableList<String> billItems;

    public BillingController(Patient patient) {
        this.patient = patient;
        this.dbHandler = new BillingDBHandler();
        this.billItems = javafx.collections.FXCollections.observableArrayList();
    }

    public void addAppointmentToBill(Label totalBillAmount, ListView<String> billList) {
        try {
            ObservableList<Appointment> appointments = dbHandler.getUnpaidAppointments(patient.getPatientId());
            double total = calculateTotal();

            for (Appointment appointment : appointments) {
                int id = appointment.getAppointmentId();
                if (addedAppointments.contains(id)) continue;
                String description = appointment.getDescription();
                double price = appointment.getPrice();
                String item = "Appointment: " + description + " - $" + price;
                billItems.add(item);
                total += price;
                addedAppointments.add(id);
            }

            billList.setItems(billItems);
            totalBillAmount.setText("$" + total);

        } catch (SQLException e) {
            showAlert("Error", "Failed to add appointments to the bill.");
            e.printStackTrace();
        }
    }

    public void addRoomToBill(Label totalBillAmount, ListView<String> billList) {
        try {
            ObservableList<Room> rooms = dbHandler.getUnpaidRoomBookings(patient.getPatientId());
            double total = calculateTotal();

            for (Room room : rooms) {
                int id = room.getRoomId();
                if (addedRooms.contains(id)) continue;

                String roomType = room.getRoomType();
                double price = room.getPrice();
                String item = "Room: " + roomType + " - $" + price;
                billItems.add(item);
                total += price;

                addedRooms.add(id);
            }

            billList.setItems(billItems);
            totalBillAmount.setText("$" + total);

        } catch (SQLException e) {
            showAlert("Error", "Failed to add room bookings to the bill.");
            e.printStackTrace();
        }
    }

    public void addPrescriptionToBill(Label totalBillAmount, ListView<String> billList) {
        try {
            ObservableList<PrescriptionItem> prescriptions = dbHandler.getUnpaidPrescriptions(patient.getPatientId());
            double total = calculateTotal();

            for (PrescriptionItem prescription : prescriptions) {
                int itemId = prescription.getId();
                if (addedPrescriptions.contains(itemId)) continue;

                String itemName = prescription.getName();
                int requiredQuantity = prescription.getQuantity();
                int availableQuantity = prescription.getavailableQuantity();
                double unitPrice = prescription.getUnitPrice();
                double totalPrice = requiredQuantity * unitPrice;

                if (requiredQuantity > availableQuantity) {
                    showAlert("Insufficient Stock", "Not enough stock for " + itemName + ". Only " + availableQuantity + " available.");
                    continue;
                }

                String item = "Prescription: " + itemName + " (x" + requiredQuantity + ") - $" + totalPrice;
                billItems.add(item);
                total += totalPrice;
                addedPrescriptions.add(itemId);
            }

            billList.setItems(billItems);
            totalBillAmount.setText("$" + total);

        } catch (SQLException e) {
            showAlert("Error", "Failed to add prescription items to the bill.");
            e.printStackTrace();
        }
    }

    public void addTreatmentToBill(Label totalBillAmount, ListView<String> billList) {
        try {
            ObservableList<PatientTreatment> treatments = dbHandler.getUnpaidTreatments(patient.getPatientId());
            double total = calculateTotal();

            for (PatientTreatment treatment : treatments) {
                int id = treatment.getId();
                if (addedTreatments.contains(id)) continue;

                double price = treatment.getPrice();
                String item = "Treatment: " + id + " - $" + price;
                billItems.add(item);
                total += price;
                addedTreatments.add(id);
            }

            billList.setItems(billItems);
            totalBillAmount.setText("$" + total);

        } catch (SQLException e) {
            showAlert("Error", "Failed to add treatments to the bill.");
            e.printStackTrace();
        }
    }

    public void payBill(Label totalBillAmount, ListView<String> billList) {
        try {
            // Pay Appointments
            for (int appointmentId : addedAppointments) {
                dbHandler.updatePaymentStatus("Appointment", "appointment_id", appointmentId);
            }

            // Pay Rooms
            for (int bookingId : addedRooms) {
                dbHandler.updatePaymentStatus("Booking", "booking_id", bookingId);
            }

            // Pay Treatments
            for (int treatmentId : addedTreatments) {
                dbHandler.updatePaymentStatus("Patient_Treatment", "treatment_id", treatmentId);
            }

            // Pay Prescriptions
            for (int itemId : addedPrescriptions) {
                dbHandler.updatePaymentStatus("Prescription", "item_id", itemId);
                dbHandler.updateStockQuantity(itemId, dbHandler.getUnpaidPrescriptions(patient.getPatientId()).get(0).getQuantity());
                dbHandler.insertGoodOut(itemId, dbHandler.getUnpaidPrescriptions(patient.getPatientId()).get(0).getQuantity(),
                        dbHandler.getUnpaidPrescriptions(patient.getPatientId()).get(0).getTotalPrice());
            }

            billItems.clear();
            totalBillAmount.setText("$0");
            showAlert("Success", "The bill has been paid successfully!");

        } catch (SQLException e) {
            showAlert("Error", "Failed to process the bill payment.");
            e.printStackTrace();
        }
    }

    private double calculateTotal() {
        return billItems.stream()
                .mapToDouble(item -> Double.parseDouble(item.split("- \\$")[1].trim()))
                .sum();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
