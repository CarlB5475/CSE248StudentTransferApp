package models;

public class CollegeProfileModel extends ConnectionModel {

	public CollegeProfileModel(String url) {
		super(url);
	}

	public String getCollegeInfo(ViewableCollege currentCollege) {
		return "College Id: " + currentCollege.getCollegeId() + "\n" + 
				"College Url: " + currentCollege.getUrl() + "\n" + 
				"College Location: " + currentCollege.getCity() + ", " + currentCollege.getState() + ", " + currentCollege.getCollegeZip() + "\n" + 
				"College Coordinates (Latitude, Longitude): (" + currentCollege.getLatitude() + ", " + currentCollege.getLongitude() + ")\n" + 
				"College Attendance Cost: " + currentCollege.getAttendanceCost() + "\n" + 
				"College Student Size: " + currentCollege.getStudentSize() + "\n" + 
				"College Type: " + currentCollege.getCollegeType();
	}
	
	
}
