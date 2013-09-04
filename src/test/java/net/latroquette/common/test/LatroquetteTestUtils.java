package net.latroquette.common.test;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;

public class LatroquetteTestUtils {

	static User getMainUser(){
		UsersService users = new UsersService();
		String login = LatroquetteTest.TEST_USER_LOGIN;
		User user = users.getUserByLogin(login);
		users.close();
		return user;
	}
}
