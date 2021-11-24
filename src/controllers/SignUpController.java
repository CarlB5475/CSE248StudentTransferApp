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
import utilities.SQLiteConnection;
import utilities.ViewChanger;

public class SignUpController implements Initializable {
	private SignUpModel signUpModel = new SignUpModel(SQLiteConnection.getMainUrl());
	
	@FXML private TextField firstNameInput, lastNameInput, userNameInput, zipInput, latInput, lonInput;
	@FXML private PasswordField passwordInput, reEnterPasswordInput;
	
	public void signUp(ActionEvent event) {
		String firstName = firstNameInput.getText(), lastName = lastNameInput.getText();
		String userName = userNameInput.getText().toLowerCase(), zip = zipInput.getText();
		String latitude = latInput.getText(), longitude = lonInput.getText();
		String password = passwordInput.getText(), reEnteredPassword = reEnterPasswordInput.getText();
		
		boolean hasEmptyInputs = firstName.equals("") || lastName.equals("") || userName.equals("") ||
				zip.equals("") || latitude.equals("") || longitude.equals("") || password.equals("") || reEnteredPassword.equals("");
		
		
		if(hasEmptyInputs || !isValidAccount(firstName, lastName, userName, zip, latitude, longitude, password, reEnteredPassword))
			return;
		
		Stream.of(firstNameInput, lastNameInput, userNameInput, zipInput, latInput, lonInput, passwordInput, reEnterPasswordInput)
		.forEach(textField -> textField.setText(""));
		
		signUpModel.addNewStudent(firstName, lastName, userName, zip, latitude, longitude, password);
		
		Alert signUpAlert = new Alert(AlertType.INFORMATION);
		signUpAlert.setTitle("Sign Up Alert!");
		signUpAlert.setHeaderText("You have successfully created your new account with the username \"" + userName + "\"!");
		signUpAlert.setContentText("Go back to the login menu to login!");
		signUpAlert.showAndWait();		
	}
	
	public void goToLogin(ActionEvent event) throws IOException {
		ViewChanger.changeToLoginView(event);
	}
	
	private boolean isValidAccount(String firstName, String lastName, String userName, String zip, 
			String latitude, String longitude, String password, String reEnteredPassword) {
		
		Alert nameAlert = new Alert(AlertType.ERROR);
		nameAlert.setTitle("Invalid Name Error!");
		nameAlert.setContentText("Create a valid name!");
		nameAlert.setHeaderText("Your name must have letters only!");
		if(!signUpModel.isProperName(firstName, lastName)) {
			nameAlert.showAndWait();
			return false;
		}
		
		Alert usernameAlert = new Alert(AlertType.ERROR);
		usernameAlert.setTitle("Invalid Username Error");
		usernameAlert.setContentText("Enter a valid username!");
		if(!signUpModel.isProperUsername(userName)) {
			usernameAlert.setHeaderText("This username contains a space!");
			usernameAlert.showAndWait();
			return false;
		}
		if(!signUpModel.isUniqueUsername(userName)) {
			usernameAlert.setHeaderText("The username \"" + userName + "\" already exists!");
			usernameAlert.showAndWait();
			return false;
		}
		
		Alert zipAlert = new Alert(AlertType.ERROR);
		zipAlert.setTitle("Invalid Zip Code Alert!");
		zipAlert.setHeaderText("Your zip code needs to contain numbers only and be 5 numbers long, or be in the format \"01234-5678\"!");
		zipAlert.setContentText("Enter a valid zip code!");
		if(!signUpModel.isProperZip(zip)) {
			zipAlert.showAndWait();
			return false;
		}
		
		Alert locationAlert = new Alert(AlertType.ERROR);
		locationAlert.setTitle("Invalid Location Error!");
		locationAlert.setContentText("Enter a valid latitude and longitude!");
		String[] locationStrings = {latitude, longitude};
		if(signUpModel.areDoubleValues(locationStrings)) {
			locationAlert.setHeaderText("Your latitude and longitude need to be numberical values!");
			locationAlert.showAndWait();
			return false;
		}
		if(signUpModel.isProperLocation(latitude, longitude)) {
			locationAlert.setHeaderText("The latitude needs to be between -90 and 90 degrees and the longitude needs to be between -180 and 180 degrees!");
			locationAlert.showAndWait();
			return false;
		}
		
		Alert invalidPasswordAlert = new Alert(AlertType.ERROR);
		invalidPasswordAlert.setTitle("Invalid Password Error");
		invalidPasswordAlert.setHeaderText("This password is invalid!");
		String invalidPasswordMessage = "A password should contain at least 8 characters, "
				+ "contain at least one capital letter, "
				+ "one lowercase letter, and one digit. "
				+ "It should not contain any spaces! "
				+ "Enter a valid password!";
		invalidPasswordAlert.setContentText(invalidPasswordMessage);
		if(!signUpModel.isProperPassword(password)) {
			invalidPasswordAlert.showAndWait();
			return false;
		}
		
		Alert passwordMismatchAlert = new Alert(AlertType.ERROR);
		passwordMismatchAlert.setTitle("Password Mismatch Error");
		passwordMismatchAlert.setHeaderText("The password and re-entered password are not the same!");
		passwordMismatchAlert.setContentText("Enter a valid password that you can remember!");
		if(signUpModel.hasMatchingPasswords(password, reEnteredPassword)) {
			passwordMismatchAlert.showAndWait();
			return false;
		}
		
		return true;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(!signUpModel.isConnectedDb()) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
}
