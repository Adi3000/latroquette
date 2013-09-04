package net.latroquette.common.database.data.profile;

import java.util.Arrays;
import java.util.List;

import net.latroquette.common.test.LatroquetteTest;
import net.latroquette.common.test.dbunit.ListDbUnitTestCase;

import org.junit.Test;

public class UsersTest extends ListDbUnitTestCase implements LatroquetteTest {

	private static List<String> DBU_FILES = Arrays.asList(
				 TEST_USER_DBU_RESOURCE
			);
	
	@Test
	public void testGetUserByLogin() {
		UsersService users = new UsersService();
		String login = TEST_USER_LOGIN;
		User user = users.getUserByLogin(login);
		users.close();
		assertNotNull(user);
		assertEquals(login, user.getLogin());
	}

	@Override
	protected List<String> getDataSetsPath() {
		return DBU_FILES;
	}
	

}
