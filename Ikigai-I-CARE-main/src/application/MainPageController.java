package application;

import java.io.IOException;

import adminModel.AdminModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainPageController {

    @FXML
    private Button adminButton;

    @FXML
    private Button doctorButton;

    @FXML
    private Button patientButton;

    @FXML
    private Label selectLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    void userLogin(ActionEvent event) {
    	if(event.getSource() == adminButton) {
    		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
    		AdminModel.getInstance().getViewFactory().closeStage(stage);
    		AdminModel.getInstance().getViewFactory().showLoginWindow();
    	}
    	else if(event.getSource() == patientButton) {
    		try {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientApplication/PatientLogin.fxml"));
    			Parent root = loader.load();
    	        Stage stage = new Stage();
    	        stage.setScene(new Scene(root));
    	        stage.show();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    		
    		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
    		stage.close();
    	}
    	else if(event.getSource() == doctorButton) {
    		try {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Doctorapplication/DoctorLogin.fxml"));
    			Parent root = loader.load();
    	        Stage stage = new Stage();
    	        stage.setScene(new Scene(root));
    	        stage.show();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    		
    		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
    		stage.close();
    		
    	}
    }
    
    

}
