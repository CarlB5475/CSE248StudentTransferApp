package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.*;
import utilities.SQLiteConnection;
import utilities.ViewChanger;

public class LoginController implements Initializable {
	private LoginModel loginModel = new LoginModel(SQLiteConnection.getMainUrl());
	
	@FXML private TextField userInput;
	@FXML private PasswordField passwordInput;
	
	public void login(ActionEvent event) throws IOException {
		String username = userInput.getText(), password = passwordInput.getText();
		userInput.setText("");
		passwordInput.setText("");
		
		if(loginModel.isLogin(username, password))
			ViewChanger.changeToCollegeSearchView(event);
		else {
			Alert loginAlert = new Alert(AlertType.ERROR);
			loginAlert.setTitle("Invalid Login Error");
			loginAlert.setHeaderText("This attempted login was not successful!");
			loginAlert.setContentText("Please reenter the correct login information!");
			loginAlert.showAndWait();
		}
	}
	
	public void signUp(ActionEvent event) throws IOException {
		ViewChanger.changeToSignUpView(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(!loginModel.isConnectedDb()) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
}
