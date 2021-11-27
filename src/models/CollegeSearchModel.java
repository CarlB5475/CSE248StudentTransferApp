package models;

import java.sql.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class CollegeSearchModel extends ConnectionModel {

	public CollegeSearchModel(String url) {
		super(url);
	}
	
	public LinkedList<ViewableCollege> searchColleges(LinkedList<String> predicateStatements, ViewableStudent currentStudent, double radius) {
		LinkedList<ViewableCollege> collegeList = searchCollegesByQuery(predicateStatements);
		if(radius == -1) // if radius isn't being used here
			return collegeList;
		collegeList = filterCollegesByRadius(collegeList, currentStudent, radius);
		return collegeList;
	}
	
	// returns true if the favorite college has been added; return false if there's no room for another favorite college
	public boolean addFavoriteToStudent(ViewableCollege favoriteCollege, ViewableStudent student) {
		String user = student.getUserName();
		final int MAX_FAVORITES = 10;
		
		resetConnection();
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// gets both the students table and favorites table for the given student
		String query = "SELECT * FROM students INNER JOIN favorites ON favorites.favoritesId = students.favoritesId WHERE userName=?";
		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, user);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			statement = getConnection().createStatement();
			for(int i = 1; i <= MAX_FAVORITES; i++) {
				String collegeIdLabel = "collegeId" + i;
				int currentCollegeId = resultSet.getInt(collegeIdLabel);
				if(currentCollegeId != 0) // if there is already a college id in there
					continue;
				
				String updateStatement = "UPDATE favorites SET " + collegeIdLabel + " = " + favoriteCollege.getCollegeId() + 
						" WHERE favoritesId = " + student.getFavoritesId();
				statement.executeUpdate(updateStatement);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				statement.close();
				preparedStatement.close();
				resultSet.close();
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return false;
	}
	
	public String formZipPredicate(String zipCode) {
		if(zipCode.equals(""))
			return "";
		return "collegeZip LIKE '%" + zipCode + "%'";
	}
	
	public String formCostPredicate(int minCost, int maxCost) {
		boolean validRange = true;
		if(minCost != -1 && maxCost != -1)
			validRange = minCost <= maxCost;

		if(minCost == -1 && maxCost == -1 || !validRange) // cost is not being used
			return "";
		if(maxCost == -1) // maxCost is not being used
			return "attendanceCost >= " + minCost;
		if(minCost == -1) // minCost is not being used
			return "attendanceCost <= " + maxCost;
		return "attendanceCost >= " + minCost + " AND attendanceCost <= " + maxCost;
	}
	
	public String formCollegeTypePredicate(String collegeType) {
		boolean validCollegeType = collegeType.equals("Public") || collegeType.equals("Private Non-Profit") || collegeType.equals("Private For-Profit");
		if(collegeType.equals("") || !validCollegeType)
			return "";
		return "collegeType = '" + collegeType + "'";
	}
	
	public String formStudentSizePredicate(int minSize, int maxSize) {
		boolean validRange = true;
		if(minSize != -1 && maxSize != -1)
			validRange = minSize <= maxSize;
			
		if(minSize == -1 && maxSize == -1 || !validRange) // studentSize is not being used
			return "";
		if(maxSize == -1) // maxSize is not being used
			return "studentSize >= " + minSize;
		if(minSize == -1) // minSize is not being used
			return "studentSize <= " + maxSize;
		
		return "studentSize >= " + minSize + " AND studentSize <= " + maxSize;
	}
	
	private LinkedList<ViewableCollege> searchCollegesByQuery(LinkedList<String> predicateStatements) {
		LinkedList<ViewableCollege> collegeList = new LinkedList<>();
		String query = getCompleteQuery(predicateStatements);
		
		resetConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().createStatement();
			resultSet = statement.executeQuery(query);
			while(resultSet.next())
				collegeList.add(getCollege(resultSet));
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
		
		return collegeList;
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
	
	/* This fills the query with predicate statements.
	 * I.e "SELECT * FROM colleges WHERE collegeZip LIKE '%11720%' AND attendanceCost<=20000 AND collegeType='Public' AND studentSize<=200000"
	 * Statements like "collegeZip='11720'" is considered a predicate statement.
	 */
	private String getCompleteQuery(LinkedList<String> predicateStatements) {
		String query = "SELECT * FROM colleges";
		if(predicateStatements.isEmpty())
			return query;
		
		query += " WHERE ";
		ListIterator<String> predicateStatementIter = predicateStatements.listIterator();
		while(predicateStatementIter.hasNext()) {
			query += predicateStatementIter.next();
			if(predicateStatementIter.hasNext())
				query += " AND ";
		}
		return query;
	}
	
	private LinkedList<ViewableCollege> filterCollegesByRadius(LinkedList<ViewableCollege> collegeList, ViewableStudent currentStudent, double radius) {
		LinkedList<ViewableCollege> filteredCollegeList = new LinkedList<>();
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		while(collegeListIter.hasNext()) {
			ViewableCollege currentCollege = collegeListIter.next();
			double distanceFromCollege = calculateDistance(
					currentStudent.getLatitude(), currentCollege.getLatitude(),
					currentStudent.getLongitude(), currentCollege.getLongitude());
			if(distanceFromCollege <= radius) 
				filteredCollegeList.add(currentCollege);
		}	
		return filteredCollegeList;
	}
	
	public double calculateDistance(double studentLatitude, double collegeLatitude, double studentLongitude, double collegeLongitude) {
		final int EXPONENT = 2;
		double differenceMilesLatitude = convertLatitudeToMiles(collegeLatitude - studentLatitude);
		double differenceMilesLongitude = convertLongitudeToMiles(collegeLongitude - studentLongitude);
		return Math.sqrt(Math.pow(differenceMilesLatitude, EXPONENT) + Math.pow(differenceMilesLongitude, EXPONENT));
	}
	
	private double convertLatitudeToMiles(double latitude) {
		final double MILES_PER_DEGREE = 69;
		return latitude * MILES_PER_DEGREE;
	}
	
	private double convertLongitudeToMiles(double longitude) {
		final double MILES_PER_DEGREE = 54.6;
		return longitude * MILES_PER_DEGREE;
	}
}
