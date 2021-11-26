package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

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
		signUpModel.addNewStudent("A", "B", user, "11720", "-45.0", "120.0", "Password1");
		student = loginModel.getLoggedStudent(user);
	}
	
	@BeforeEach
	void reset() {
		predicateStatements = new LinkedList<>();
		collegeList = new LinkedList<>();
	}
	
	@Test
	@Disabled("Successful test")
	void testCalculateDistance() {
		double collegeLatitude = 5, collegeLongitude = 5;
		double studentLatitude = 1, studentLongitude = 1;
		double distance = collegeSearchModel.calculateDistance(studentLatitude, collegeLatitude, studentLongitude, collegeLongitude);
		double actual = Math.sqrt(32);
		assertEquals(actual, distance, 0.000001);
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByZip() {
		String zipCode = "11794";
		String zipPredicate = "collegeZip LIKE '%" + zipCode + "%'";
		predicateStatements.add(zipPredicate);
		setCollegeList();
		if(collegeList.isEmpty())
			assertTrue(collegeList.isEmpty());
		
		collegeList.forEach(college -> {
			System.out.println(college);
			assertTrue(college.getCollegeZip().contains(zipCode));
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByAttendanceCost() {
		int attendanceCost = 25500;
		String maxCostPredicate = "attendanceCost <= " + attendanceCost;
		predicateStatements.add(maxCostPredicate);
		setCollegeList();
		if(collegeList.isEmpty())
			assertTrue(collegeList.isEmpty());
		
		collegeList.forEach(college -> {
			System.out.println(college.getName() + "; Attendance Cost = " + college.getAttendanceCost());
			assertTrue(college.getAttendanceCost() <= attendanceCost);
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByType() {
		String collegeType = "Public";
		String collegeTypePredicate = "collegeType = '" + collegeType + "'";
		predicateStatements.add(collegeTypePredicate);
		setCollegeList();
		if(collegeList.isEmpty())
			assertTrue(collegeList.isEmpty());
		
		collegeList.forEach(college -> {
			System.out.println(college.getName() + "; College Type = " + college.getCollegeType());
			assertTrue(college.getCollegeType().equals(collegeType));
		});
	}
	
	@Test
	@Disabled("Successful test")
	void testSearchCollegesByStudentSize() {
		int studentSize = 50000;
		String maxStudentSizePredicate = "studentSize = " + studentSize;
		predicateStatements.add(maxStudentSizePredicate);
		setCollegeList();
		if(collegeList.isEmpty())
			assertTrue(collegeList.isEmpty());
		
		collegeList.forEach(college -> {
			System.out.println(college.getName() + "; Student Size = " + college.getStudentSize());
			assertTrue(college.getStudentSize() <= studentSize);
		});
	}
	
	@Test
	@Disabled("Not set up")
	void testSearchCollegesByRadius() {
		
	}
	
	@Test
	@Disabled("Not set up")
	void testSearchCollegesMixed() {
		
	}
	
	private void setCollegeList() {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, DISABLED_RADIUS);
	}
	
	private void setCollegeList(int radius) {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, radius);
	}
}
