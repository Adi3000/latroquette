package net.latroquette.rest.security;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UserAdminInfo;
import net.latroquette.common.database.data.profile.UserInfo;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.adi3000.common.web.jsf.UtilsBean;

@Path("/authentication")
@WebService(name=Services.AUTHENTICATION_WEB_SERVICE)
public class Authentication extends SpringBeanAutowiringSupport{
	
	@Inject
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
		return user  == null || user.getXmppBlock() ? "0" : "1";
    }
	
	@POST
	@Path("/smf/register")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<UserAdminInfo> registerForSMF(@FormParam("login") String login, @FormParam("mail") String mail,  
			@FormParam("password") String password, @FormParam("loginState") Integer loginState) throws ServiceException {
		User user = new User();
		user.setLogin(login);
		user.setMail(mail);
		user.setPassword(password);
		usersService.registerNewUser(user, loginState, AuthenticationMethod.SMF);
		return new GenericEntity<UserAdminInfo>(new UserAdminInfo(user)) {};
	}
	@POST
	@Path("/smf/auth")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<UserAdminInfo> authenticateForSMF(@FormParam("login") String login, 
			@FormParam("password") String password, @FormParam("byToken") String byToken) {
		User user = usersService.authenticateUser(login, UtilsBean.urlDecode(password, true), Boolean.valueOf(byToken), AuthenticationMethod.SMF);
		return new GenericEntity<UserAdminInfo>(new UserAdminInfo(user)) {};
	}
	
	@GET
	@Path("/searchUser")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<List<User>> searchUser(@QueryParam("q") String pattern){
		List<User> usersFound = usersService.searchUsers(pattern);
		return new GenericEntity<List<User>>(usersFound) {};
	}
	@GET
	@Path("/userInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<UserInfo> getUserInfo(@QueryParam("login") String login){
		User userFound = usersService.getUserByLogin(login);
		return new GenericEntity<UserInfo>(new UserInfo(userFound)) {};
	}
	
	@POST
	@Path("/isUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String isUser(@QueryParam("login") String login){
		User user = usersService.getUserByLogin(login);
		return user  == null ? "0" : "1";
	}
	
	@POST
	@Path("/activation")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericEntity<UserAdminInfo> authenticateForSMF(@FormParam("login") String login) {
		User user = usersService.forceValidateUser(login, AuthenticationMethod.SMF);
		return new GenericEntity<UserAdminInfo>(new UserAdminInfo(user)) {};
	}
	
}
