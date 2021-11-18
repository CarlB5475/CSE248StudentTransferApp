package models;

import java.sql.*;

public class SignUpModel extends ConnectionModel {
	
	public SignUpModel() {
		super();
	}
	
	public void addNewStudent(String firstName, String lastName, String username, String zip, String password, String reEnteredPassword) {
		
	}
	
	// if name has letters only
	public boolean isValidName(String firstName, String lastName) {
		return false;
	}
	
	// if username does not already exists and has no spaces
	public boolean isValidUsername(String username) {
		return false;
	}
	
	// if zip has numbers only
	public boolean isValidZip(String zip) {
		return false;
	}
	
	// if password has at least 8 characters, has no space, and has a lowercase and uppercase letter and a digit
	public boolean isValidPassword(String password, String reEnteredPassword) {
		return false;
	}
}
