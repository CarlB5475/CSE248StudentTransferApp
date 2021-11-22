module Nanas_Student_Transfer_App {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive java.sql;
	requires transitive javafx.base;
	requires javafx.graphics;
	requires com.fasterxml.jackson.databind;
	requires org.junit.jupiter.api;
	
	exports controllers;
	opens controllers to javafx.fxml;
	
	exports models;
	exports utilities;
	exports tests;
	
	opens application to javafx.graphics, javafx.fxml;
}
