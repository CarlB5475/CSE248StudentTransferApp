package utilities;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.Scanner;

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
					+ "collegeId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "name VARCHAR(50) NOT NULL,"
					+ "url VARCHAR(200),"
					+ "city VARCHAR(200) NOT NULL, " 
					+ "state VARCHAR(50) NOT NULL,"
					+ "collegeZip VARCHAR(20) NOT NULL,"
					+ "tuition FLOAT NOT NULL,"
					+ "studentSize INTEGER NOT NULL,"
					+ "collegeType INTEGER NOT NULL" // 1 = public, 2 = private non-profit, 3 = private for-profit
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
		final String INITIAL_URL_STRING = "https://api.data.gov/ed/collegescorecard/v1/schools.json"
				+ "?school.degrees_awarded.predominant=3,academics.program_bachelors_computer"
				+ "&_fields=id,school.name,school.school_url,school.city,school.state,school.zip,"
				+ "cost.avg_net_price.public,cost.avg_net_price.private,latest.student.size,school.ownership"
				+ "&api_key=XZjJrfMKdwPnCZRvOpEYvnPEHFpBm7uvaY2Ibcu8"
				+ "&_per_page=100";
		
		if(hasCollegeData(conn)) // if there are colleges in the table, don't initialize
			return;
		
		String inline = "";
		
		try {
			URL url = new URL(INITIAL_URL_STRING);
			
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
			
		} catch (IOException e) {
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
