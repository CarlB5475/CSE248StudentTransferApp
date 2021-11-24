package models;

import java.sql.*;

public class LoginModel extends ConnectionModel {
	
	public LoginModel(String url) {
		super(url);
	}

	public boolean isLogin(String user, String pass) {
		user = user.toLowerCase();
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM students WHERE userName=? and password=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			resultSet = preparedStatement.executeQuery();
			return resultSet.next();
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
	
	public ViewableStudent getLoggedStudent(String user) {
		ViewableStudent student = null;
		
		user = user.toLowerCase();
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM students WHERE userName=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, user);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			int studentId = resultSet.getInt("studentId"), favoritesId = resultSet.getInt("favoritesId");
			String firstName = resultSet.getString("firstName"), lastName = resultSet.getString("lastName");
			String userName = resultSet.getString("userName"), password = resultSet.getString("password"), zip = resultSet.getString("zip");
			double latitude = resultSet.getDouble("latitude"), longitude = resultSet.getDouble("longitude");
			
			student = new ViewableStudent(studentId, firstName, lastName, userName, password, zip, latitude, longitude, favoritesId);
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
		
		return student;
	}
}
