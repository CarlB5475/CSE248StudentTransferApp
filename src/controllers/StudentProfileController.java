package controllers;

import models.*;

public class StudentProfileController {
	private ViewableStudent loggedStudent;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
	}
}
