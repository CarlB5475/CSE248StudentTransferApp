package models;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SignUpModel extends ConnectionModel {
	
	public SignUpModel(String url) {
		super(url);
	}
	
	public SignUpModel() {
		super();
	}
	
	public void addNewStudent(String firstName, String lastName, String username, String zip, String latitude, String longitude, String password) {
		username = username.toLowerCase();
		int favoritesId = getFavoritesId();
		
		final double PRECISION_NUMBER = 1000000; // The number of zeros determines the number of decimal places for rounding
		double latDouble = Double.parseDouble(latitude), lonDouble = Double.parseDouble(longitude);
		latDouble = Math.round(latDouble * PRECISION_NUMBER) / PRECISION_NUMBER; // This rounds the latitude and longitude by 6 decimal places
		lonDouble = Math.round(lonDouble * PRECISION_NUMBER) / PRECISION_NUMBER;
		
		PreparedStatement preparedStatement = null;
		String statementString = "INSERT INTO students (firstName, lastName, userName, password, zip, latitude, longitude, favoritesId)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		// 1 = firstName, 2 = lastName, 3 = userName, 4 = password, 5 = zip, 6 = latitude, 7 = longitude, 8 = favoritesId
		
		try {
			resetConnection();
			preparedStatement = getConnection().prepareStatement(statementString);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, username);
			preparedStatement.setString(4, password);
			preparedStatement.setString(5, zip);
			preparedStatement.setDouble(6, latDouble);
			preparedStatement.setDouble(7, lonDouble);
			preparedStatement.setInt(8, favoritesId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				preparedStatement.close();
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private int getFavoritesId() {
		resetConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		int favoritesId = 0;
		
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("INSERT INTO favorites DEFAULT VALUES"); // makes an empty favorites row with the favoritesId
			resultSet = statement.executeQuery("SELECT * FROM favorites ORDER BY favoritesId DESC");
			resultSet.next();
			favoritesId = resultSet.getInt("favoritesId");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				statement.close();
				resultSet.close();
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return favoritesId;
	}
	
	public boolean isValidAccount(String firstName, String lastName, String username, String zip, String latitude, String longitude, String password, String reEnteredPassword) {
		return isValidName(firstName, lastName) && isValidUsername(username) && 
				isValidZip(zip) && isValidLocation(latitude, longitude) && isValidPassword(password, reEnteredPassword);
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
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				preparedStatement.close();
				resultSet.close();
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return true;
	}
	
	private boolean isValidZip(String zip) {
		Alert zipAlert = new Alert(AlertType.ERROR);
		zipAlert.setTitle("Invalid Zip Code Alert!");
		zipAlert.setHeaderText("Your zip code needs to contain numbers only and be 5 numbers long, or be in the format \"01234-5678\"!");
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
		} else if(zip.length() != 5 || !zip.matches("[0-9]+")) {
			zipAlert.showAndWait();
			return false;
		}
		
		return true;
	}
	
	private boolean isValidLocation(String latitude, String longitude) {
		Alert locationAlert = new Alert(AlertType.ERROR);
		locationAlert.setTitle("Invalid Location Error!");
		locationAlert.setContentText("Enter a valid latitude and longitude!");
		
		try {
			double latDouble = Double.parseDouble(latitude); // Tests if these string values would throw a NumberFormatException
			double lonDouble = Double.parseDouble(longitude);
			
			// The range for latitude and longitude has to be from -90 to 90 degrees and -180 to 180 degrees respectively
			boolean latInRange = latDouble >= -90 && latDouble <= 90;
			boolean lonInRange = lonDouble >= -180 && lonDouble <= 180;
			if(!latInRange || !lonInRange) {
				locationAlert.setHeaderText("The latitude needs to be between -90 and 90 degrees and the longitude needs to be between -180 and 180 degrees!");
				locationAlert.showAndWait();
				return false;
			}
		} catch (NumberFormatException e) {
			locationAlert.setHeaderText("Your latitude and longitude need to be numberical values!");
			locationAlert.showAndWait();
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
