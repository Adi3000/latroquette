package net.latroquette.web.validators.profile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;



@ManagedBean
@RequestScoped
public class UserLoginValidator implements Validator {
	
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public void validate(FacesContext arg0, UIComponent arg1, Object value)	throws ValidatorException {
		User user = usersService.getUserByLogin(value.toString());
		if(user != null){
 			FacesMessage msg = new FacesMessage("User already exists","This user already exist");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
 		}
	}

}
