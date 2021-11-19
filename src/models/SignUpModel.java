package models;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SignUpModel extends ConnectionModel {
	
	public SignUpModel() {
		super();
	}
	
	public void addNewStudent(String firstName, String lastName, String username, String zip, String password, String reEnteredPassword) {
		
	}
	
	public boolean isValidAccount(String firstName, String lastName, String username, String zip, String password, String reEnteredPassword) {
		return isValidName(firstName, lastName) && isValidUsername(username) && isValidZip(zip) && isValidPassword(password, reEnteredPassword);
	}
	
	private boolean isValidName(String firstName, String lastName) {
		Alert nameAlert = new Alert(AlertType.ERROR);
		nameAlert.setTitle("Invalid Name Error!");
		nameAlert.setContentText("Create a valid name!");
		
		if(!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
			nameAlert.setHeaderText("Your name must have letters only!");
			nameAlert.showAndWait();
			return false;
		}
		
		return true;
	}
	
	private boolean isValidUsername(String username) {
		Alert usernameAlert = new Alert(AlertType.ERROR);
		usernameAlert.setTitle("Invalid Username Error");
		usernameAlert.setContentText("Enter a valid username!");
		username = username.toLowerCase();
		
		if(username.contains(" ")) {
			usernameAlert.setHeaderText("This username contains a space!");
			usernameAlert.showAndWait();
			return false;
		}
		
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM students WHERE userName=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				usernameAlert.setHeaderText("The username \"" + username + "\" already exists!");
				usernameAlert.showAndWait();
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}
	
	private boolean isValidZip(String zip) {
		Alert zipAlert = new Alert(AlertType.ERROR);
		zipAlert.setTitle("Invalid Zip Code Alert!");
		zipAlert.setHeaderText("Your zip code needs to contain numbers only or be in the format \"01234-5678\"!");
		zipAlert.setContentText("Enter a valid zip code!");
		
		if(zip.contains("-")) {
			String[] zipParts = zip.split("-");
			if(zipParts.length != 2 || zipParts[0].length() != 5 || zipParts[1].length() != 4) {
				zipAlert.showAndWait();
				return false;
			}
			
			for(String part : zipParts) {
				if(!part.matches("[0-9]+")) {
					zipAlert.showAndWait();
					return false;
				}
			}
		} else if(!zip.matches("[0-9]+")) {
			zipAlert.showAndWait();
			return false;
		}
		
		return true;
	}
	
	private boolean isValidPassword(String password, String reEnteredPassword) {
		try {
			if(password.length() < 8)
				throw new Exception();
			if(password.contains(" "))
				throw new Exception();
			if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) // this checks if a password has a lowercase letter, a capital letter, and a digit
				throw new Exception();
			
		} catch (Exception e) {
			String contentMessage = "A password should contain at least 8 characters, "
					+ "contain at least one capital letter, "
					+ "one lowercase letter, and one digit. "
					+ "It should not contain any spaces! "
					+ "Enter a valid password!";
			
			Alert invalidPasswordAlert = new Alert(AlertType.ERROR);
			invalidPasswordAlert.setTitle("Invalid Password Error");
			invalidPasswordAlert.setHeaderText("This password is invalid!");
			invalidPasswordAlert.setContentText(contentMessage);
			invalidPasswordAlert.showAndWait();
			return false;
		}
		
		if(!password.equals(reEnteredPassword)) {
			Alert passwordMismatchAlert = new Alert(AlertType.ERROR);
			passwordMismatchAlert.setTitle("Password Mismatch Error");
			passwordMismatchAlert.setHeaderText("The password and re-entered password are not the same!");
			passwordMismatchAlert.setContentText("Enter a valid password that you can remember!");
			passwordMismatchAlert.showAndWait();
			return false;
		}
		return true;
	}
}
