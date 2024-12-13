package adminViews;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewFactory {
   
	public void createStage(FXMLLoader loader) {
        Scene scene = null;
		
		try {
			scene = new Scene(loader.load());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Admin");
		stage.show();
	}

	public void showLoginWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/AdminLogin.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showSignUpWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/AdminSignUp.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showDashboardWindow() {
	   try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/AdminHomePage.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showDoctorManagement() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/DoctorManagement.fxml"));
	        Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void showScheduleShift() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/ScheduleShift.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showScheduleWorkshop() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/ScheduleWorkshop.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
    public void showInventory() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/ViewInventory.fxml"));
			Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
    public void showMedicalSupplyRequest() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/MedicalSupplyRequest.fxml"));
    		Parent root = loader.load();
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
    
    public void showManageDoctorRequest() {
    	try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminViews/ManageDoctorRequest.fxml"));
        	Parent root = loader.load();
    	    Stage stage = new Stage();
    	    stage.setScene(new Scene(root));
    	    stage.show();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }

	
	
	
	public void closeStage(Stage stage) {
		stage.close();
	}
}
