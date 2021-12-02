package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.*;

import models.*;

public class CollegeProfileModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	
	static CollegeProfileModel collegeProfileModel = new CollegeProfileModel(TEST_URL);
	
	static LinkedList<String> predicateStatements = new LinkedList<>();
	static final int DISABLED_RADIUS = -1;
	static ViewableStudent student = null;
	static LinkedList<ViewableCollege> collegeList;
	
	@BeforeAll
	static void setup() {
		TestingUtilities.resetTestFile(collegeProfileModel.getConnection(), TEST_URL);
		CollegeSearchModel collegeSearchModel = new CollegeSearchModel(TEST_URL);
		String zipCode = "11794";
		String zipPredicate = collegeSearchModel.formZipPredicate(zipCode);
		predicateStatements.add(zipPredicate);
		collegeList = collegeSearchModel.searchColleges(predicateStatements, student, DISABLED_RADIUS);
	}
	
	@Test
	@Disabled("Successful test")
	void testGetCollegeInfo() {
		ViewableCollege currentCollege = collegeList.getFirst();
		String strCollegeInfo = collegeProfileModel.getCollegeInfo(currentCollege);
		System.out.println(strCollegeInfo);
		assertTrue(!strCollegeInfo.equals(""));
	}
}
