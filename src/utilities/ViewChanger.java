package utilities;

import java.io.IOException;

import controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import models.ViewableStudent;

public class ViewChanger {
	
	public static void changeToLoginView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/login-view.fxml"));
		Scene loginScene = new Scene(loader.load());
		setToMainStyle(loginScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}
	
//	public static void changeToLoginView(Menu menu) throws IOException {
//		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/login-view.fxml"));
//		Scene loginScene = new Scene(loader.load());
//		setToMainStyle(loginScene);
//		
//		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//		window.setScene(loginScene);
//		window.show();
//	}
	
	public static void changeToSignUpView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/signUp-view.fxml"));
		Scene signUpScene = new Scene(loader.load());
		setToMainStyle(signUpScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(signUpScene);
		window.show();
	}
	
	public static void changeToCollegeSearchView(ActionEvent event, ViewableStudent loggedStudent) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/collegeSearch-view.fxml"));
		Scene collegeSearchScene = new Scene(loader.load());
		setToMainStyle(collegeSearchScene);
		
		CollegeSearchController controller = loader.getController();
		controller.setStudent(loggedStudent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(collegeSearchScene);
		window.show();
	}
	
	private static void setToMainStyle(Scene scene) {
		scene.getStylesheets().add(ViewChanger.class.getResource("/views/main-style.css").toExternalForm());
	}
}
