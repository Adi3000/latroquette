package net.latroquette.common.database.data.profile;


import net.latroquette.common.database.data.Repositories;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;

@Repository(value=Repositories.USERS_SERVICE)
public class UsersServiceImpl extends AbstractDAO<User> implements UsersService{
	
	@Transactional(readOnly=true)
	public User getUserByLogin(String login){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login)) ;
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
	
}
