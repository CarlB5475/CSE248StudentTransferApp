package models;

import java.sql.*;

public class SignUpModel extends ConnectionModel {
	
	public SignUpModel(String url) {
		super(url);
	}
	
	public void addNewStudent(String firstName, String lastName, String userName, String zip, String latitude, String longitude, String password) {
		userName = userName.toLowerCase();
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
			preparedStatement.setString(3, userName);
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

	public boolean isProperName(String firstName, String lastName) {
		return firstName.matches("[a-zA-Z]+") && lastName.matches("[a-zA-Z]+");
	}
	
	public boolean isProperUsername(String userName) {
		userName = userName.toLowerCase();
		return !userName.contains(" ");
	}
	
	public boolean isUniqueUsername(String userName) {
		userName = userName.toLowerCase();
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM students WHERE userName=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();
			return !resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
			return false;
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
	}
	


	public boolean isProperZip(String zip) {
		if(zip.contains("-")) {
			String[] zipParts = zip.split("-");
			if(zipParts.length != 2 || zipParts[0].length() != 5 || zipParts[1].length() != 4)
				return false;
			
			for(String part : zipParts) {
				if(!part.matches("[0-9]+"))
					return false;
			}
			return true;
		}
		
		return zip.length() == 5 && zip.matches("[0-9]+");
	}
	
	
	public boolean areValidDoubles(String[] stringArr) {
		for(String string : stringArr) {
			try {
				Double.parseDouble(string);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isProperLocation(String latitude, String longitude) {
		double latDouble = 0;
		double lonDouble = 0;
		try {
			latDouble = Double.parseDouble(latitude);
			lonDouble = Double.parseDouble(longitude);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		
		// The range for latitude and longitude has to be from -90 to 90 degrees and -180 to 180 degrees respectively
		boolean latInRange = latDouble >= -90 && latDouble <= 90;
		boolean lonInRange = lonDouble >= -180 && lonDouble <= 180;
		return latInRange && lonInRange;
	}
	
	public boolean isProperPassword(String password) {
		boolean hasProperLength = password.length() >= 8;
		boolean hasSpaces = password.contains(" ");
		boolean hasProperChars = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");// this checks if a password has a lowercase letter, a capital letter, and a digit
		return  hasProperLength && !hasSpaces && hasProperChars;
	}
	
	public boolean hasMatchingPasswords(String password, String reEnteredPassword) {
		return password.equals(reEnteredPassword);
	}	
}
