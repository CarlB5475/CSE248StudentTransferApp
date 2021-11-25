package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.*;

import models.*;

public class CollegeSearchModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	static CollegeSearchModel collegeSearchModel = null;
	
	@BeforeAll
	static void setup() {
		collegeSearchModel = new CollegeSearchModel(TEST_URL);
		TestingUtilities.resetTestFile(collegeSearchModel.getConnection(), TEST_URL);
	}
	
	@Test
	void testCalculateDistance() {
		double collegeLatitude = 5, collegeLongitude = 5;
		double studentLatitude = 1, studentLongitude = 1;
		double distance = collegeSearchModel.calculateDistance(studentLatitude, collegeLatitude, studentLongitude, collegeLongitude);
		double actual = Math.sqrt(32);
		assertEquals(actual, distance, 0.000001);
	}
	
	@Test
	void testSearchColleges() {
		double disabledRadius = -1; // radius is not going to be used
		String zipPredicate = "zip LIKE '%11794%'";
		LinkedList<String> predicateStatements = new LinkedList<>();
                predicateStatements.add(zipPredicate);
		LinkedList<ViewableCollege> collegeList = collegeSearchModel.searchColleges(null, null, disabledRadius);
	}
}
