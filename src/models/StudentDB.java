package models;

import java.sql.*;

import utilities.SQLiteConnection;

public class StudentDB {
	private static Connection connection;
	
	private StudentDB() {
	}
	
	public static boolean isConnectedDb() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void resetConnection() {
		connection = SQLiteConnection.studentDBConnector();
		if(connection == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
}
