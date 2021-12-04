package tests;

import static org.junit.jupiter.api.Assertions.*;

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
	
	@BeforeEach
	void reset() {
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
	@Disabled("Successful test")
	void testGetStudentInfo() {
		String strStudentInfo = studentProfileModel.getStudentInfo(student);
		System.out.println(strStudentInfo);
		assertTrue(!strStudentInfo.equals(""));
	}
	
	@Test
	@Disabled("Successful test")
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
	
	@Test
	@Disabled("Successful test")
	void testDeleteFavorite() {
		setAllFavorites();
		LinkedList<ViewableCollege> favoriteColleges = studentProfileModel.getFavorites(student);
		ListIterator<ViewableCollege> favoriteCollegesIter = favoriteColleges.listIterator();
		ViewableCollege collegeToDelete = favoriteCollegesIter.next();
		favoriteCollegesIter.next();
		favoriteCollegesIter.next();
		ViewableCollege collegeToDelete2 = favoriteCollegesIter.next();
		
		boolean result1 = studentProfileModel.deleteFavorite(student, collegeToDelete);
		displayResult(result1, collegeToDelete);
		assertTrue(result1);
		
		boolean result2 = studentProfileModel.deleteFavorite(student, collegeToDelete);
		displayResult(result2, collegeToDelete);
		assertFalse(result2);
		
		boolean result3 = studentProfileModel.deleteFavorite(student, collegeToDelete2);
		displayResult(result3, collegeToDelete2);
		assertTrue(result3);
		
		boolean result4 = studentProfileModel.deleteFavorite(student, collegeToDelete2);
		displayResult(result4, collegeToDelete2);
		assertFalse(result4);
	}
	
	@Test
	@Disabled("Successful test")
	void testDeleteAndAddFavorites() {
		setAllFavorites();
		LinkedList<ViewableCollege> favoriteColleges = studentProfileModel.getFavorites(student);
		ListIterator<ViewableCollege> favoriteCollegesIter = favoriteColleges.listIterator();
		ViewableCollege collegeToDelete = favoriteCollegesIter.next();
		favoriteCollegesIter.next();
		favoriteCollegesIter.next();
		ViewableCollege collegeToDelete2 = favoriteCollegesIter.next();
		
		studentProfileModel.deleteFavorite(student, collegeToDelete);
		studentProfileModel.deleteFavorite(student, collegeToDelete2);
		
		boolean result = collegeSearchModel.addFavoriteToStudent(collegeToDelete2, student);
		assertTrue(result);
		boolean result2 = collegeSearchModel.addFavoriteToStudent(collegeToDelete, student);
		assertTrue(result2);
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
	
	private static void displayResult(boolean result, ViewableCollege college) {
		if(result)
			System.out.println("The college \"" + college.getName() + "\" has been deleted.");
		else
			System.out.println("The college \"" + college.getName() + "\" is not in favorites.");
	}
}
