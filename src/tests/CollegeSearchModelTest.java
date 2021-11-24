package tests;

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
}
