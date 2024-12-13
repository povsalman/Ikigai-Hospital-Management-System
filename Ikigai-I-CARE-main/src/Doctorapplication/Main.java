//package Doctor.application;
//	
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.stage.Stage;
//import javafx.scene.Group;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//
//
//public class Main extends Application {
//	
//	public static void main(String[] args) {
//		launch(args);
//	}
//
//	
//	@Override
//	public void start(Stage stage) {
//		try {
//			
//			Parent root = FXMLLoader.load(getClass().getResource("DoctorMainForm.fxml"));
//			
//			Scene scene = new Scene(root);
//			
//			//cool way to keep adding the css
//			//String css = this.getClass().getResource("application.css").toExternalForm();
//			//scene.getStylesheets().add(css);
//			
//			stage.setScene(scene);
//			
//			stage.show();
//		
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
