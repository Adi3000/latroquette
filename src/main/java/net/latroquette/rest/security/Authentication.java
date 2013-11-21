package net.latroquette.rest.security;

import javax.jws.WebService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.adi3000.common.util.security.User;

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
    public String getClichedMessage(@QueryParam("login") String login, @QueryParam("password") String password) {
		User user = usersService.authenticateUser(login, password, AuthenticationMethod.EJABBER);
		return user  == null ? "0" : "1";
    }
}
