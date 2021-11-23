package models;

import java.sql.*;

import utilities.*;

public abstract class ConnectionModel {
	private Connection conn = null;
	private String url;
	
	public ConnectionModel(String url) {
		this.url = url;
		conn = SQLiteConnection.connector(url);
		if(conn == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public boolean isConnectedDb() {
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void resetConnection() {
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
