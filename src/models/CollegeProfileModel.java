package models;

public class CollegeProfileModel extends ConnectionModel {

	public CollegeProfileModel(String url) {
		super(url);
	}

	public String getCollegeInfo(ViewableCollege currentCollege) {
		return 
				"College Id: " + currentCollege.getCollegeId() + "\n" + 
				"College Url: " + currentCollege.getUrl() + "\n" + 
				"College Address: " + currentCollege.getState();
	}
	
	
}
