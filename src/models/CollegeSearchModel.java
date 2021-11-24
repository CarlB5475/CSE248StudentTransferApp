package models;

import java.sql.*;
import java.util.LinkedList;

public class CollegeSearchModel extends ConnectionModel {

	public CollegeSearchModel(String url) {
		super(url);
	}
	
	public LinkedList<ViewableCollege> searchColleges(String[] predicateStatements, ViewableStudent currentStudent, int radius) {
		LinkedList<ViewableCollege> collegeList = searchCollegesByQuery(predicateStatements);
		if(radius == -1) // if radius isn't being used here
			return collegeList;
		filterCollegesByRadius(collegeList, currentStudent, radius);
		return collegeList;
	}
	
	public void addFavoriteToStudent(ViewableCollege favoriteCollege, ViewableStudent student) {
		
	}
	
	private LinkedList<ViewableCollege> searchCollegesByQuery(String[] predicateStatements) {
		LinkedList<ViewableCollege> collegeList = new LinkedList<>();
		
		String query = getCompleteQuery(predicateStatements);
		
		resetConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = getConnection().createStatement();
			resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				
			}
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
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return college;
	}
	
	/* This fills the query with predicate statements.
	 * I.e "SELECT * FROM colleges WHERE zip='11720' AND attendanceCost<=20000 AND collegeType='Public' AND studentSize<=200000"
	 * Statements like "zip='11720'" is considered a predicate statement.
	 */
	private String getCompleteQuery(String[] predicateStatements) {
		String query = "SELECT * FROM colleges";
		if(predicateStatements.length < 1)
			return query;
		
		query += " WHERE ";
		for(int i = 0; i < predicateStatements.length; i++) {
			query += predicateStatements[i];
			if(i < predicateStatements.length - 1) 
				query += " AND ";
		}
		
		return query;
	}
	
	private void filterCollegesByRadius(LinkedList<ViewableCollege> collegeList, ViewableStudent currentStudent, int radius) {
		
	}
}
