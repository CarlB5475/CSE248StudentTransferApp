package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import utilities.*;

public class CollegeSearchController implements Initializable {
	private ViewableStudent loggedStudent;
	private CollegeSearchModel collegeSearchModel = new CollegeSearchModel(SQLiteConnection.getMainUrl());
	private ObservableList<ViewableCollege> viewableColleges;
	
	@FXML private MenuBar menuBar;
	
	@FXML private CheckBox zipCodeCheckBox, distanceCheckBox, attendanceCostCheckBox, collegeTypeCheckBox, studentSizeCheckBox;
	@FXML private Tab radiusTab, attendanceCostTab, collegeTypeTab, studentSizeTab;
	private boolean zipCodeDisabled, distanceDisabled, attendanceCostDisabled, collegeTypeDisabled, studentSizeDisabled;
	
	@FXML private TextField radiusInput, minCostInput, maxCostInput, minStudentSizeInput, maxStudentSizeInput;
	@FXML private RadioButton publicRadioButton, privateNonProfitRadioButton, privateForProfitRadioButton;
	private ToggleGroup collegeTypeToggleGroup;
	
	@FXML private TableView<ViewableCollege> collegeTable;
	@FXML private TableColumn<ViewableCollege, Integer> collegeIdColumn;
	@FXML private TableColumn<ViewableCollege, String> collegeNameColumn, collegeUrlColumn, collegeTypeColumn;
	
	@FXML private Button viewSelectedCollegeButton, addFavoriteCollegeButton;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
	}
	
	public void viewStudentProfile(ActionEvent event) {
		
	}
	
	public void logOut(ActionEvent event) throws IOException {
		loggedStudent = null;
		ViewChanger.changeToLoginView(menuBar);
	}
	
	public void selectSearchOptions(ActionEvent event) {
		zipCodeDisabled = !zipCodeCheckBox.isSelected();
		distanceDisabled = !distanceCheckBox.isSelected();
		attendanceCostDisabled = !attendanceCostCheckBox.isSelected();
		collegeTypeDisabled = !collegeTypeCheckBox.isSelected();
		studentSizeDisabled = !studentSizeCheckBox.isSelected();
		
		// sets the criteria to be enabled or disabled if any of the check boxes are selected
		toggleDisable(radiusTab, distanceDisabled);
		toggleDisable(attendanceCostTab, attendanceCostDisabled);
		toggleDisable(collegeTypeTab, collegeTypeDisabled);
		toggleDisable(studentSizeTab, studentSizeDisabled);
	}
	
	public void searchColleges(ActionEvent event) {
		
	}
	
	public void viewSelectedCollege(ActionEvent event) {
		
	}
	
	public void addFavoriteCollege(ActionEvent event) {
		
	}
	
	private void toggleDisable(Tab tab, boolean isDisabled) {
		tab.setDisable(isDisabled);
	}
	
	private ObservableList<ViewableCollege> getViewableColleges(LinkedList<String> predicateStatements, int radius) {
		ObservableList<ViewableCollege> colleges = FXCollections.observableArrayList();
		colleges.addAll(collegeSearchModel.searchColleges(predicateStatements, loggedStudent, radius));
		return colleges;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		collegeIdColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, Integer>("collegeId"));
		collegeNameColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("name"));
		collegeUrlColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("url"));
		collegeTypeColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("collegeType"));
		
		final LinkedList<String> INITIAL_STATEMENTS = new LinkedList<>(); // predicate statements not being used
		final int INITIAL_RADIUS = -1; // radius is not being used
		viewableColleges = getViewableColleges(INITIAL_STATEMENTS, INITIAL_RADIUS);
		collegeTable.setItems(viewableColleges);
		
		collegeTypeToggleGroup = new ToggleGroup();
		this.publicRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateNonProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateForProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		
		Stream.of(radiusTab, attendanceCostTab, collegeTypeTab, studentSizeTab).forEach(tab -> tab.setDisable(true));
		// sets every option to be disabled for now
		Stream.of(zipCodeDisabled, distanceDisabled, attendanceCostDisabled, collegeTypeDisabled, studentSizeDisabled).forEach(isDisabled -> isDisabled = true);
	}
}
