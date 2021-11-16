package models;

import java.sql.*;

import utilities.*;

public class LoginModel {
	private Connection conn = null;
	
	public LoginModel() {
		conn = SQLiteConnection.connector();
		if(conn == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
	
	public boolean isConnectedDb() {
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isLogin(String user, String pass) {
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM students WHERE userName=? and password=?";
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			return false;
		}
	}

	public void resetConnection() {
		try {
			if(conn.isClosed()) {
				conn = SQLiteConnection.connector();
				if(conn == null) {
					System.out.println("Connection not successful");
					System.exit(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
