package controllers;

import javafx.fxml.*;
import javafx.scene.control.*;
import models.*;
import utilities.SQLiteConnection;

public class CollegeProfileController {
	private CollegeProfileModel collegeProfileModel = new CollegeProfileModel(SQLiteConnection.getMainUrl());
	private ViewableCollege currentCollege;
	
	@FXML private Label collegeNameLabel;
	@FXML private TextArea infoTextArea;
	
	public void setCollege(ViewableCollege college) {
		currentCollege = college;
		String strCollegeInfo = collegeProfileModel.getCollegeInfo(currentCollege);
		collegeNameLabel.setText(currentCollege.getName());
		infoTextArea.setText(strCollegeInfo);
	}
}
