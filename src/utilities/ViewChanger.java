package utilities;

import java.io.IOException;

import controllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

public class ViewChanger {
	
	public static void changeToLoginView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/login-view.fxml"));
		Scene loginScene = new Scene(loader.load());
		setToMainStyle(loginScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.show();
	}
	
	public static void changeToLoginView(MenuBar menuBar) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/login-view.fxml"));
		Scene loginScene = new Scene(loader.load());
		setToMainStyle(loginScene);
		
		Stage window = (Stage)menuBar.getScene().getWindow();
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
	
	public static void viewCollegeProfile(ViewableCollege selectedCollege) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/collegeProfile-view.fxml"));
		Scene viewCollegeScene = new Scene(loader.load());
		setToMainStyle(viewCollegeScene);
		
		CollegeProfileController controller = loader.getController();
		controller.setCollege(selectedCollege);
		
		Stage popupWindow = new Stage();
		popupWindow.setScene(viewCollegeScene);
		popupWindow.setTitle(selectedCollege.getName() + " Information");
		popupWindow.initModality(Modality.APPLICATION_MODAL);
		popupWindow.showAndWait();
	}
	
	public static void changeToStudentProfileView(MenuBar menuBar, ViewableStudent loggedStudent) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewChanger.class.getResource("/views/studentProfile-view.fxml"));
		Scene studentProfileScene = new Scene(loader.load());
		setToMainStyle(studentProfileScene);
		
		StudentProfileController controller = loader.getController();
		controller.setStudent(loggedStudent);
		
		Stage window = (Stage)menuBar.getScene().getWindow();
		window.setScene(studentProfileScene);
		window.show();
	}
	
	private static void setToMainStyle(Scene scene) {
		scene.getStylesheets().add(ViewChanger.class.getResource("/views/main-style.css").toExternalForm());
	}
}
