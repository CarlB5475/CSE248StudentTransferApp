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
		int minCost = 20000;
		int maxCost = -1;
		String maxCostPredicate = collegeSearchModel.formCostPredicate(minCost, maxCost);
		if(!maxCostPredicate.equals(""))
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
			if(minCost == -1 && maxCost == -1) {
				assertTrue(minCost == -1 && maxCost == -1);
				System.out.println("Cost predicate is not used");
				break;
			}
			
			if(minCost == -1) {
				assertTrue(currentCost <= maxCost);
			} else if(maxCost == -1) {
				assertTrue(currentCost >= minCost);
			} else if(!maxCostPredicate.equals("")) {
				assertTrue(currentCost >= minCost && currentCost <= maxCost);
			} else {
				assertTrue(minCost >= maxCost);
				System.out.println("The minimum is greather than the maximum");
				break;
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
		int minStudentSize = 1;
		int maxStudentSize = 20000;
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
			if(minStudentSize == -1 && maxStudentSize == -1) {
				assertTrue(minStudentSize == -1 && maxStudentSize == -1);
				System.out.println("Student size predicate is not used");
				break;
			}
			
			if(minStudentSize == -1) {
				assertTrue(currentStudentSize <= maxStudentSize);
			} else if(maxStudentSize == -1) {
				assertTrue(currentStudentSize >= minStudentSize);
			} else if(!studentSizePredicate.equals("")) {
				assertTrue(currentStudentSize >= minStudentSize && currentStudentSize <= maxStudentSize);
			} else {
				assertTrue(minStudentSize >= maxStudentSize);
				System.out.println("The minimum is greather than the maximum");
				break;
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
		int attendanceCost = 20000;
		String collegeTypePredicate = collegeSearchModel.formCollegeTypePredicate(collegeType);
//		String maxCostPredicate = collegeSearchModel.formMaxCostPredicate(attendanceCost);
		predicateStatements.addAll(Stream.of(collegeTypePredicate).toList());
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
			assertTrue(currentDistance <= radius);
			assertTrue(college.getCollegeType().equals(collegeType));
		});
	}
	
	private void setCollegeList(LinkedList<String> predicateStatements) {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, DISABLED_RADIUS);
	}
	
	private void setCollegeList(LinkedList<String> predicateStatements, int radius) {
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, radius);
	}
}
