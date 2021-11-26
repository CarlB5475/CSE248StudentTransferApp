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
	
	public void addFavoriteToStudent(ViewableCollege favoriteCollege, ViewableStudent student) {
		
	}
	
	public String formZipPredicate(String zipCode) {
		if(zipCode.equals(""))
			return "";
		return "collegeZip LIKE '%" + zipCode + "%'";
	}
	
	public String formMaxCostPredicate(int attendanceCost) {
		if(attendanceCost == -1) // attendanceCost is not being used
			return "";
		return "attendanceCost <= " + attendanceCost;
	}
	
	public String formCollegeTypePredicate(String collegeType) {
		if(collegeType.equals(""))
			return "";
		return "collegeType = '" + collegeType + "'";
	}
	
	public String formMaxStudentSizePredicate(int studentSize) {
		if(studentSize == -1) // studentSize is not being used
			return "";
		return "studentSize <= " + studentSize;
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
	 * I.e "SELECT * FROM colleges WHERE zip LIKE '%11720%' AND attendanceCost<=20000 AND collegeType='Public' AND studentSize<=200000"
	 * Statements like "zip='11720'" is considered a predicate statement.
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
		double distance = 0;
		final int EXPONENT = 2;
		double studentMilesLatitude = convertLatitudeToMiles(studentLatitude);
		double collegeMilesLatitude = convertLatitudeToMiles(collegeLatitude);
		double studentMilesLongitude = convertLongitudeToMiles(studentLongitude);
		double collegeMilesLongitude = convertLongitudeToMiles(studentLongitude);
		
		double totalMilesLatitude = Math.pow((collegeMilesLatitude - studentMilesLatitude), EXPONENT);
		double totalMilesLongitude = Math.pow((collegeMilesLongitude - studentMilesLongitude), EXPONENT);
		distance = Math.sqrt(totalMilesLatitude + totalMilesLongitude);
		return distance;
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
