package net.latroquette.common.database.data.profile;


import java.util.List;

import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jivesoftware.smack.BOSHConfiguration;
import org.jivesoftware.smack.BOSHConnectionExtended;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.security.Security;

@Repository(value=Services.USERS_SERVICE)
public class UsersServiceImpl extends AbstractDAO<User> implements UsersService{
	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class.getName());
	
	
	@Transactional(readOnly=true)
	public User getUserByLogin(String login){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login).ignoreCase()) ;
		return (User)req.uniqueResult();
	}

	@Transactional(readOnly=false)
	public void registerNewUser(User newUser){
		newUser.setDatabaseOperation(DatabaseOperation.INSERT);
		persist(newUser);
	}
	
	@Transactional(readOnly=false)
	public void updateUser(User user){
		user.setDatabaseOperation(DatabaseOperation.UPDATE);
		persist(user);
	}

	@Override
	@Transactional(readOnly=true)
	public User authenticateUser(String login, String password,
			AuthenticationMethod method) {
		String encryptedPassword = Security.encryptedPassword(password);
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login).ignoreCase())
				.add(Restrictions.eq("password", encryptedPassword));
		return (User)req.uniqueResult();
	}
	
	public XMPPSession prebindXMPP(User user, String password){
		BOSHConfiguration boshConfiguration = new BOSHConfiguration(
				false,  "jabber.latroquette.net", 5280, "/http-bind", "jabber.latroquette.net");
		BOSHConnectionExtended boshConnection = new BOSHConnectionExtended(boshConfiguration);
		boshConfiguration.setDebuggerEnabled(logger.isDebugEnabled());
		try {
			boshConnection.connect();
			boshConnection.login(user.getLogin(), password, null);
		} catch (XMPPException e) {
			logger.error("Can't loggin to " +  "jabber.latroquette.net" + 5280 + " with login : " +user.getLogin() , e);
			return null;
		}
		
		return new XMPPSession(
				boshConnection.getUser(), 
				boshConnection.getConnectionID(), 
				boshConnection.getClientRid());
		
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<User> searchUsers(String pattern) {
		Criteria req = createCriteria(User.class)
				.add(Restrictions.like("login", "%"+pattern+"%").ignoreCase()) ;
		@SuppressWarnings("unchecked")
		List<User> result = req.list();
		return result;
	}
	
}
