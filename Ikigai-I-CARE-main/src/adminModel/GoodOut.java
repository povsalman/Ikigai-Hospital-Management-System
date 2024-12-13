package adminModel;

public class GoodOut {
    private int itemId;
    private String itemName;
    private String type;
    private int quantityRemoved;
    private double purchasePrice;
    private double salePrice;
    private String issuedDate;

    // Constructor
    public GoodOut(int itemId, String itemName, String type, int quantityRemoved, double purchasePrice, double salePrice, String issuedDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.type = type;
        this.quantityRemoved = quantityRemoved;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.issuedDate = issuedDate;
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

    public int getQuantityRemoved() {
        return quantityRemoved;
    }

    public void setQuantityRemoved(int quantityRemoved) {
        this.quantityRemoved = quantityRemoved;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }
}
