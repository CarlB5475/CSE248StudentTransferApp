package utilities;

import java.sql.*;

public class SQLiteConnection {
	
	public static Connection connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:data/transferAppDB.sqlite";
			Connection conn = DriverManager.getConnection(url);
			initializeTables(conn);
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
					+ "zip INTEGER NOT NULL,"
					+ "favoritesId INTEGER NOT NULL"
					+ ")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS colleges ("
					+ "collegeId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "collegeName VARCHAR(50) NOT NULL,"
					+ "collegeUrl VARCHAR(200),"
					+ "collegeCity VARCHAR(200) NOT NULL, " 
					+ "collegeState VARCHAR(50) NOT NULL,"
					+ "collegeZip INTEGER NOT NULL,"
					+ "collegeTuition INTEGER NOT NULL"
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
}
