package utilities;

import java.sql.*;

public class SQLiteConnection {
	
	public static Connection studentDBConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:data/studentDB.sqlite";
			Connection conn = DriverManager.getConnection(url);
			Statement statement = conn.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS students ("
					+ "studentId INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "firstName VARCHAR(50) NOT NULL,"
					+ "lastName VARCHAR(50) NOT NULL, " 
					+ "userName VARCHAR(50) NOT NULL UNIQUE,"
					+ "password VARCHAR(50) NOT NULL,"
					+ "zip INTEGER,"
					+ "favoritesId INTEGER NOT NULL"
					+ ")");
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e);
			return null;
		}
	}
}
