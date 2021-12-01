package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.control.*;
import models.*;
import utilities.SQLiteConnection;

public class CollegeProfileController implements Initializable {
	private CollegeProfileModel collegeProfileModel = new CollegeProfileModel(SQLiteConnection.getMainUrl());
	private ViewableCollege currentCollege;
	
	@FXML private Label collegeNameLabel;
	@FXML private TextArea infoTextArea;
	
	public void setCollege(ViewableCollege college) {
		currentCollege = college;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String strCollegeInfo = collegeProfileModel.getCollegeInfo(currentCollege);
		
		collegeNameLabel.setText(currentCollege.getName());
		infoTextArea.setText(strCollegeInfo);
	}
}
