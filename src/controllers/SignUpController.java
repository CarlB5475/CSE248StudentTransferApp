package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.*;
import utilities.ViewChanger;

public class SignUpController implements Initializable {
	private SignUpModel signUpModel = new SignUpModel();
	
	@FXML private TextField firstNameInput, lastNameInput, usernameInput, zipInput, latInput, lonInput;
	@FXML private PasswordField passwordInput, reEnterPasswordInput;
	
	public void signUp(ActionEvent event) {
		String firstName = firstNameInput.getText(), lastName = lastNameInput.getText();
		String username = usernameInput.getText().toLowerCase(), zip = zipInput.getText();
		String latitude = latInput.getText(), longitude = lonInput.getText();
		String password = passwordInput.getText(), reEnteredPassword = reEnterPasswordInput.getText();
		
		boolean hasEmptyInputs = firstName.equals("") || lastName.equals("") || username.equals("") ||
				zip.equals("") || latitude.equals("") || longitude.equals("") || password.equals("") || reEnteredPassword.equals("");
		
		
		if(hasEmptyInputs || !signUpModel.isValidAccount(firstName, lastName, username, zip, latitude, longitude, password, reEnteredPassword))
			return;
		
		Stream.of(firstNameInput, lastNameInput, usernameInput, zipInput, latInput, lonInput, passwordInput, reEnterPasswordInput)
		.forEach(textField -> textField.setText(""));
		
		signUpModel.addNewStudent(firstName, lastName, username, zip, latitude, longitude, password);
		
		Alert signUpAlert = new Alert(AlertType.INFORMATION);
		signUpAlert.setTitle("Sign Up Alert!");
		signUpAlert.setHeaderText("You have successfully created your new account with the username \"" + username + "\"!");
		signUpAlert.setContentText("Go back to the login menu to login!");
		signUpAlert.showAndWait();		
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
