package net.latroquette.common.database.data.profile;

import java.util.List;

import net.latroquette.common.util.ServiceException;
import net.latroquette.web.security.AuthenticationMethod;

import com.adi3000.common.database.hibernate.session.DAO;

public interface UsersService extends DAO<User>{

	
	public User getUserByLogin(String login);
	
	public User authenticateUser(String login, String password, AuthenticationMethod method);
	
	public User authenticateUser(String login, String passwordOrToken, Boolean byToken, AuthenticationMethod method);
	
	public List<User> searchUsers(String pattern);

	public User registerNewUser(User newUser)  throws ServiceException;
	
	public void updateUser(User user);
	
	public void smfRegisterNewUser(User user, String clearPassword) throws ServiceException;

	public User validateUser(String login, AuthenticationMethod method);
}
