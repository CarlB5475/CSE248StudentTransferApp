package tests;

import org.junit.jupiter.api.*;

import models.*;

public class LoginModelTest {
	static final String URL = "jdbc:sqlite:data/testDB.sqlite";
	static LoginModel loginModel = null;
	
	@BeforeAll
	static void setup() {
		SignUpModel signUpModel = new SignUpModel(URL);
		
		loginModel = new LoginModel(URL);
	}
	
	@Test
	void testIsLogin() {
		
	}
}
