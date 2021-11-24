package controllers;

import models.*;
import utilities.*;

public class CollegeSearchController {
	private ViewableStudent loggedStudent;
	private CollegeSearchModel collegeSearchModel = new CollegeSearchModel(SQLiteConnection.getMainUrl());
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
	}
}
