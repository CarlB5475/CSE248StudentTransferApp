package models;

import java.util.Objects;

public class ViewableCollege {
	private int collegeId; // college id is already unique from SQLite database
	private String name, url;
	private String city, state, collegeZip;
	private double latitude, longitude;
	private int attendanceCost, studentSize;
	private String collegeType;
	
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

	@Override
	public int hashCode() {
		return Objects.hash(attendanceCost, city, collegeId, collegeType, collegeZip, latitude, longitude, name, state,
				studentSize, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewableCollege other = (ViewableCollege) obj;
		return attendanceCost == other.attendanceCost && Objects.equals(city, other.city)
				&& collegeId == other.collegeId && Objects.equals(collegeType, other.collegeType)
				&& Objects.equals(collegeZip, other.collegeZip)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude)
				&& Objects.equals(name, other.name) && Objects.equals(state, other.state)
				&& studentSize == other.studentSize && Objects.equals(url, other.url);
	}

	@Override
	public String toString() {
		return "ViewableCollege [collegeId=" + collegeId + ", name=" + name + ", url=" + url + ", city=" + city
				+ ", state=" + state + ", collegeZip=" + collegeZip + ", latitude=" + latitude + ", longitude="
				+ longitude + ", attendanceCost=" + attendanceCost + ", studentSize=" + studentSize + ", collegeType="
				+ collegeType + "]";
	}
}
