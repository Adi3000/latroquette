package net.latroquette.common.database.data.profile;

import java.util.Arrays;
import java.util.List;

import net.latroquette.common.test.LatroquetteTest;
import net.latroquette.common.test.dbunit.ListDbUnitTestCase;
import net.latroquette.common.test.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class UsersTest extends ListDbUnitTestCase implements LatroquetteTest {

	@Autowired
	private UsersService usersService;
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	private static List<String> DBU_FILES = Arrays.asList(
				 TEST_USER_DBU_RESOURCE
			);
	
	@Test
	public void testGetUserByLogin() {
		String login = TEST_USER_LOGIN;
		User user = usersService.getUserByLogin(login);
		assertNotNull(user);
		assertEquals(login, user.getLogin());
	}

	@Override
	protected List<String> getDataSetsPath() {
		return DBU_FILES;
	}
	

}
