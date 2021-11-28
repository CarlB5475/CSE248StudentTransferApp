package controllers;

import java.net.URL;
import java.util.*;

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
	
	@FXML private TableView<ViewableCollege> collegeTable;
	@FXML private TableColumn<ViewableCollege, Integer> collegeIdColumn;
	@FXML private TableColumn<ViewableCollege, String> collegeNameColumn;
	@FXML private TableColumn<ViewableCollege, String> collegeUrlColumn;
	@FXML private TableColumn<ViewableCollege, String> collegeTypeColumn;
	
	@FXML private Button viewSelectedCollegeButton;
	@FXML private Button addFavoriteCollegeButton;
	
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
		collegeIdColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, Integer>("collegeId"));
		collegeNameColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("name"));
		collegeUrlColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("url"));
		collegeTypeColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("collegeType"));
		
		final LinkedList<String> INITIAL_STATEMENTS = new LinkedList<>();
		final int INITIAL_RADIUS = -1; // radius is not being used
		viewableColleges = getViewableColleges(INITIAL_STATEMENTS, INITIAL_RADIUS);
		collegeTable.setItems(viewableColleges);
		
		collegeTypeToggleGroup = new ToggleGroup();
		this.publicRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateNonProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateForProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		
	}
	
	private ObservableList<ViewableCollege> getViewableColleges(LinkedList<String> predicateStatements, int radius) {
		ObservableList<ViewableCollege> colleges = FXCollections.observableArrayList();
		colleges.addAll(collegeSearchModel.searchColleges(predicateStatements, loggedStudent, radius));
		return colleges;
	}
}
