package net.latroquette.common.database.data.profile;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsersTest {

	@Test
	public void testGetUserByLogin() {
		String login = "adi3000";
		Users users = new Users();
		User user = users.getUserByLogin(login);
		users.closeSession();
		assertNotNull(user);
		assertEquals(login, user.getLogin());
	}

}
