package models;

import java.sql.*;

public class LoginModel extends ConnectionModel {
	
	public LoginModel() {
		super();
	}

	public boolean isLogin(String user, String pass) {
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
}
