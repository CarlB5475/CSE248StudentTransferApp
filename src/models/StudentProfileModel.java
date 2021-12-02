package models;

import java.sql.*;
import java.util.LinkedList;

public class StudentProfileModel extends ConnectionModel {

	public StudentProfileModel(String url) {
		super(url);
	}
	
	public LinkedList<ViewableCollege> getFavorites(ViewableStudent student) {
		LinkedList<ViewableCollege> favoritesList = new LinkedList<>();
		int favoritesId = student.getFavoritesId();
		
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM favorites WHERE favoritesId=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setInt(1, favoritesId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			
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
		
		return favoritesList;
	}

	private ViewableCollege getCollege(ResultSet resultSet) {
		ViewableCollege college = null;
		try {
			int collegeId = resultSet.getInt("collegeId");
			String name = resultSet.getString("name"), url = resultSet.getString("url");
			String city = resultSet.getString("city"), state = resultSet.getString("state"), collegeZip = resultSet.getString("collegeZip");
			double latitude = resultSet.getDouble("latitude"), longitude = resultSet.getDouble("longitude");
			int attendanceCost = resultSet.getInt("attendanceCost"), studentSize = resultSet.getInt("studentSize");
			String collegeType = resultSet.getString("collegeType");
			
			college = new ViewableCollege(collegeId, name, url, city, state, collegeZip, latitude, longitude, attendanceCost, studentSize, collegeType);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return college;
	}
}
