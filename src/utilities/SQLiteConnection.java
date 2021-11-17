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
		
		String inline = "";
		
		try {
			URL url = new URL(URL_STRING);
			
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			int responsecode = urlConn.getResponseCode();
			System.out.println("Response code is: " + responsecode);
			
			if (responsecode != 200) {
				System.out.println("HttpResponseCode: " + responsecode);
				System.exit(responsecode);
			}
			
			Scanner sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				inline += sc.nextLine();
			}
			System.out.println(inline);
			sc.close();
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode node = objectMapper.readValue(inline, JsonNode.class);
			JsonNode array = node.get("results");
			
			for(int i = 0; i < array.size(); i++) {
				JsonNode currentNode = array.get(i);
				addCollegeData(conn, currentNode);
			}
			
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	private static void addCollegeData(Connection conn, JsonNode currentNode) {
		JsonNode collegeIdNode = currentNode.get("id");
		JsonNode nameNode = currentNode.get("school.name");
		JsonNode urlNode = currentNode.get("school.school_url");
		JsonNode cityNode = currentNode.get("school.city");
		JsonNode stateNode = currentNode.get("school.state");
		JsonNode collegeZipNode = currentNode.get("school.zip");
		JsonNode averageCostNode = currentNode.get("latest.cost.attendance.academic_year");
		JsonNode studentSizeNode = currentNode.get("latest.student.size");
		JsonNode collegeTypeNode = currentNode.get("school.ownership"); // 1 = public, 2 = private non-profit, 3 = private for-profit
		
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
