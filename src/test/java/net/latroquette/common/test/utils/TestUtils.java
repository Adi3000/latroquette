package net.latroquette.common.test.utils;

import net.latroquette.common.database.data.profile.User;


public class TestUtils {
	public static final String SPRING_CONFIG = "/applicationContext.xml";
	public static final String LOGIN = "TestUser";
	public static final String PASSWORD = "Test1234";
	public static com.adi3000.common.util.security.User createTestUser(){
		User user = new net.latroquette.common.database.data.profile.User();
		user.setLogin(LOGIN);
		user.setPassword(PASSWORD);
		return user;
	}
}
