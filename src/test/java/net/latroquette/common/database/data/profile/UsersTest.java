package net.latroquette.common.database.data.profile;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsersTest {

	@Test
	public void testGetUserByLogin() {
		String login = "adi3000";
		UsersService users = new UsersService();
		User user = users.getUserByLogin(login);
		users.close();
		assertNotNull(user);
		assertEquals(login, user.getLogin());
	}

}
