package net.latroquette.common.database.data.profile;

import java.util.List;

import net.latroquette.web.security.AuthenticationMethod;

import com.adi3000.common.database.hibernate.session.DAO;

public interface UsersService extends DAO<User>{

	
	public User getUserByLogin(String login);
	
	public User authenticateUser(String login, String password, AuthenticationMethod method);
	
	public List<User> searchUsers(String pattern);

	public void registerNewUser(User newUser);
	
	public void updateUser(User user);
	
	public XMPPSession prebindXMPP(User user, String password);
}
