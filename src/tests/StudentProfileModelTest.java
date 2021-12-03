package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.junit.jupiter.api.*;

import models.*;

public class StudentProfileModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	
	static StudentProfileModel studentProfileModel = new StudentProfileModel(TEST_URL);
	static CollegeSearchModel collegeSearchModel = null;
	
	static LinkedList<String> predicateStatements = new LinkedList<>();
	static final int DISABLED_RADIUS = -1; // radius is not going to be used when = -1
	static ViewableStudent student = null;
	static LinkedList<ViewableCollege> collegeList = new LinkedList<>();
	
	@BeforeAll
	static void setup() {
		String user = "AB";
		TestingUtilities.resetTestFile(studentProfileModel.getConnection(), TEST_URL);
		SignUpModel signUpModel = new SignUpModel(TEST_URL);
		LoginModel loginModel = new LoginModel(TEST_URL);
		collegeSearchModel = new CollegeSearchModel(TEST_URL);
		
		signUpModel.addNewStudent("A", "B", user, "11720", "41.0", "-75.5", "Password1");
		student = loginModel.getLoggedStudent(user);
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, DISABLED_RADIUS);
	}
	
	@Test
	@Disabled("Successful test")
	void testGetFavorites() {
		setAllFavorites();
		LinkedList<ViewableCollege> favoriteColleges = studentProfileModel.getFavorites(student);
		ListIterator<ViewableCollege> favoriteCollegesIter = favoriteColleges.listIterator();
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		
		while(favoriteCollegesIter.hasNext()) {
			ViewableCollege currentFavorite = favoriteCollegesIter.next();
			ViewableCollege currentCollege = collegeListIter.next();
			assertTrue(currentFavorite.equals(currentCollege));
		}
		assertTrue(favoriteColleges.size() == 10);
	}
	
	@Test
	void testGetFavoritesNotFull() {
		setPartialFavorites();
		LinkedList<ViewableCollege> favoriteColleges = studentProfileModel.getFavorites(student);
		ListIterator<ViewableCollege> favoriteCollegesIter = favoriteColleges.listIterator();
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		
		while(favoriteCollegesIter.hasNext()) {
			ViewableCollege currentFavorite = favoriteCollegesIter.next();
			ViewableCollege currentCollege = collegeListIter.next();
			assertTrue(currentFavorite.equals(currentCollege));
		}
		assertTrue(favoriteColleges.size() == 5);
	}
	
	private static void setAllFavorites() {
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		for(int i = 0; i < collegeSearchModel.getMaxFavorites(); i++)
			collegeSearchModel.addFavoriteToStudent(collegeListIter.next(), student);
	}
	
	private static void setPartialFavorites() {
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		for(int i = 0; i < collegeSearchModel.getMaxFavorites() / 2; i++)
			collegeSearchModel.addFavoriteToStudent(collegeListIter.next(), student);
	}
}
