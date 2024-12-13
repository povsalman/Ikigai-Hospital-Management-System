package patientModel;

public class Doctor {
    private int doctorId;
    private String docName;
    private String email;
    private String gender;
    private String phone;
    private String specialization;
    private String image;

    public Doctor(int doctorId, String docName, String email, String gender, String phone, String specialization,String image) {
        this.doctorId = doctorId;
        this.docName = docName;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.specialization = specialization;
        this.image = image;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDocName() {
        return docName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialization() {
        return specialization;
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}

