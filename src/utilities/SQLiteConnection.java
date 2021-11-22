package utilities;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.LinkedList;
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
			e.printStackTrace();
			return null;
		}
	}
	
	// must have "jdbc:sqlite:data/example.sqlite"
	public static Connection connector(String url) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection(url);
			initializeTables(conn);
			initializeCollegeData(conn);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void initializeTables(Connection conn) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS students ("
					+ "studentId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "firstName VARCHAR(50) NOT NULL,"
					+ "lastName VARCHAR(50) NOT NULL, " 
					+ "userName VARCHAR(50) NOT NULL UNIQUE,"
					+ "password VARCHAR(50) NOT NULL,"
					+ "zip VARCHAR(20) NOT NULL,"
					+ "latitude DOUBLE NOT NULL,"
					+ "longitude DOUBLE NOT NULL,"
					+ "favoritesId INTEGER NOT NULL"
					+ ")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS colleges ("
					+ "collegeId INTEGER PRIMARY KEY,"
					+ "name VARCHAR(50) NOT NULL,"
					+ "url VARCHAR(200),"
					+ "city VARCHAR(200) NOT NULL, " 
					+ "state VARCHAR(50) NOT NULL,"
					+ "collegeZip VARCHAR(20) NOT NULL,"
					+ "latitude DOUBLE NOT NULL,"
					+ "longitude DOUBLE NOT NULL,"
					+ "attendanceCost INTEGER NOT NULL,"
					+ "studentSize INTEGER NOT NULL,"
					+ "collegeType VARCHAR(30) NOT NULL,"
					+ "hasCSBachelorsDegree VARCHAR(10) NOT NULL" 
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
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static void initializeCollegeData(Connection conn) {
		final String URL_STRING = "https://api.data.gov/ed/collegescorecard/v1/schools.json"
				+ "?school.degrees_awarded.predominant=3,latest.academics.program.bachelors.computer"
				+ "&_fields=id,school.name,school.school_url,school.city,school.state,school.zip,location.lat,location.lon,"
				+ "latest.cost.attendance.academic_year,latest.student.size,school.ownership,latest.academics.program.bachelors.computer"
				+ "&api_key=XZjJrfMKdwPnCZRvOpEYvnPEHFpBm7uvaY2Ibcu8"
				+ "&_per_page=100";
		
		if(hasCollegeData(conn)) // if there are colleges in the table, don't initialize
			return;
		
		long totalPages = Math.round(getTotalPages(URL_STRING));
		LinkedList<JsonNode> nodesList = getNodesList(totalPages, URL_STRING);
		nodesList.forEach(node -> addCollegeData(conn, node));
	}
	
	private static void addCollegeData(Connection conn, JsonNode currentResultNode) {
		JsonNode collegeIdNode = currentResultNode.get("id");
		JsonNode nameNode = currentResultNode.get("school.name");
		JsonNode urlNode = currentResultNode.get("school.school_url");
		JsonNode cityNode = currentResultNode.get("school.city");
		JsonNode stateNode = currentResultNode.get("school.state");
		JsonNode collegeZipNode = currentResultNode.get("school.zip");
		JsonNode latNode = currentResultNode.get("location.lat");
		JsonNode lonNode = currentResultNode.get("location.lon");
		JsonNode averageCostNode = currentResultNode.get("latest.cost.attendance.academic_year");
		JsonNode studentSizeNode = currentResultNode.get("latest.student.size");
		JsonNode collegeTypeNode = currentResultNode.get("school.ownership"); // 1 = public, 2 = private non-profit, 3 = private for-profit
		JsonNode hasCSDegreeNode = currentResultNode.get("latest.academics.program.bachelors.computer");
		
		String collegeTypeString = null;
		switch (collegeTypeNode.asInt()) {
			case 1: collegeTypeString = "Public";
					break;
			case 2: collegeTypeString = "Private Non-Profit";
					break;
			case 3: collegeTypeString = "Private For-Profit";
					break;
		}
		
		String hasCSBachelorsDegreeString = null;
		if(hasCSDegreeNode.asInt() > 0)
			hasCSBachelorsDegreeString = "True";
		else
			hasCSBachelorsDegreeString = "False";
		
		PreparedStatement preparedStatement = null;
		String statementString = "INSERT INTO colleges (collegeId, name, url, city, state, collegeZip, latitude, longitude,"
				+ "attendanceCost, studentSize, collegeType, hasCSBachelorsDegree)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		/* 1 = collegeId, 2 = name, 3 = url, 4 = city, 5 = state, 6 = collegeZip, 7 = latitude, 8 = longitude
		 * 9 = attendanceCost, 10 = studentSize, 11 = collegeType, 12 = hasCSBachelorsDegree
		 */
		
		try {
			preparedStatement = conn.prepareStatement(statementString);
			preparedStatement.setInt(1, collegeIdNode.asInt());
			preparedStatement.setString(2, nameNode.asText());
			preparedStatement.setString(3, urlNode.asText());
			preparedStatement.setString(4, cityNode.asText());
			preparedStatement.setString(5, stateNode.asText());
			preparedStatement.setString(6, collegeZipNode.asText());
			preparedStatement.setDouble(7, latNode.asDouble());
			preparedStatement.setDouble(8, lonNode.asDouble());
			preparedStatement.setInt(9, averageCostNode.asInt());
			preparedStatement.setInt(10, studentSizeNode.asInt());
			preparedStatement.setString(11, collegeTypeString);
			preparedStatement.setString(12, hasCSBachelorsDegreeString);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static LinkedList<JsonNode> getNodesList(long totalPages, String urlString) {
		LinkedList<JsonNode> nodesList = new LinkedList<>();
		for(int currentPage = 0; currentPage < totalPages; currentPage++) {
			String currentUrlString = urlString + "&_page=" + currentPage;
			JsonNode currentMainNode = getMainNode(currentUrlString); 
			JsonNode currentResultsArrNode = currentMainNode.get("results"); // gets the node with the results in the current page
			addCurrentFilteredNodes(nodesList, currentResultsArrNode); // adds filtered results from current page with bachelors CS degree to the list
		}
		return nodesList;
	}
	
	private static void addCurrentFilteredNodes(LinkedList<JsonNode> nodesList, JsonNode resultsArrNode) {
		final String FILTER_STRING = "latest.academics.program.bachelors.computer";
		
		for(int i = 0; i < resultsArrNode.size(); i++) {
			JsonNode currentNode = resultsArrNode.get(i);
			JsonNode filterNode = currentNode.get(FILTER_STRING);
			if(filterNode.asInt() > 0)
				nodesList.add(currentNode);
		}
	}
	
	private static double getTotalPages(String urlString) {
		JsonNode mainNode = getMainNode(urlString);
		JsonNode metadataNode = mainNode.get("metadata");
		JsonNode totalField = metadataNode.get("total");
		JsonNode perPageField = metadataNode.get("per_page");
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
			int responseCode = urlConn.getResponseCode();
			
			if (responseCode != 200) {
				System.out.println("HttpResponseCode: " + responseCode);
				System.exit(responseCode);
			}
			
			Scanner sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				inline += sc.nextLine();
			}
			sc.close();
			
			ObjectMapper objectMapper = new ObjectMapper();
			mainNode = objectMapper.readValue(inline, JsonNode.class);	
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return mainNode;
	}
	
	private static boolean hasCollegeData(Connection conn) {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM colleges");
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
