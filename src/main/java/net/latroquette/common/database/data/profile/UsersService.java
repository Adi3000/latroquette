package net.latroquette.common.database.data.profile;

import net.latroquette.common.database.data.Repositories;

public interface UsersService extends Repositories<User>{

	
	public User getUserByLogin(String login);

	public void registerNewUser(User newUser);
	
	public void updateUser(User user);
}
