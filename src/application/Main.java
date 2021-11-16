package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import models.StudentDB;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			restoreData();
			
			Parent root = FXMLLoader.load(getClass().getResource("/views/login_view.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/views/main_style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Student Transfer App");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void restoreData() {
		StudentDB.resetConnection();
	}
}
