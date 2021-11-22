package models;

public class ViewableStudent {
	private int studentId;
	private String firstName, lastName;
	private String userName, password, zip;
	private double latitude, longitude;
	private int favoritesId;
	
	public ViewableStudent(int studentId, String firstName, String lastName, String userName, String password,
			String zip, double latitude, double longitude, int favoritesId) {
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
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
}
