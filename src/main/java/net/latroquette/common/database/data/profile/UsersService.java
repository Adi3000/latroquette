package net.latroquette.common.database.data.profile;


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;


public class UsersService extends AbstractDAO<User>{
	
	public User getUserByLogin(String login){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login)) ;
		return (User)req.uniqueResult();
	}

	public boolean registerNewUser(User newUser){
		newUser.setDatabaseOperation(DatabaseOperation.INSERT);
		persist(newUser);
		return sendForCommit();
	}
	
	public boolean updateUser(User user){
		user.setDatabaseOperation(DatabaseOperation.UPDATE);
		persist(user);
		return sendForCommit();
	}
	
}
