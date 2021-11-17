package utilities;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.Scanner;

import com.fasterxml.jackson.databind.*;

public class SQLiteConnection {
	
	public static Connection connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:data/transferAppDB.sqlite";
			Connection conn = DriverManager.getConnection(url);
			initializeTables(conn);
			initializeCollegeData(conn);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e);
			return null;
		}
	}
	
	private static void initializeTables(Connection conn) {
		try {
			Statement statement = conn.createStatement();
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS students ("
					+ "studentId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "firstName VARCHAR(50) NOT NULL,"
					+ "lastName VARCHAR(50) NOT NULL, " 
					+ "userName VARCHAR(50) NOT NULL UNIQUE,"
					+ "password VARCHAR(50) NOT NULL,"
					+ "zip VARCHAR(20) NOT NULL,"
					+ "favoritesId INTEGER NOT NULL"
					+ ")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS colleges ("
					+ "collegeId INTEGER PRIMARY KEY,"
					+ "name VARCHAR(50) NOT NULL,"
					+ "url VARCHAR(200),"
					+ "city VARCHAR(200) NOT NULL, " 
					+ "state VARCHAR(50) NOT NULL,"
					+ "collegeZip VARCHAR(20) NOT NULL,"
					+ "tuitionCost INTEGER NOT NULL,"
					+ "studentSize INTEGER NOT NULL,"
					+ "collegeType VARCHAR(30) NOT NULL" 
					+ ")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS favorites ("
					+ "favoritesId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "collegeId1 INTEGER,"
					+ "collegeId2 INTEGER,"
					+ "collegeId3 INTEGER,"
					+ "collegeId4 INTEGER,"
					+ "collegeId5 INTEGER,"
					+ "collegeId6 INTEGER,"
					+ "collegeId7 INTEGER,"
					+ "collegeId8 INTEGER,"
					+ "collegeId9 INTEGER,"
					+ "collegeId10 INTEGER"
					+ ")");
		} catch (SQLException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	private static void initializeCollegeData(Connection conn) {
		final String URL_STRING = "https://api.data.gov/ed/collegescorecard/v1/schools.json"
				+ "?school.degrees_awarded.predominant=3,academics.program_bachelors_computer"
				+ "&_fields=id,school.name,school.school_url,school.city,school.state,school.zip,"
				+ "latest.cost.attendance.academic_year,latest.student.size,school.ownership"
				+ "&api_key=XZjJrfMKdwPnCZRvOpEYvnPEHFpBm7uvaY2Ibcu8"
				+ "&_per_page=100";
		
		if(hasCollegeData(conn)) // if there are colleges in the table, don't initialize
			return;
		
		long totalPages = Math.round(getTotalPages(URL_STRING));
		
		for(int currentPage = 0; currentPage < totalPages; currentPage++) {
			System.out.println("Current page: " + currentPage);
			String currentUrlString = URL_STRING + "&_page=" + currentPage;
			JsonNode currentMainNode = getMainNode(currentUrlString); // gets the node with the results in the current page
			JsonNode currentResultsArr = currentMainNode.get("results");
			
			for(int i = 0; i < currentResultsArr.size(); i++) {
				JsonNode currentNode = currentResultsArr.get(i);
				addCollegeData(conn, currentNode);
			}
		}
	}
	
	private static void addCollegeData(Connection conn, JsonNode currentResultNode) {
		JsonNode collegeIdNode = currentResultNode.get("id");
		JsonNode nameNode = currentResultNode.get("school.name");
		JsonNode urlNode = currentResultNode.get("school.school_url");
		JsonNode cityNode = currentResultNode.get("school.city");
		JsonNode stateNode = currentResultNode.get("school.state");
		JsonNode collegeZipNode = currentResultNode.get("school.zip");
		JsonNode averageCostNode = currentResultNode.get("latest.cost.attendance.academic_year");
		JsonNode studentSizeNode = currentResultNode.get("latest.student.size");
		JsonNode collegeTypeNode = currentResultNode.get("school.ownership"); // 1 = public, 2 = private non-profit, 3 = private for-profit
		
		String collegeTypeString = null;
		switch (collegeTypeNode.asInt()) {
			case 1: collegeTypeString = "Public";
					break;
			case 2: collegeTypeString = "Private Non-Profit";
					break;
			case 3: collegeTypeString = "Private For-Profit";
					break;
		}
		
		PreparedStatement preparedStatement = null;
		String statementString = "INSERT INTO colleges (collegeId, name, url, city, state, collegeZip, tuitionCost, studentSize, collegeType)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			preparedStatement = conn.prepareStatement(statementString);
			preparedStatement.setInt(1, collegeIdNode.asInt());
			preparedStatement.setString(2, nameNode.asText());
			preparedStatement.setString(3, urlNode.asText());
			preparedStatement.setString(4, cityNode.asText());
			preparedStatement.setString(5, stateNode.asText());
			preparedStatement.setString(6, collegeZipNode.asText());
			preparedStatement.setInt(7, averageCostNode.asInt());
			preparedStatement.setInt(8, studentSizeNode.asInt());
			preparedStatement.setString(9, collegeTypeString);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	private static double getTotalPages(String urlString) {
		JsonNode mainNode = getMainNode(urlString);
		JsonNode metaDataNode = mainNode.get("metadata");
		JsonNode totalField = metaDataNode.get("total");
		JsonNode perPageField = metaDataNode.get("per_page");
		
		return Math.ceil(totalField.asDouble() / perPageField.asInt());
	}
	
	private static JsonNode getMainNode(String urlString) {
		String inline = "";
		JsonNode mainNode = null;
		
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			int responsecode = urlConn.getResponseCode();
			
			if (responsecode != 200) {
				System.out.println("HttpResponseCode: " + responsecode);
				System.exit(responsecode);
			}
			
			Scanner sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				inline += sc.nextLine();
			}
			sc.close();
			System.out.println(inline);
			
			ObjectMapper objectMapper = new ObjectMapper();
			mainNode = objectMapper.readValue(inline, JsonNode.class);	
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
		return mainNode;
	}
	
	private static boolean hasCollegeData(Connection conn) {
		try {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM colleges");
			return result.next();
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
}
