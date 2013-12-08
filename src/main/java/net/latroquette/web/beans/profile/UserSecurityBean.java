package net.latroquette.web.beans.profile;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

import org.apache.commons.lang.StringUtils;

import com.adi3000.common.web.faces.FacesUtil;

@ManagedBean
@ViewScoped
public class UserSecurityBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5338239792415058369L;
	private static final String mail_purpose = "mail";
	private static final String password_purpose = "password";
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	
	private String mailToken;
	private boolean validated;
	private String login;
	private String purpose;
	private User user;
	@ManagedProperty("#{userBean}")
	private UserBean userBean;
	private String passwordConfirm;
	private String password;
	/**
	 * @return the mailToken
	 */
	public String getMailToken() {
		return mailToken;
	}
	/**
	 * @param mailToken the mailToken to set
	 */
	public void setMailToken(String mailToken) {
		this.mailToken = mailToken;
	}
	/**
	 * @return the validated
	 */
	public boolean isValidated() {
		return validated;
	}
	/**
	 * @param validated the validated to set
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	/**
	 * @return the userBean
	 */
	public UserBean getUserBean() {
		return userBean;
	}
	/**
	 * @param userBean the userBean to set
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	/**
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public void validate(){
		if(StringUtils.isEmpty(login) ||StringUtils.isEmpty(mailToken) || StringUtils.isEmpty(purpose)){
			FacesUtil.navigationForward("/error404");
		}
		if(isMailPurpose()){
			user = usersService.validateUser(login, mailToken, AuthenticationMethod.HTTP);
			validated = user != null;
			//Logout previous user if user is not the same
			if(validated && userBean.isLoggedIn() && !userBean.getId().equals(user.getId())){
				userBean.logoutUser();
			}else if (validated){
				//refresh user
				userBean.isActuallyLoggedIn();
			}
		}else if(isPasswordPurpose()){
			user = usersService.validateMailTokenForPassword(login, mailToken, AuthenticationMethod.HTTP);
			validated = user != null;
			userBean.logoutUser();
		}else{
			FacesUtil.navigationForward("/error404");
		}
	}
	
	public boolean isMailPurpose(){
		return mail_purpose.equals(purpose);
	}
	public boolean isPasswordPurpose(){
		return password_purpose.equals(purpose);
	}
	public String changePassword(){
		FacesContext fc = FacesContext.getCurrentInstance();
		if(StringUtils.isEmpty(passwordConfirm) || !passwordConfirm.equals(password)){
			FacesMessage msg = new FacesMessage("Probleme de mot de passe [302]", 
					"Le mot de passe est incorrect ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, msg);
			fc.validationFailed();
		}
		usersService.changePassword(getPassword(), user);
		return "/index";
	}
}
