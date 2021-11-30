package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;

import models.*;

public class CollegeSearchModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	
	static CollegeSearchModel collegeSearchModel = new CollegeSearchModel(TEST_URL);
	
	static LinkedList<String> predicateStatements = new LinkedList<>();
	static final int DISABLED_RADIUS = -1; // radius is not going to be used when = -1
	static ViewableStudent student = null;
	static LinkedList<ViewableCollege> collegeList = new LinkedList<>();
	
	@BeforeAll
	static void setup() {
		String user = "AB";
		TestingUtilities.resetTestFile(collegeSearchModel.getConnection(), TEST_URL);
		SignUpModel signUpModel = new SignUpModel(TEST_URL);
		LoginModel loginModel = new LoginModel(TEST_URL);
		signUpModel.addNewStudent("A", "B", user, "11720", "41.0", "-75.5", "Password1");
		student = loginModel.getLoggedStudent(user);
	}
	
	@BeforeEach
	void reset() {
		predicateStatements = new LinkedList<>();
		collegeList = new LinkedList<>();
	}
	
	@Test
	@Disabled("Successful test")
	void testAddFavorites() {
		String zipCode = "11794";
		String zipPredicate = collegeSearchModel.formZipPredicate(zipCode);
		predicateStatements.add(zipPredicate);
		setCollegeList(predicateStatements);
		
		boolean result = collegeSearchModel.addFavoriteToStudent(collegeList.getFirst(), student);
		assertTrue(result);
	}
	
	@Test
	@Disabled("Successful test")
	void testAddFavoritesMultiple() {
		String collegeType = "Public";
		String collegeTypePredicate = collegeSearchModel.formCollegeTypePredicate(collegeType);
		predicateStatements.add(collegeTypePredicate);
		setCollegeList(predicateStatements);
		
		final int MAX_FAVORITES = 10;
		ListIterator<ViewableCollege> collegeListIter = collegeList.listIterator();
		for(int i = 1; i <= MAX_FAVORITES; i++)
			collegeSearchModel.addFavoriteToStudent(collegeListIter.next(), student);
		
		
		boolean result = collegeSearchModel.addFavoriteToStudent(collegeListIter.next(), student);
		if(!result)
			System.out.println("There's no room to put this college as a favorite for this student.");
		assertFalse(result);
	}
	
	@Test
	@Disabled("Successful test")
	void testCalculateDistance() {
		double collegeLatitude = 5, collegeLongitude = 5;
		double studentLatitude = 1, studentLongitude = 1;
		double distance = collegeSearchModel.calculateDistance(studentLatitude, collegeLatitude, studentLongitude, collegeLongitude);
		double actual = Math.sqrt(123874.56);
		assertEquals(actual, distance, 0.000001);
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByZip() {
		String zipCode = "11794";
		String zipPredicate = collegeSearchModel.formZipPredicate(zipCode);
		predicateStatements.add(zipPredicate);
		setCollegeList(predicateStatements);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		collegeList.forEach(college -> {
			System.out.println(college);
			assertTrue(college.getCollegeZip().contains(zipCode));
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByAttendanceCost() {
		// Given: min and max cost are valid integers and has valid range
		int minCost = 1;
		int maxCost = 30000;
		String maxCostPredicate = collegeSearchModel.formCostPredicate(minCost, maxCost);
		predicateStatements.add(maxCostPredicate);
		setCollegeList(predicateStatements);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		for(ViewableCollege college : collegeList) {
			System.out.println(college.getName() + "; Attendance Cost = " + college.getAttendanceCost());
			int currentCost = college.getAttendanceCost();
			
			if(maxCost == -1) {
				assertTrue(currentCost >= minCost);
			} else {
				assertTrue(currentCost >= minCost && currentCost <= maxCost);
			}
		}
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByType() {
		String collegeType = "Public";
		String collegeTypePredicate = collegeSearchModel.formCollegeTypePredicate(collegeType);
		predicateStatements.add(collegeTypePredicate);
		setCollegeList(predicateStatements);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		collegeList.forEach(college -> {
			System.out.println(college.getName() + "; College Type = " + college.getCollegeType());
			assertTrue(college.getCollegeType().equals(collegeType));
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByStudentSize() {
		// Given: min and max are valid integers and has valid range
		int minStudentSize = 10000;
		int maxStudentSize = -1;
		String studentSizePredicate = collegeSearchModel.formStudentSizePredicate(minStudentSize, maxStudentSize);
		if(!studentSizePredicate.equals(""))
			predicateStatements.add(studentSizePredicate);
		setCollegeList(predicateStatements);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		for(ViewableCollege college : collegeList) {
			System.out.println(college.getName() + "; Student Size = " + college.getStudentSize());
			int currentStudentSize = college.getStudentSize();
			
			if(maxStudentSize == -1) {
				assertTrue(currentStudentSize >= minStudentSize);
			} else {
				assertTrue(currentStudentSize >= minStudentSize && currentStudentSize <= maxStudentSize);
			}
		}
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByRadius() {
		int radius = 100; // radius in miles 
		setCollegeList(predicateStatements, radius);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		collegeList.forEach(college -> {
			System.out.println(college.getName() + "; Latitude = " + college.getLatitude() + "; Longitude = " + college.getLongitude());
			double currentDistance = collegeSearchModel.calculateDistance(
					student.getLatitude(), college.getLatitude(), student.getLongitude(), college.getLongitude());
			assertTrue(currentDistance <= radius);
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesMixed() {
		int radius = 150;
		String collegeType = "Public";
		int minCost = 1;
		int maxCost = 20000;
		String collegeTypePredicate = collegeSearchModel.formCollegeTypePredicate(collegeType);
		String costPredicate = collegeSearchModel.formCostPredicate(minCost, maxCost);
		predicateStatements.addAll(Stream.of(collegeTypePredicate, costPredicate).toList());
		setCollegeList(predicateStatements, radius);
		if(collegeList.isEmpty()) {
			assertTrue(collegeList.isEmpty());
			System.out.println("College list is empty.");
			return;
		}
		
		collegeList.forEach(college -> {
			System.out.println(college);
			double currentDistance = collegeSearchModel.calculateDistance(
					student.getLatitude(), college.getLatitude(), student.getLongitude(), college.getLongitude());
			double currentCost = college.getAttendanceCost();
			assertTrue(currentDistance <= radius);
			assertTrue(college.getCollegeType().equals(collegeType));
			assertTrue(currentCost >= minCost && currentCost <= maxCost);
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesDefault() {
		setCollegeList(predicateStatements);
		assertTrue(!collegeList.isEmpty());
		collegeList.forEach(college -> System.out.println(college.getCollegeId() + ": " + college.getName()));
	}
	
	@Test
	@Disabled("Successful test")
	void testValidIntegerAndDouble() {
		String[] strIntegerValues = {"100", "ab", "213.5", "-12"};
		String[] strDoubleValues = {"123.1", "56", "cd", "-12.5"};
		
		assertAll("integer values", 
				() -> assertTrue(collegeSearchModel.isValidInteger(strIntegerValues[0])),
				() -> assertFalse(collegeSearchModel.isValidInteger(strIntegerValues[1])),
				() -> assertFalse(collegeSearchModel.isValidInteger(strIntegerValues[2])),
				() -> assertFalse(collegeSearchModel.isValidInteger(strIntegerValues[3]))
				);
		
		assertAll("double values", 
				() -> assertTrue(collegeSearchModel.isValidDouble(strDoubleValues[0])),
				() -> assertTrue(collegeSearchModel.isValidDouble(strDoubleValues[1])),
				() -> assertFalse(collegeSearchModel.isValidDouble(strDoubleValues[2])),
				() -> assertFalse(collegeSearchModel.isValidDouble(strDoubleValues[3]))
				);
	}
	
	@Test
	@Disabled("Successful test")
	void testValidRange() {
		int[] minValues = {0, 500, -5};
		int[] maxValues = {500, 0, -1, -5};
		
		assertAll("ranges", 
				() -> assertTrue(collegeSearchModel.isValidRange(minValues[0], maxValues[0])),
				() -> assertFalse(collegeSearchModel.isValidRange(minValues[1], maxValues[1])),
				() -> assertTrue(collegeSearchModel.isValidRange(minValues[1], maxValues[2])),
				() -> assertFalse(collegeSearchModel.isValidRange(minValues[2], maxValues[0])),
				() -> assertFalse(collegeSearchModel.isValidRange(minValues[0], maxValues[3])),
				() -> assertFalse(collegeSearchModel.isValidRange(minValues[2], maxValues[3]))
				);
	}
	
	private void setCollegeList(LinkedList<String> predicateStatements) {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, DISABLED_RADIUS);
	}
	
	private void setCollegeList(LinkedList<String> predicateStatements, int radius) {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, radius);
	}
}
