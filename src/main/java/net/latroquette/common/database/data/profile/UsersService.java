package net.latroquette.common.database.data.profile;

import net.latroquette.web.security.AuthenticationMethod;

import com.adi3000.common.database.hibernate.session.DAO;

public interface UsersService extends DAO<User>{

	
	public User getUserByLogin(String login);
	
	public User authenticateUser(String login, String password, AuthenticationMethod method);

	public void registerNewUser(User newUser);
	
	public void updateUser(User user);
}
