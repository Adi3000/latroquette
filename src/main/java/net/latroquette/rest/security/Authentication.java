package net.latroquette.rest.security;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import net.latroquette.common.database.data.profile.User;

@Path("/authentication")
@WebService(name=Services.AUTHENTICATION_WEB_SERVICE)
public class Authentication extends SpringBeanAutowiringSupport{
	
	@Autowired
	private UsersService usersService;
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}


	@POST
	@Path("/ejabberd")
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticateForEjabberd(@QueryParam("login") String login, @QueryParam("password") String password) {
		User user = usersService.authenticateUser(login, password, AuthenticationMethod.EJABBER);
		return user  == null ? "0" : "1";
    }
	
	@GET
	@Path("/searchUser")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<List<User>> searchUser(@QueryParam("q") String pattern){
		List<User> usersFound = usersService.searchUsers(pattern);
		return new GenericEntity<List<User>>(usersFound) {};
	}
	@POST
	@Path("/isUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String isUser(@QueryParam("login") String login){
		User user = usersService.getUserByLogin(login);
		return user  == null ? "0" : "1";
	}
	
	
}
