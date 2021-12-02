package controllers;

import javafx.collections.ObservableList;
import models.*;
import utilities.*;

public class StudentProfileController {
	private ViewableStudent loggedStudent;
	private StudentProfileModel studentProfileModel = new StudentProfileModel(SQLiteConnection.getMainUrl());
	private ObservableList<ViewableCollege> favoriteColleges;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
	}
}
