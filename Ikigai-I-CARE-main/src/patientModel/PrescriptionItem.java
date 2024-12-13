package patientModel;

public class PrescriptionItem {
    private int id;
    private String name;
    private int quantity;
    private String type;
    private double unitPrice;
    private double totalPrice;
    private int quantity_avaialble;

    public PrescriptionItem(int id, String name, int quantity, String type, double unitPrice, double totalPrice,int quantity_avail) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.quantity_avaialble = quantity_avail;
    }

    public int getId() {
        return id;
    }
    
    public int getavailableQuantity() {
    	return quantity_avaialble;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
