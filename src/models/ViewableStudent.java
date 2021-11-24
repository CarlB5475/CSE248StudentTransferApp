package models;

import java.util.Objects;

public class ViewableStudent {
	private int studentId; // student id is already unique from SQLite database
	private String firstName, lastName;
	private String userName, password, zip;
	private double latitude, longitude;
	private int favoritesId;
	
	public ViewableStudent(int studentId, String firstName, String lastName, String userName, String password,
			String zip, double latitude, double longitude, int favoritesId) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		userName = userName.toLowerCase();
		this.userName = userName;
		this.password = password;
		this.zip = zip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.favoritesId = favoritesId;
	}

	public int getStudentId() {
		return studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getZip() {
		return zip;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getFavoritesId() {
		return favoritesId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(favoritesId, firstName, lastName, latitude, longitude, password, studentId, userName, zip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewableStudent other = (ViewableStudent) obj;
		return favoritesId == other.favoritesId && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude)
				&& Objects.equals(password, other.password) && studentId == other.studentId
				&& Objects.equals(userName, other.userName) && Objects.equals(zip, other.zip);
	}
}
