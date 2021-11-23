package tests;

import java.sql.*;

import utilities.SQLiteConnection;

public class TestingUtilities {
	private static final String TEST_URL = "jdbc:sqlite:data/testDB.sqlite";
	
	public static String getTestUrl() {
		return TEST_URL;
	}
	
	public static void resetTestFile(Connection conn, String url) {
		resetConnection(conn, url);
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("DROP TABLE IF EXISTS students");
			statement.executeUpdate("DROP TABLE IF EXISTS favorites");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static void resetConnection(Connection conn, String url) {
		try {
			if(conn.isClosed()) {
				conn = SQLiteConnection.connector(url);
				if(conn == null) {
					System.out.println("Connection not successful");
					System.exit(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
