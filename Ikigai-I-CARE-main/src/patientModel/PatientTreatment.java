package patientModel;

import java.util.Date;

public class PatientTreatment {
	private int id;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;
    private Double price;


    // Constructor
    public PatientTreatment(int id,String description, Date startDate, Date endDate, String status,Double price) {
        this.setId(id);
    	this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.setPrice(price);
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
