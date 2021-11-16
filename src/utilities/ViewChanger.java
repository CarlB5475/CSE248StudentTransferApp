package utilities;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewChanger {
	
	public static void changeToLoginView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/login-view.fxml"));
		Scene loginScene = new Scene(loader.load());
		setToMainStyle(loginScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}
	
	public static void changeToSignUpView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/signUp-view.fxml"));
		Scene signUpScene = new Scene(loader.load());
		setToMainStyle(signUpScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(signUpScene);
		window.show();
	}
	
	public static void changeToCollegeSearchView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/collegeSearch-view.fxml"));
		Scene collegeSearchScene = new Scene(loader.load());
		setToMainStyle(collegeSearchScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(collegeSearchScene);
		window.show();
	}
	
	private static void setToMainStyle(Scene scene) {
		scene.getStylesheets().add(ViewChanger.class.getResource("/views/main-style.css").toExternalForm());
	}
}
