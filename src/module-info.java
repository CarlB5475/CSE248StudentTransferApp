module Nanas_Student_Transfer_App {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive java.sql;
	requires transitive javafx.base;
	requires javafx.graphics;
	
	exports controllers;
	opens controllers to javafx.fxml;
	
	exports models;
	exports utilities;
	
	opens application to javafx.graphics, javafx.fxml;
}
