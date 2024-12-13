package adminModel;

public class StockItem {
    private int itemId;
    private String itemName;
    private String description;
    private String type;
    private int quantityAvailable;
    private int threshold;
    private double unitPrice;

    // Constructor
    public StockItem(int itemId, String itemName, String description, String type, int quantityAvailable, int threshold, double unitPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.type = type;
        this.quantityAvailable = quantityAvailable;
        this.threshold = threshold;
        this.unitPrice = unitPrice;
    }
    
    public StockItem(int itemId, String itemName, int quantityAvailable, int threshold) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantityAvailable = quantityAvailable;
        this.threshold = threshold;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
