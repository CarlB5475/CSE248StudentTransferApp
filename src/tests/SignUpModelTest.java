package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import models.*;

public class SignUpModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	static SignUpModel signUpModel = null;
	
	@BeforeAll
	static void setup() {
		signUpModel = new SignUpModel(TEST_URL);
		TestingUtilities.resetTestFile(signUpModel.getConnection(), TEST_URL);
		signUpModel.addNewStudent("A", "B", "AB", "11720", "-45.0", "120.0", "Password1");
	}
	
	@Test
	@Disabled("Successful test")
	void testIsProperName() {
		assertTrue(signUpModel.isProperName("C", "D"));
		assertFalse(signUpModel.isProperName("12343", "D"));
		assertFalse(signUpModel.isProperName("C", " "));
	}
	
	@Test
	@Disabled("Successful test")
	void testIsProperUsername() {
		assertTrue(signUpModel.isProperUsername("CD"));
		assertFalse(signUpModel.isProperUsername("C D"));
	}
	
	@Test
	@Disabled("Successful test")
	void testIsUniqueUsername() {
		assertTrue(signUpModel.isUniqueUsername("CD"));
		assertFalse(signUpModel.isUniqueUsername("AB"));
	}
	
	@Test
	@Disabled("Successful test")
	void testIsProperZip() {
		String[] zips = {"11720", "11720-1234", "123", "123456", "12345-12-123", "abc", "abc-def"};
		assertAll("zips", 
				() -> assertTrue(signUpModel.isProperZip(zips[0])),
				() -> assertTrue(signUpModel.isProperZip(zips[1])),
				() -> assertFalse(signUpModel.isProperZip(zips[2])),
				() -> assertFalse(signUpModel.isProperZip(zips[3])),
				() -> assertFalse(signUpModel.isProperZip(zips[4])),
				() -> assertFalse(signUpModel.isProperZip(zips[5])),
				() -> assertFalse(signUpModel.isProperZip(zips[6]))
				);
	}
	
	@Test
	@Disabled("Successful test")
	void testAreDoubleValues() {
		String[] strArr1 = {"123", "12.8"};
		String[] strArr2 = {"abc", "123", "def"};
		assertTrue(signUpModel.areValidDoubles(strArr1));
		assertFalse(signUpModel.areValidDoubles(strArr2));
	}
	
	@Test
	@Disabled("Successful test")
	void testIsProperLocation() {
		String[] latitudes = {"85.0", "-85.0", "95", "-95"};
		String[] longitudes = {"123", "-123", "190", "-190"};
		assertAll("locations",
				() -> assertTrue(signUpModel.isProperLocation(latitudes[0], longitudes[0])),
				() -> assertTrue(signUpModel.isProperLocation(latitudes[1], longitudes[1])),
				() -> assertFalse(signUpModel.isProperLocation(latitudes[2], longitudes[2])),
				() -> assertFalse(signUpModel.isProperLocation(latitudes[3], longitudes[3]))
				);
	}
	
	@Test
	@Disabled("Successful test")
	void testIsProperPassword() {
		String[] passwords = {"Password1", "pass", "Password", "password1", "PASSWORD1"};
		assertAll("passwords", 
				() -> assertTrue(signUpModel.isProperPassword(passwords[0])),
				() -> assertFalse(signUpModel.isProperPassword(passwords[1])),
				() -> assertFalse(signUpModel.isProperPassword(passwords[2])),
				() -> assertFalse(signUpModel.isProperPassword(passwords[3])),
				() -> assertFalse(signUpModel.isProperPassword(passwords[4]))
				);
	}
	
	@Test
	@Disabled("Successful test")
	void testHasMatchingPasswords() {
		assertTrue(signUpModel.hasMatchingPasswords("Password1", "Password1"));
		assertFalse(signUpModel.hasMatchingPasswords("Pass", "Password1"));
		assertFalse(signUpModel.hasMatchingPasswords("Password1", "Pass"));
	}
}
