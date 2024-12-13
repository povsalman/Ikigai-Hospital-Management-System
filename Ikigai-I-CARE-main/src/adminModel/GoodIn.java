package adminModel;

public class GoodIn {
    private int itemId;
    private String itemName;
    private String type;
    private int quantityPurchased;
    private double purchasePrice;
    private String requestDate;
    private String receivedDate;

    // Constructor
    public GoodIn(int itemId, String itemName, String type, int quantityPurchased, double purchasePrice, String requestDate, String receivedDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.type = type;
        this.quantityPurchased = quantityPurchased;
        this.purchasePrice = purchasePrice;
        this.requestDate = requestDate;
        this.receivedDate = receivedDate;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }
}
