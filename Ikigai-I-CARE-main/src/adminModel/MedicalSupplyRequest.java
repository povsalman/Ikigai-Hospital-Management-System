package adminModel;

public class MedicalSupplyRequest {
    private int adminId;
    private String adminName;
    private int itemId;
    private String itemName;
    private String itemDescription;
    private String itemType;
    private int quantityRequested;
    private double unitPrice;
    private double totalPrice;
    private String requestDate;

    public MedicalSupplyRequest( String adminName, int itemId, String itemName, 
                                String itemType, int quantityRequested, double unitPrice, double totalPrice,
                                String requestDate) {
      
        this.adminName = adminName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantityRequested = quantityRequested;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.requestDate = requestDate;
    }

    // Getters and Setters for all fields
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public int getQuantityRequested() { return quantityRequested; }
    public void setQuantityRequested(int quantityRequested) { this.quantityRequested = quantityRequested; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
}
