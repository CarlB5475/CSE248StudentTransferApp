package models;

import java.sql.*;
import java.util.LinkedList;

public class StudentProfileModel extends ConnectionModel {

	public StudentProfileModel(String url) {
		super(url);
	}
	
	public LinkedList<ViewableCollege> getFavorites(ViewableStudent student) {
		LinkedList<ViewableCollege> favoritesList = new LinkedList<>();
		String user = student.getUserName();
		
		resetConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String favoritesQuery = "SELECT * FROM students INNER JOIN favorites ON favorites.favoritesId = students.favoritesId WHERE userName=?";
		try {
			preparedStatement = getConnection().prepareStatement(favoritesQuery);
			preparedStatement.setString(1, user);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			for(int i = 1; i <= getMaxFavorites(); i++) {
				String collegeIdLabel = "collegeId" + i;
				if(!hasCollege(resultSet, collegeIdLabel))
					continue;
				int currentCollegeId = resultSet.getInt(collegeIdLabel);
				ViewableCollege currentCollege = getViewableCollege(currentCollegeId);
				favoritesList.add(currentCollege);
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
		
		return favoritesList;
	}
	
	private boolean hasCollege(ResultSet resultSet, String collegeIdLabel) {
		int currentCollegeId = 0;
		try {
			currentCollegeId = resultSet.getInt(collegeIdLabel);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return currentCollegeId != 0;
	}

	private ViewableCollege getViewableCollege(int collegeId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String collegeQuery = "SELECT * FROM colleges WHERE collegeId=?";
		try {
			preparedStatement = getConnection().prepareStatement(collegeQuery);
			preparedStatement.setInt(1, collegeId);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			return formViewableCollege(collegeId, resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		} finally {
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private ViewableCollege formViewableCollege(int collegeId, ResultSet resultSet) {
		ViewableCollege college = null;
		try {
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
