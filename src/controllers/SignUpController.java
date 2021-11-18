package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import models.*;
import utilities.ViewChanger;

public class SignUpController implements Initializable {
	private SignUpModel signUpModel = new SignUpModel();
	
	@FXML private TextField firstNameInput, lastNameInput, usernameInput, zipInput;
	@FXML private PasswordField passwordInput, reEnterPasswordInput;
	
	public void signUp(ActionEvent event) {
		
	}
	
	public void goToLogin(ActionEvent event) throws IOException {
		ViewChanger.changeToLoginView(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(!signUpModel.isConnectedDb()) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
}
