package net.latroquette.common.test;

import com.adi3000.common.util.security.User;

public interface LatroquetteTest {
	
	public static final String TEST_USER_DBU_RESOURCE = "users.main-user";
	public static final User TEST_USER =  LatroquetteTestUtils.getMainUser();
	public static final String TEST_USER_LOGIN =  "TestUser1";
	

}
