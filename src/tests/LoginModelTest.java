package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import models.*;

public class LoginModelTest {
	static final String TEST_URL = TestingUtilities.getTestUrl();
	static LoginModel loginModel = null;
	
	@BeforeAll
	static void setup() {
		SignUpModel signUpModel = new SignUpModel(TEST_URL);
		TestingUtilities.resetTestFile(signUpModel.getConnection(), TEST_URL);
		signUpModel.addNewStudent("A", "B", "AB", "11720", "-45.0", "120.0", "Password1");
		loginModel = new LoginModel(TEST_URL);
	}
	
	@Test
	void testIsLogin() {
		String[] users = {"AB", "hi"};
		String[] passwords = {"Password1", "hi"};
				
		assertAll("logins",
				() -> assertTrue(loginModel.isLogin(users[0], passwords[0])),
				() -> assertFalse(loginModel.isLogin(users[0], passwords[1])),
				() -> assertFalse(loginModel.isLogin(users[1], passwords[0])),
				() -> assertFalse(loginModel.isLogin(users[1], passwords[1]))
				);
	}
	
	@Test
	void testGetLoggedStudent() {
		ViewableStudent s1 = new ViewableStudent(1, "A", "B", "AB", "Password1", "11720", -45.0, 120.0, 1);
		ViewableStudent s2 = loginModel.getLoggedStudent("AB");
		assertEquals(s1, s2);
	}
}
