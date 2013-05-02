package net.latroquette.common.security;

import java.net.SocketAddress;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Logger;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.optimizer.CommonValues;





/**
 * Utilities of static methods to provide encrypt and decrypt procedures.
 * @author adi
 *
 */
public class Security {
	
	private static final Logger LOGGER = Logger.getLogger(Security.class.getName());
	
	public static final int ERROR = CommonValues.ERROR_OR_INFINITE;  
	
	private static Set<Integer> ANONYMOUS_SESSION_ID = new TreeSet<Integer>();

	public static int generateSessionID(int clientHashCode,SocketAddress client) {
		// TODO Auto-generated method stub
		LOGGER.info("Anonymous UUID Session asked from " + client.toString());
		UUID uuid = UUID.randomUUID(); 
		int uuidHash = uuid.hashCode();
		LOGGER.finer("UUID computed : " + uuid);
		if (ANONYMOUS_SESSION_ID.add(uuidHash) )
		{
			return uuidHash;
		}
		return ERROR;
	}
	public static int generateSessionID(int clientHashCode,User user) {
		// TODO Auto-generated method stub
		LOGGER.info("User UUID Session asked from " + user.toString());
		return generateSessionID(clientHashCode);
	}
	private static int generateSessionID(int clientHashCode) {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID(); 
		int uuidHash = uuid.hashCode();
		LOGGER.finer("UUID computed : " + uuid);
		if (ANONYMOUS_SESSION_ID.add(uuidHash) )
		{
			return uuidHash;
		}
		return ERROR;
	}
	
	public static boolean isUserLogged(User user){
		return user != null && user != User.ANONYMOUS_USER && !User.ANONYMOUS_USER.equals(user);
	}
	
	public static void checkUserLogged(User user){
		if(!isUserLogged(user)){
			throw new IllegalArgumentException("User is not logged in ! ");
		}
	}

}
