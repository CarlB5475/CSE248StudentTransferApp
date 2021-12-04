package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
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

public class StudentProfileController implements Initializable {
	private ViewableStudent loggedStudent;
	private StudentProfileModel studentProfileModel = new StudentProfileModel(SQLiteConnection.getMainUrl());
	private ObservableList<ViewableCollege> favoriteColleges;
	
	@FXML private Label profileLabel;
	@FXML private MenuBar menuBar;
	@FXML private TextArea studentInfoTextArea;
	
	@FXML private TableView<ViewableCollege> collegeTable;
	@FXML private TableColumn<ViewableCollege, Integer> collegeIdColumn;
	@FXML private TableColumn<ViewableCollege, String> collegeNameColumn, collegeUrlColumn, collegeTypeColumn;
	
	@FXML private Button viewFavoriteCollegeButton, deleteFavoriteCollegeButton;
	
	public void setStudent(ViewableStudent student) {
		loggedStudent = student;
		String strProfileLabel = student.getFirstName() + " " + student.getLastName() + " Profile";
		String strStudentInfo = studentProfileModel.getStudentInfo(student);
		profileLabel.setText(strProfileLabel);
		studentInfoTextArea.setText(strStudentInfo);
		
		setFavoriteColleges();
	}
	
	public void goToCollegeSearch(ActionEvent event) throws IOException {
		ViewChanger.changeToCollegeSearchView(menuBar, loggedStudent);
	}
	
	public void logOut(ActionEvent event) throws IOException {
		ViewChanger.changeToLoginView(menuBar);
	}
	
	public void toggleDisableTableOptions(MouseEvent event) {
		ViewableCollege selectedCollege = collegeTable.getSelectionModel().getSelectedItem();
		boolean isSelectedCollege = selectedCollege != null;
		viewFavoriteCollegeButton.setDisable(!isSelectedCollege);
		deleteFavoriteCollegeButton.setDisable(!isSelectedCollege);
	}
	
	public void viewSelectedFavorite(ActionEvent event) throws IOException {
		ViewableCollege selectedCollege = collegeTable.getSelectionModel().getSelectedItem();
		ViewChanger.viewCollegeProfile(selectedCollege);
	}
	
	public void deleteSelectedFavorite(ActionEvent event) {
		ViewableCollege selectedCollege = collegeTable.getSelectionModel().getSelectedItem();
		boolean isSuccessful = studentProfileModel.deleteFavorite(loggedStudent, selectedCollege);
		if(!isSuccessful) 
			return;
		setFavoriteColleges();
		Stream.of(viewFavoriteCollegeButton, deleteFavoriteCollegeButton).forEach(button -> button.setDisable(true));
		
		Alert favoriteDeletedAlert = new Alert(AlertType.INFORMATION);
		favoriteDeletedAlert.setTitle("Favorite College Deleted Alert");
		favoriteDeletedAlert.setHeaderText("The college \"" + selectedCollege.getName() + "\" has been deleted!");
		favoriteDeletedAlert.setContentText("You can add this college as your favorite again by searching for it in college search.");
		favoriteDeletedAlert.showAndWait();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		collegeIdColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, Integer>("collegeId"));
		collegeNameColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("name"));
		collegeUrlColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("url"));
		collegeTypeColumn.setCellValueFactory(new PropertyValueFactory<ViewableCollege, String>("collegeType"));
		
		Stream.of(viewFavoriteCollegeButton, deleteFavoriteCollegeButton).forEach(button -> button.setDisable(true));
	}
	
	private void setFavoriteColleges() {
		favoriteColleges = FXCollections.observableArrayList();
		LinkedList<ViewableCollege> favoriteCollegeList = studentProfileModel.getFavorites(loggedStudent);
		favoriteColleges.addAll(favoriteCollegeList);
		collegeTable.setItems(favoriteColleges);
	}
}
