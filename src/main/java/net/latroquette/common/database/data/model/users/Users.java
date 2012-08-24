package net.latroquette.common.database.data.model.users;

import java.util.List;

import net.latroquette.common.database.session.DatabaseSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;



public class Users extends DatabaseSession{
	
	public User getUserByLogin(String login){
		Criteria req = this.session.createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login)) ;
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>)req.list();
		
		return users.isEmpty() ?  null : users.get(0);
	}

	
}
