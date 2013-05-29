package net.latroquette.common.database.data.profile;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDAO;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


public class Users extends AbstractDAO<User>{
	
	public User getUserByLogin(String login){
		Criteria req = this.session.createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login)) ;
		return (User)req.uniqueResult();
	}

	public boolean registerNewUser(User newUser){
		newUser.setDatabaseOperation(IDatabaseConstants.INSERT);
		persist(newUser);
		return commit();
	}
	
	public boolean updateUser(User user){
		user.setDatabaseOperation(IDatabaseConstants.UPDATE);
		persist(user);
		return commit();
	}
	
}
