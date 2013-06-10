package net.latroquette.web.validators.profile;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;



@FacesValidator("net.latroquette.web.validators.profile.UserLoginValidator")
public class UserLoginValidator implements Validator {
		
	public void validate(FacesContext arg0, UIComponent arg1, Object value)	throws ValidatorException {
		UsersService userSearch = new UsersService();
		User user = userSearch.getUserByLogin(value.toString());
		userSearch.closeSession();
		if(user != null){
 			FacesMessage msg = new FacesMessage("User already exists","This user already exist");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
 		}
	}

}
