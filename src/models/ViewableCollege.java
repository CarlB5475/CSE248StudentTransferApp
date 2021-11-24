package models;

public class ViewableCollege {
	private int collegeId; // college id is already unique from SQLite database
	String name, url;
	String city, state, collegeZip;
	double latitude, longitude;
	int attendanceCost, studentSize;
	String collegeType;
	
	public ViewableCollege(int collegeId, String name, String url, String city, String state, String collegeZip,
			double latitude, double longitude, int attendanceCost, int studentSize, String collegeType) {
		super();
		this.collegeId = collegeId;
		this.name = name;
		this.url = url;
		this.city = city;
		this.state = state;
		this.collegeZip = collegeZip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.attendanceCost = attendanceCost;
		this.studentSize = studentSize;
		this.collegeType = collegeType;
	}

	public int getCollegeId() {
		return collegeId;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCollegeZip() {
		return collegeZip;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getAttendanceCost() {
		return attendanceCost;
	}

	public int getStudentSize() {
		return studentSize;
	}

	public String getCollegeType() {
		return collegeType;
	}
	
	
}
