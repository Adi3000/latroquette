package net.latroquette.common.database.data.profile;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import net.latroquette.common.test.LatroquetteTest;
import net.latroquette.common.test.dbunit.ListDbUnitTestCase;
import net.latroquette.common.test.utils.TestUtils;
import net.latroquette.common.util.ServiceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class UsersTest extends ListDbUnitTestCase implements LatroquetteTest {

	@Inject
	private transient UsersService usersService;
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	private static List<String> DBU_FILES = Arrays.asList(
				 USER_DBU_RESOURCE
			);
	
	@Test
	public void testGetUserByLogin() {
		String login = TEST_USER_LOGIN;
		User user = usersService.getUserByLogin(login);
		assertNotNull(user);
		assertEquals(login, user.getLogin());
	}
	@Test
	public void testSendVerificationMail() throws ServiceException{
		String login = TEST_USER_LOGIN;
		User user = usersService.getUserByLogin(login);
		usersService.sendVerificationMail(user);
	}

	@Override
	protected List<String> getDataSetsPath() {
		return DBU_FILES;
	}
	

}
