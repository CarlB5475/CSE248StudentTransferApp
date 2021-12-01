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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.*;
import utilities.*;

public class CollegeSearchController implements Initializable {
	private ViewableStudent loggedStudent;
	private CollegeSearchModel collegeSearchModel = new CollegeSearchModel(SQLiteConnection.getMainUrl());
	private ObservableList<ViewableCollege> viewableColleges;
	
	@FXML private MenuBar menuBar;
	
	@FXML private CheckBox zipCodeCheckBox, distanceCheckBox, attendanceCostCheckBox, collegeTypeCheckBox, studentSizeCheckBox;
	private boolean zipCodeDisabled = true, distanceDisabled = true, attendanceCostDisabled = true, collegeTypeDisabled = true, studentSizeDisabled = true;
	@FXML private Tab radiusTab, attendanceCostTab, collegeTypeTab, studentSizeTab;
	
	@FXML private TextField radiusInput, minCostInput, maxCostInput, minStudentSizeInput, maxStudentSizeInput;
	@FXML private CheckBox disableMaxCostCheckBox, disableMaxStudentSizeCheckBox;
	private boolean maxCostDisabled, maxStudentSizeDisabled;
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
		
		// sets the criteria to be enabled if any of the check boxes are selected
		toggleDisable(radiusTab, distanceDisabled);
		toggleDisable(attendanceCostTab, attendanceCostDisabled);
		toggleDisable(collegeTypeTab, collegeTypeDisabled);
		toggleDisable(studentSizeTab, studentSizeDisabled);
	}
	
	public void disableMax(ActionEvent event) {
		maxCostDisabled = disableMaxCostCheckBox.isSelected();
		maxStudentSizeDisabled = disableMaxStudentSizeCheckBox.isSelected();
		
		maxCostInput.setDisable(maxCostDisabled);
		maxStudentSizeInput.setDisable(maxStudentSizeDisabled);
		
		maxCostInput.setText("");
		maxStudentSizeInput.setText("");
	}
	
	public void searchColleges(ActionEvent event) {
		LinkedList<String> predicateStatements = new LinkedList<>();
		double radius = -1; // radius is not used when = -1
		
		if(!addPredicateStatements(predicateStatements))
			return;
		
		if(!distanceDisabled) {
			String strRadius = radiusInput.getText();
			if(!collegeSearchModel.isValidDouble(strRadius)) {
				Alert radiusAlert = new Alert(AlertType.ERROR);
				radiusAlert.setTitle("Invalid Distance Alert");
				radiusAlert.setHeaderText("The radius should be a valid double value!");
				radiusAlert.setContentText("Enter a valid radius!");
				radiusAlert.showAndWait();
				return;
			}
			radius = Double.parseDouble(strRadius);
		}
		
		viewableColleges = getViewableColleges(predicateStatements, radius);
		collegeTable.setItems(viewableColleges);
		
		Alert searchAlert = new Alert(AlertType.INFORMATION);
		searchAlert.setTitle("Search Alert");
		searchAlert.setHeaderText("The search has been successful!");
		searchAlert.setContentText("Look through the results in the colleges tab.");
		searchAlert.showAndWait();
	}
	
	public void viewSelectedCollege(ActionEvent event) throws IOException {
		ViewableCollege selectedCollege = collegeTable.getSelectionModel().getSelectedItem();
		ViewChanger.viewCollegeProfile(selectedCollege);
	}
	
	public void addFavoriteCollege(ActionEvent event) {
		ViewableCollege selectedCollege = collegeTable.getSelectionModel().getSelectedItem();
		if(!collegeSearchModel.addFavoriteToStudent(selectedCollege, loggedStudent)) {
			Alert fullFavoritesAlert = new Alert(AlertType.ERROR);
			fullFavoritesAlert.setTitle("Full Favorites Alert");
			fullFavoritesAlert.setHeaderText("You have the maximum amount of favorites you can have!");
			fullFavoritesAlert.setContentText("Delete some favorite colleges to make room for more!");
			fullFavoritesAlert.showAndWait();
		}
	}
	
	public void toggleDisableTableOptions(MouseEvent event) {
		viewSelectedCollegeButton.setDisable(collegeTable.getSelectionModel().getSelectedItem() == null);
		addFavoriteCollegeButton.setDisable(collegeTable.getSelectionModel().getSelectedItem() == null);
	}
	
	// return true if adding the predicate statements is successful, else return false
	private boolean addPredicateStatements(LinkedList<String> predicateStatements) {
		if(!zipCodeDisabled) {
			String zipPredicate = collegeSearchModel.formZipPredicate(loggedStudent.getZip());
			predicateStatements.add(zipPredicate);
		}
		
		if(!attendanceCostDisabled) {
			if(!addAttendanceCostPredicate(predicateStatements))
				return false;
		}
		
		if(!collegeTypeDisabled) {
			String collegeType = "";
			if(publicRadioButton.isSelected())
				collegeType = publicRadioButton.getText();
			else if(privateNonProfitRadioButton.isSelected())
				collegeType = privateNonProfitRadioButton.getText();
			else if(privateForProfitRadioButton.isSelected())
				collegeType = privateForProfitRadioButton.getText();
			
			String collegeTypePredicate = collegeSearchModel.formCollegeTypePredicate(collegeType);
			predicateStatements.add(collegeTypePredicate);
		}
		
		if(!studentSizeDisabled) {
			if(!addStudentSizePredicate(predicateStatements))
				return false;
		}

		return true;
	}
	
	// return true if adding the attendance cost predicate is successful, else return false
	private boolean addAttendanceCostPredicate(LinkedList<String> predicateStatements) {
		Alert attendanceCostAlert = new Alert(AlertType.ERROR);
		attendanceCostAlert.setTitle("Attendance Cost Alert");
		attendanceCostAlert.setContentText("Enter a valid attendance cost range here!");
		
		String strMinCost = minCostInput.getText();
		String strMaxCost = maxCostInput.getText();
		int minCost = -1;
		int maxCost = -1;
					
		if(!collegeSearchModel.isValidInteger(strMinCost)) {
			attendanceCostAlert.setHeaderText("The minimum cost must be an integer!");
			attendanceCostAlert.showAndWait();
			return false;
		}
					
		minCost = Integer.parseInt(strMinCost);
		if(!maxCostDisabled) {
			if(!collegeSearchModel.isValidInteger(strMaxCost)) {
				attendanceCostAlert.setHeaderText("The maximum cost must be an integer!");
				attendanceCostAlert.showAndWait();
				return false;
			}
			maxCost = Integer.parseInt(strMaxCost);
						
			if(!collegeSearchModel.isValidRange(minCost, maxCost)) {
				attendanceCostAlert.setHeaderText("The range is invalid! Minumum must be less than or equal to maximum!");
				attendanceCostAlert.showAndWait();
				return false;
			}
		}
					
		String attendanceCostPredicate = collegeSearchModel.formCostPredicate(minCost, maxCost);
		predicateStatements.add(attendanceCostPredicate);
		return true;
	}
	
	// return true if adding the student size predicate is successful, else return false
	private boolean addStudentSizePredicate(LinkedList<String> predicateStatements) {
		Alert studentSizeAlert = new Alert(AlertType.ERROR);
		studentSizeAlert.setTitle("Student Size Alert");
		studentSizeAlert.setContentText("Enter a valid student size range here!");
		
		String strMinStudentSize = minStudentSizeInput.getText();
		String strMaxStudentSize = maxStudentSizeInput.getText();
		int minStudentSize = -1;
		int maxStudentSize = -1;
					
		if(!collegeSearchModel.isValidInteger(strMinStudentSize)) {
			studentSizeAlert.setHeaderText("The minimum student size must be an integer!");
			studentSizeAlert.showAndWait();
			return false;
		}
					
		minStudentSize = Integer.parseInt(strMinStudentSize);
		if(!maxCostDisabled) {
			if(!collegeSearchModel.isValidInteger(strMaxStudentSize)) {
				studentSizeAlert.setHeaderText("The maximum student size must be an integer!");
				studentSizeAlert.showAndWait();
				return false;
			}
			maxStudentSize = Integer.parseInt(strMaxStudentSize);
						
			if(!collegeSearchModel.isValidRange(minStudentSize, maxStudentSize)) {
				studentSizeAlert.setHeaderText("The range is invalid! Minumum must be less than or equal to maximum!");
				studentSizeAlert.showAndWait();
				return false;
			}
		}
					
		String studentSizePredicate = collegeSearchModel.formStudentSizePredicate(minStudentSize, maxStudentSize);
		predicateStatements.add(studentSizePredicate);
		return true;
	}
	
	private void toggleDisable(Tab tab, boolean isDisabled) {
		tab.setDisable(isDisabled);
	}
	
	private ObservableList<ViewableCollege> getViewableColleges(LinkedList<String> predicateStatements, double radius) {
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
		final double INITIAL_RADIUS = -1; // radius is not being used
		viewableColleges = getViewableColleges(INITIAL_STATEMENTS, INITIAL_RADIUS);
		collegeTable.setItems(viewableColleges);
		
		collegeTypeToggleGroup = new ToggleGroup();
		this.publicRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateNonProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		this.privateForProfitRadioButton.setToggleGroup(collegeTypeToggleGroup);
		
		Stream.of(radiusTab, attendanceCostTab, collegeTypeTab, studentSizeTab).forEach(tab -> tab.setDisable(true));
		Stream.of(viewSelectedCollegeButton, addFavoriteCollegeButton).forEach(button -> button.setDisable(true));
	}
}
