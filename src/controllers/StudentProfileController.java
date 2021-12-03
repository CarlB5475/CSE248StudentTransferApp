package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import models.*;
import utilities.*;

public class StudentProfileController {
	private ViewableStudent loggedStudent;
	private StudentProfileModel studentProfileModel = new StudentProfileModel(SQLiteConnection.getMainUrl());
	private ObservableList<ViewableCollege> favoriteColleges;
	
	@FXML private Label profileLabel;
	@FXML private MenuBar menuBar;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
		String strProfileLabel = student.getFirstName() + " " + student.getLastName() + " Profile";
		profileLabel.setText(strProfileLabel);
	}
}
