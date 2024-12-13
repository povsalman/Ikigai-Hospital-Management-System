package adminControllers;

import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InventoryController implements Initializable {

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
	private TableView<GoodIn> goodInTableView;

	@FXML
	private TableColumn<GoodIn, Integer> GoodInItemIDCol;

	@FXML
	private TableColumn<GoodIn, String> GoodInItemNameCol;

	@FXML
	private TableColumn<GoodIn, String> GoodInItemTypeCol;

	@FXML
	private TableColumn<GoodIn, Integer> GoodInQuantityPurchasedCol;

	@FXML
	private TableColumn<GoodIn, Double> GoodInPurchasePriceCol;

	@FXML
	private TableColumn<GoodIn, String> GoodInRequestDateCol;

	@FXML
	private TableColumn<GoodIn, String> GoodInDateCol;

	@FXML
	private TableView<GoodOut> goodOutTableView;

	@FXML
	private TableColumn<GoodOut, Integer> GoodOutItemIDCol;

	@FXML
	private TableColumn<GoodOut, String> GoodOutItemNameCol;

	@FXML
	private TableColumn<GoodOut, String> GoodOutItemTypeCol;

	@FXML
	private TableColumn<GoodOut, Integer> GoodOutQuantityRemovedCol;

	@FXML
	private TableColumn<GoodOut, Double> GoodOutPurchasePrice;

	@FXML
	private TableColumn<GoodOut, Double> GoodOutSalePrice;

	@FXML
	private TableColumn<GoodOut, String> GoodOutDate;

	

    @FXML
    private ComboBox<String> GoodInSearchCriteriaComboBox;

    @FXML
    private TextField GoodInSearchTextField;
    
    @FXML
    private TextField StockDetailsSearchTextField;
    @FXML
    private ComboBox<String> StockSearchCriteriaComboBox; 

   @FXML
    private ComboBox<String> GoodOutSearchCriteriaComboBox1;

    @FXML
    private TextField GoodOutSearchTextField;

    @FXML
    private Button doctorManagementButton;

    

    @FXML
    private Button homeButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button logOutButton;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button scheduleShiftButton;

    @FXML
    private Button scheduleWorkshopButton;

    @FXML
    private Label stockDetailsLabel;

    
    @FXML
    private Button supplyRequestButton;

    @FXML
    private AnchorPane trackInventoryAnchorPane;

    @FXML
    private Button trackInventoryButton;

    @FXML
    private AnchorPane viewInventoryAnchorPane;

    @FXML
    private Label viewInventoryLabel;
    
    private StockItemHandler stockItemHandler;

    private GoodInHandler goodInHandler;
    
    private GoodOutHandler goodOutHandler;

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
    void shiftSchedule(ActionEvent event) {

    }

    @FXML
    void trackInventory(ActionEvent event) {
    	
    if (event.getSource() == trackInventoryButton)
   	 {
   		 trackInventoryAnchorPane.setVisible(true);
   		 viewInventoryAnchorPane.setVisible(false);
   	 }

    }

    @FXML
    void viewDoctorManagement(ActionEvent event) {

    }

    @FXML
    void viewInventory(ActionEvent event) {
    	
    	if (event.getSource() == inventoryButton)
   	 {
   		trackInventoryAnchorPane.setVisible(false);
   		viewInventoryAnchorPane.setVisible(true);
   	 }

    }
    
    public void initializeButtons()
    {
        homeButton.setOnAction(e->onHomeButton());
   		scheduleShiftButton.setOnAction(e -> onScheduleShiftButton());
   		scheduleWorkshopButton.setOnAction(e->onScheduleWorkshopButton());
   		supplyRequestButton.setOnAction(e->onSupplyRequestButton());
   		doctorManagementButton.setOnAction(e->onDoctorManagement());
   		logOutButton.setOnAction(e->onLogOutButton());
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stockItemHandler = new StockItemHandler();
        goodInHandler = new GoodInHandler();
        goodOutHandler = new GoodOutHandler();

        initializeButtons();
        initializeSearchCriteria();
        setupStockDetailsTable();
        setupGoodInTable();
        setupGoodOutTable();

        loadStockItems();
        loadGoodInItems();
        loadGoodOutItems();

        setupStockDetailsSearch();
        setupGoodInSearch();
        setupGoodOutSearch();
        checkLowStockItems(); 
    }

    
    private void initializeSearchCriteria() {
        // Stock Details search criteria
        StockSearchCriteriaComboBox.setItems(FXCollections.observableArrayList(
            "Item Name", "Type", "Description", "Threshold"
        ));
        StockSearchCriteriaComboBox.setValue("Item Name"); // Default criteria

        // GoodIn search criteria
        GoodInSearchCriteriaComboBox.setItems(FXCollections.observableArrayList(
            "Item Name", "Type", "Quantity Purchased", "Request Date", "Received Date"
        ));
        GoodInSearchCriteriaComboBox.setValue("Item Name"); // Default criteria

        // GoodOut search criteria
        GoodOutSearchCriteriaComboBox1.setItems(FXCollections.observableArrayList(
            "Item Name", "Type", "Quantity Removed", "Sale Price", "Issued Date"
        ));
        GoodOutSearchCriteriaComboBox1.setValue("Item Name"); // Default criteria
    }

    private void setupGoodInSearch() {
        FilteredList<GoodIn> filteredData = new FilteredList<>(FXCollections.observableArrayList(goodInHandler.getAllGoodInEntries()), p -> true);

        GoodInSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCriteria = GoodInSearchCriteriaComboBox.getValue();
            filteredData.setPredicate(goodIn -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (selectedCriteria) {
                    case "Item Name":
                        return goodIn.getItemName().toLowerCase().contains(lowerCaseFilter);
                    case "Type":
                        return goodIn.getType().toLowerCase().contains(lowerCaseFilter);
                    case "Quantity Purchased":
                        return String.valueOf(goodIn.getQuantityPurchased()).contains(lowerCaseFilter);
                    case "Request Date":
                        return goodIn.getRequestDate().toLowerCase().contains(lowerCaseFilter);
                    case "Received Date":
                        return goodIn.getReceivedDate().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        goodInTableView.setItems(filteredData);
    }

   
    private void setupGoodOutSearch() {
        FilteredList<GoodOut> filteredData = new FilteredList<>(FXCollections.observableArrayList(goodOutHandler.getAllGoodOutEntries()), p -> true);

        GoodOutSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCriteria = GoodOutSearchCriteriaComboBox1.getValue();
            filteredData.setPredicate(goodOut -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (selectedCriteria) {
                    case "Item Name":
                        return goodOut.getItemName().toLowerCase().contains(lowerCaseFilter);
                    case "Type":
                        return goodOut.getType().toLowerCase().contains(lowerCaseFilter);
                    case "Quantity Removed":
                        return String.valueOf(goodOut.getQuantityRemoved()).contains(lowerCaseFilter);
                    case "Sale Price":
                        return String.valueOf(goodOut.getSalePrice()).contains(lowerCaseFilter);
                    case "Issued Date":
                        return goodOut.getIssuedDate().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        goodOutTableView.setItems(filteredData);
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
    
    private void setupGoodInTable() {
        GoodInItemIDCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        GoodInItemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        GoodInItemTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        GoodInQuantityPurchasedCol.setCellValueFactory(new PropertyValueFactory<>("quantityPurchased"));
        GoodInPurchasePriceCol.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        GoodInRequestDateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        GoodInDateCol.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
    }

    
    private void setupGoodOutTable() {
        GoodOutItemIDCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        GoodOutItemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        GoodOutItemTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        GoodOutQuantityRemovedCol.setCellValueFactory(new PropertyValueFactory<>("quantityRemoved"));
        GoodOutPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        GoodOutSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        GoodOutDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
    }

    
    private void loadStockItems() {
        stockDetailsTableView.setItems(FXCollections.observableArrayList(stockItemHandler.getAllStockItems()));
    }

    private void loadGoodInItems() {
        goodInTableView.setItems(FXCollections.observableArrayList(goodInHandler.getAllGoodInEntries()));
    }
    private void loadGoodOutItems() {
        goodOutTableView.setItems(FXCollections.observableArrayList(goodOutHandler.getAllGoodOutEntries()));
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
   
    
    private void checkLowStockItems() {
        List<StockItem> lowStockItems = stockItemHandler.getLowStockItems();

        if (!lowStockItems.isEmpty()) {
            StringBuilder alertMessage = new StringBuilder("The following items are running low:\n");

            for (StockItem item : lowStockItems) {
                alertMessage.append("ID: ").append(item.getItemId())
                             .append(", Name: ").append(item.getItemName())
                             .append(", Quantity: ").append(item.getQuantityAvailable())
                             .append(", Threshold: ").append(item.getThreshold())
                             .append("\n");
            }

            // Show alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Low Stock Alert");
            alert.setHeaderText("Some stock items are running low!");
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
        }
    }

    
    private void onLogOutButton() {
		//closing the stage
		Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
		AdminModel.getInstance().getViewFactory().closeStage(stage);
		AdminModel.getInstance().getViewFactory().showLoginWindow();
	}
   	
   	private void onHomeButton() {
   	    Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showDashboardWindow(); // Open the dashboard
   	}

   	private void onSupplyRequestButton() {
   	    Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
   	    stage.close(); // Close the current stage
   	    AdminModel.getInstance().getViewFactory().showMedicalSupplyRequest(); // Open the dashboard
   	}

   	
   	private void onDoctorManagement() {
   		//closing the stage
   		Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showDoctorManagement();
   	}
   	
   	
   	
   	private void onScheduleShiftButton() {
   		//closing the stage
   		Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleShift();
   	}
   	
   	private void onScheduleWorkshopButton(){
   	
   		Stage stage = (Stage) viewInventoryLabel.getScene().getWindow();
   		AdminModel.getInstance().getViewFactory().closeStage(stage);
   		AdminModel.getInstance().getViewFactory().showScheduleWorkshop();
   				
   	}
   

}
