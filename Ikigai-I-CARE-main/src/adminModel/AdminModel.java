package adminModel;
import javax.management.modelmbean.ModelMBean;

import adminViews.*;

public class AdminModel {

	//Applying singleton design pattern
	
	//Static variable to hold the single instance of AdminModel
	private static AdminModel adminModel;
	private final ViewFactory viewFactory;
	private int loggedInAdminId;  // Store the admin_id of the logged-in admin

	
	private AdminModel()
	{// Initialize the ViewFactory instance (composition relationship)
		this.viewFactory = new ViewFactory();
		
	} 
	
	//The synchronized keyword ensures thread safety, preventing multiple threads from creating
	//multiple instances at the same time.
	//Without synchronized, two threads might pass the if (adminModel == null) check simultaneously,
	//leading to two separate instances being created.
	public static synchronized AdminModel getInstance()
	{
		// Check if the single instance is already created
		if(adminModel == null)
		{
			adminModel = new AdminModel();
		}
		
		return adminModel;
	}
	
	public ViewFactory getViewFactory() {
		return viewFactory;
	}
	
	 // Set the logged-in admin ID (after the admin logs in)
    public void setLoggedInAdminId(int adminId) {
        this.loggedInAdminId = adminId;
    }

    // Get the logged-in admin ID
    public int getLoggedInAdminId() {
        return this.loggedInAdminId;
    }
}
