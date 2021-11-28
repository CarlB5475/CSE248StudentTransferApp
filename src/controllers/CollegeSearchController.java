package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.*;
import utilities.*;

public class CollegeSearchController implements Initializable {
	private ViewableStudent loggedStudent;
	private CollegeSearchModel collegeSearchModel = new CollegeSearchModel(SQLiteConnection.getMainUrl());
	
	@FXML private Menu optionsMenu;
	
	@FXML private CheckBox zipCodeCheckBox; 
	@FXML private CheckBox distanceCheckBox;
	@FXML private CheckBox attendanceCostCheckBox;
	@FXML private CheckBox collegeTypeCheckBox;
	@FXML private CheckBox studentSizeCheckBox;
	
	@FXML private TextField radiusInput;
	@FXML private TextField minCostInput;
	@FXML private TextField maxCostInput;
	@FXML private TextField minStudentSizeInput;
	@FXML private TextField maxStudentSizeInput;
	
	@FXML private RadioButton publicRadioButton;
	@FXML private RadioButton privateNonProfitRadioButton;
	@FXML private RadioButton privateForProfitRadioButton;
	private ToggleGroup collegeTypeToggleGroup;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
	}
	
	public void viewStudentProfile(ActionEvent event) {
		
	}
	
	public void logOut(ActionEvent event) {
		
	}
	
	public void searchColleges(ActionEvent event) {
		
	}
	
	public void viewSelectedCollege(ActionEvent event) {
		
	}
	
	public void addFavoriteCollege(ActionEvent event) {
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		collegeTypeToggleGroup = new ToggleGroup();
		this.publicRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateNonProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateForProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		
	}
}
