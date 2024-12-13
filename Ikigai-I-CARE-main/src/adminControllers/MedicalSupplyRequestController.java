package adminControllers;

import java.net.URL;
import java.util.ResourceBundle;

import adminModel.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MedicalSupplyRequestController implements Initializable{

	
	    @FXML
	    private ComboBox<String> StockSearchCriteriaComboBox;
	    
	    @FXML
	    private ComboBox<String>MedRequestSearchCriteriaComboBox;

	    @FXML
	    private Button confirmOrderButton;

	    @FXML
	    private Button doctorManagementButton;

	    @FXML
	    private Button homeButton;

	    @FXML
	    private Button inventoryButton;

	    @FXML
	    private Button logOutButton;

	    @FXML
	    private TextField medSupplyReqSearchTextField;


	    @FXML
	    private TextField StockDetailsSearchTextField;	   

	    @FXML
	    private Label mediSupplyReqLabel;

	    @FXML
	    private ImageView myImageView;

	    @FXML
	    private TextField requestQuantityTextField;

	    @FXML
	    private Button scheduleShiftButton;

	    @FXML
	    private Button scheduleWorkshopButton;

	   

	    @FXML
		private TableView<StockItem> stockDetailsTableView;

		@FXML
		private TableColumn<StockItem, Integer> stockItemID;

		@FXML
		private TableColumn<StockItem, String> stockItemName;

		@FXML
		private TableColumn<StockItem, String> stockItemDescription;

		@FXML
		private TableColumn<StockItem, String> stockItemType;

		@FXML
		private TableColumn<StockItem, Integer> stockItemQuantityAvailable;

		@FXML
		private TableColumn<StockItem, Integer> stockItemThreshold;
		
		@FXML
		private TableColumn<StockItem, Double> stockItemUnitPrice;

	    @FXML
	    private TableView<MedicalSupplyRequest> medSupplyReqTableView;
	   
	    @FXML
	    private TableColumn<MedicalSupplyRequest, String> supplyReqAdminName;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, Integer> supplyReqItemID;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, String> supplyReqItemName;
	   
	    @FXML
	    private TableColumn<MedicalSupplyRequest, String> supplyReqItemType;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, Integer> supplyReqQuantityRequested;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, Double> supplyReqUnitPrice;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, Double> supplyReqTotalPrice;
	    @FXML
	    private TableColumn<MedicalSupplyRequest, String> supplyReqDate;


	    @FXML
	    private Button supplyRequestButton;

	    @FXML
	    private Label supplyRequestLabel;

	    
	    private MedicalSupplyRequestHandler medicalSupplyRequestHandler;
	    
	    private StockItemHandler stockItemHandler;
	    
	    private GoodInHandler goodInHandler;

    @FXML
    void homePage(ActionEvent event) {

    }

    @FXML
    void medicalSupplyRequest(ActionEvent event) {

    }

    @FXML
    void scheduleWorkshop(ActionEvent event) {

    }

    @FXML
    void searchMedicalSupplyRequests(ActionEvent event) {

    }

    @FXML
    void searchStockDetails(ActionEvent event) {

    }

    @FXML
    void shiftSchedule(ActionEvent event) {

    }

    @FXML
    void viewDoctorManagement(ActionEvent event) {

    }

    @FXML
    void viewInventory(ActionEvent event) {

    }
    
    public void initializeButtons()
    {
        homeButton.setOnAction(e->onHomeButton());
   		
   		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
   		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
   		inventoryButton.setOnAction(e->onInventory());
   		supplyRequestButton.setOnAction(e->onSupplyRequestButton());
   		doctorManagementButton.setOnAction(e->onDoctorManagement());
        logOutButton.setOnAction(e->onLogOutButton());
        confirmOrderButton.setOnAction(e->handleMedicalSupplyRequest());
    }
    
    @Override
   	public void initialize(URL arg0, ResourceBundle arg1) {
   		
    	medicalSupplyRequestHandler = new MedicalSupplyRequestHandler();
    	stockItemHandler = new StockItemHandler();
    	goodInHandler = new GoodInHandler();
    	initializeButtons();
    	initializeSearchCriteria();
    	setupStockDetailsTable();
    	setupMedicalSupplyReqTable();
    	loadMedicalSupplyRequests();
    	loadStockItems();
    	setupMedSupplyReqSearch();
    	setupStockDetailsSearch();
    	
    	
    }
    
    
    
    
    private void setupStockDetailsTable() {
        stockItemID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        stockItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        stockItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        stockItemType.setCellValueFactory(new PropertyValueFactory<>("type"));
        stockItemQuantityAvailable.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        stockItemThreshold.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        stockItemUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }
    
    
    
    private void setupMedicalSupplyReqTable() {
      
        supplyReqAdminName.setCellValueFactory(new PropertyValueFactory<>("adminName"));
        supplyReqItemID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        supplyReqItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        supplyReqItemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        supplyReqQuantityRequested.setCellValueFactory(new PropertyValueFactory<>("quantityRequested"));
        supplyReqUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        supplyReqTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        supplyReqDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    }

    private void loadMedicalSupplyRequests() {
        medSupplyReqTableView.setItems(FXCollections.observableArrayList(medicalSupplyRequestHandler.getAllMedicalSupplyRequests()));
    }
    
    private void loadStockItems() {
        stockDetailsTableView.setItems(FXCollections.observableArrayList(stockItemHandler.getAllStockItems()));
    }


    private void setupMedSupplyReqSearch() {
        FilteredList<MedicalSupplyRequest> filteredData = new FilteredList<>(FXCollections.observableArrayList(medicalSupplyRequestHandler.getAllMedicalSupplyRequests()), p -> true);

        medSupplyReqSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(request -> {
                String selectedCriteria = MedRequestSearchCriteriaComboBox.getValue(); // Get selected criteria
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                switch (selectedCriteria) {
                    case "Item Name":
                        return request.getItemName().toLowerCase().contains(lowerCaseFilter);
                    case "Item Type":
                        return request.getItemType().toLowerCase().contains(lowerCaseFilter);
                    case "Request Date":
                        return request.getRequestDate().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        medSupplyReqTableView.setItems(filteredData);
    }

    private void setupStockDetailsSearch() {
        FilteredList<StockItem> filteredData = new FilteredList<>(FXCollections.observableArrayList(stockItemHandler.getAllStockItems()), p -> true);

        StockDetailsSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(stockItem -> {
                String selectedCriteria = StockSearchCriteriaComboBox.getValue(); // Get selected criteria
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                switch (selectedCriteria) {
                    case "Item Name":
                        return stockItem.getItemName().toLowerCase().contains(lowerCaseFilter);
                    case "Type":
                        return stockItem.getType().toLowerCase().contains(lowerCaseFilter);
                    case "Description":
                        return stockItem.getDescription().toLowerCase().contains(lowerCaseFilter);
                    case "Threshold":
                        return String.valueOf(stockItem.getThreshold()).contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        stockDetailsTableView.setItems(filteredData);
    }
   
    
    
    private void initializeSearchCriteria() {
        // Stock Details search criteria
        StockSearchCriteriaComboBox.setItems(FXCollections.observableArrayList(
            "Item Name", "Type", "Description", "Threshold"
        ));
        StockSearchCriteriaComboBox.setValue("Item Name"); // Default criteria

        MedRequestSearchCriteriaComboBox.setItems(FXCollections.observableArrayList(
            "Item Name","Item Type",  "Request Date"
        ));
       
    }

    @FXML
    private TextField stockItemIDTextfield;

   
    private void handleMedicalSupplyRequest() {
        try {
            int itemId = Integer.parseInt(stockItemIDTextfield.getText().trim());
            int requestedQuantity = Integer.parseInt(requestQuantityTextField.getText().trim());

            if (requestedQuantity <= 0 || requestedQuantity > 500) {
                showAlert("Invalid Quantity", "Requested quantity must be between 1 and 500.");
                return;
            }

            StockItem item = stockItemHandler.getStockItemById(itemId);
            if (item == null) {
                showAlert("Invalid Item ID", "No stock item found with the provided ID.");
                return;
            }

            if (item.getQuantityAvailable() >= 3000) {
                showAlert("Sufficient Stock", "Item ID: " + itemId + " (" + item.getItemName() +
                          ") has sufficient stock (" + item.getQuantityAvailable() + "). No need to reorder.");
                return;
            }

            int requestId = medicalSupplyRequestHandler.addMedicalSupplyRequestAndGetId(
                    AdminModel.getInstance().getLoggedInAdminId(), itemId, requestedQuantity);
            if (requestId == -1) {
                showAlert("Error", "Failed to create the medical supply request.");
                return;
            }

            boolean isStockUpdated = stockItemHandler.updateStockQuantity(itemId, requestedQuantity);
            if (!isStockUpdated) {
                showAlert("Error", "Failed to update the stock item.");
                return;
            }

            boolean isGoodInInserted = goodInHandler.insertGoodInEntry(requestId, itemId, requestedQuantity);
            if (isGoodInInserted) {
                showAlert("Success", "Medical supply request has been successfully added and inventory updated.");
                loadMedicalSupplyRequests();
                loadStockItems();
            } else {
                showAlert("Error", "Failed to update the GoodIn table.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for Item ID and Quantity.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }



    private void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
    
    
    private void onLogOutButton() {
		//closing the stage
		Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showLoginWindow();
	}
   	
   	private void onHomeButton() {
   	    Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
   	}

   	private void onSupplyRequestButton() {
   	    Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest(); // Open the dashboard
   	}

   	
   	private void onDoctorManagement() {
   		//closing the stage
   		Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showDoctorManagement();
   	}
   	
   	
   	
   	private void onScheduleShiftButton() {
   		//closing the stage
   		Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleShift();
   	}
   	
   	private void onScheduleWorkshopButton(){
   	
   		Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
   				
   	}
   	private void onInventory(){
   		
   		Stage stage = (Stage) mediSupplyReqLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showInventory();
   				
   	}
   	

}
