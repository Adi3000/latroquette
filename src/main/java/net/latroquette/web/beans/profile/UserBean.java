package net.latroquette.web.beans.profile;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;

import net.latroquette.common.database.data.profile.Role;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.database.data.profile.XMPPSession;
import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;
import net.latroquette.web.security.SecurityUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adi3000.common.util.security.Security;
import com.adi3000.common.web.faces.FacesUtil;
import com.adi3000.common.web.jsf.UtilsBean;


@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(UserBean.class);

	public static final String LOGIN_VIEW_URI = "profile/login";
	public static final String PARAMETER_REQUEST_URI = "u";
	public static final String PARAMETER_QUERY_STRING = "qs";
	/**
	 * 
	 */
	private static final long serialVersionUID = 173422903879328102L;
	private transient String passwordConfirm;
	private transient String mailConfirm;
	private User user;
	private String password;
	private String previousURI;
	private String previousQueryString;
	private String displayLoginBox;
	private XMPPSession xmppSession;
	
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public UserBean(){
		user = new User();
		user.setLoginState(User.ANONYMOUS);
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
	 * @return the mailConfirm
	 */
	public String getMailConfirm() {
		return mailConfirm;
	}
	/**
	 * @param mailConfirm the mailConfirm to set
	 */
	public void setMailConfirm(String mailConfirm) {
		this.mailConfirm = mailConfirm;
	}

	/**
	 * @return the newUser
	 */
	public Integer getLoginState() {
		return user.getLoginState();
	}
	public void validateAddressMail(ComponentSystemEvent event){

		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		//get textbox1 value
		UIInput uiText1 = (UIInput)components.findComponent("addressMailField");
		

		//get textbox2 value
		UIInput uiText2 = (UIInput)components.findComponent("addressMailFieldConfirmation");
		if(uiText1 != null && uiText2 != null && uiText2.getLocalValue() != null && uiText1.getLocalValue() != null){
			String text1 = uiText1.getLocalValue().toString();
			String text2 = uiText2.getLocalValue().toString();
	
			if(!text1.equals(text2)){
	
				FacesMessage msg = new FacesMessage("Email check failed", 
						"Address mail do not match with previous given");
	
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	
				fc.addMessage(components.getClientId(), msg);
	
				//passed to the Render Response phase
				fc.renderResponse();
			}
		}
	}
	
	public void validatePassword(ComponentSystemEvent event){

		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		//get textbox1 value
		UIInput uiText1 = (UIInput)components.findComponent("passwordField");
		

		//get textbox2 value
		UIInput uiText2 = (UIInput)components.findComponent("passwordFieldConfirmation");
		if(uiText1 != null && uiText2 != null && uiText2.getLocalValue() != null && uiText1.getLocalValue() != null){
			String text2 = uiText2.getLocalValue().toString();
			String text1 = uiText1.getLocalValue().toString();
			if(!text1.equals(text2)){
	
				FacesMessage msg = new FacesMessage("Password check failed", 
						"Password do not match with previous given");
	
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	
				fc.addMessage(components.getClientId(), msg);
	
				//passed to the Render Response phase
				fc.renderResponse();
			}
		}
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
	public String registerUser()
	{
		User newUser = new User();
		newUser.setLoginState(User.ANONYMOUS);
		String forwardUrl = null;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		if(StringUtils.isEmpty(getMail()) || !getMail().equals(mailConfirm)){
			FacesMessage msg = new FacesMessage("Probleme d'email [301]", 
					"L'email est incorrect ou sa confirmation ne correspond pas");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, msg);
			fc.validationFailed();
		}
		if(StringUtils.isEmpty(passwordConfirm) || !passwordConfirm.equals(password)){
			FacesMessage msg = new FacesMessage("Probleme de mot de passe [302]", 
					"Le mot de passe est incorrect ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, msg);
			fc.validationFailed();
		}
		if(fc.isValidationFailed()){
			setDisplayLoginBox(true);
			return null;
		}
		newUser.setLogin(this.getLogin());
		newUser.setPassword(this.getPassword());
		newUser.setMail(getMail());
		this.setLogginUserInfo(newUser);
		//TODO catch the error when a user already exists
		try {
			usersService.registerNewUser(newUser);
		} catch (ServiceException e) {
			logger.error("Impossible to register " + newUser.getLogin() + " [ " + newUser.getMail() + " ]", e);
			return "/error500";
		}
		usersService.sendVerificationMail(newUser);
		forwardUrl = authenticateUser(newUser.getLogin(), this.getPassword());
		return forwardUrl;

	}
	
	public String loginUser()
	{
		return authenticateUser(this.getLogin(), this.getPassword());
	}
	
	/**
	 * Log an user
	 * @param login
	 * @param password
	 * @return
	 */
	private String authenticateUser(String login, String password){
		initPreviousURL();
		String forwardUrl = null;
		if(user == null){
			user = new User();
			user.setLoginState(User.ANONYMOUS);
		}
		user = usersService.authenticateUser(login, password, AuthenticationMethod.HTTP);
		if(user != null){
			this.setLogginUserInfo(user);
			SecurityUtil.setTokenCookie(user);
			usersService.updateUser(user);
		}else{
			user = new User();
			user.setLoginState(User.ANONYMOUS);
			this.setDisplayLoginBox(true);
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage("Utilisateur ou mot de passe incorrect");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, msg);
			fc.validationFailed();
		}
		//Is user blocked or not ?
		if(Security.checkLoginState(user)) {
			xmppSession = usersService.prebindXMPP(user, this.getPassword());
			if(!StringUtils.isEmpty(previousURI)){
				forwardUrl = previousURI;
				if(!StringUtils.isEmpty(previousQueryString)){
					forwardUrl = forwardUrl.concat("?").concat(UtilsBean.urlDecode(previousQueryString));
				}
			}else{
				forwardUrl = "/index";
			}
			forwardUrl = FacesUtil.prepareRedirect(forwardUrl);
			setDisplayLoginBox(true);
		}else{
			forwardUrl = logoutUser();;
		}
		return forwardUrl;
	}
	
	public String logoutUser()
	{
		//Unset properties of this user
		this.user = new User();
		this.mailConfirm = null;
		this.password = null;
		this.passwordConfirm = null;
		xmppSession = null;
		SecurityUtil.removeTokenCookie();
		user.setLoginState(User.ANONYMOUS);
		return "/index";
	}
	
	private void initPreviousURL(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String previousURI = params.get(PARAMETER_REQUEST_URI);
		if(!StringUtils.isEmpty(previousURI) && !previousURI.contains(LOGIN_VIEW_URI)){
			this.previousURI = params.get(PARAMETER_REQUEST_URI);
			this.previousQueryString = params.get(PARAMETER_QUERY_STRING);
		}
	}
	
	private void setLogginUserInfo(User user){
		Timestamp now = new Timestamp(new java.util.Date().getTime());
		ExternalContext externe = FacesContext.getCurrentInstance().getExternalContext();
		String ip = ((HttpServletRequest)externe.getRequest()).getRemoteAddr();
		user.setLastDateLogin(now);
		user.setLastIpLogin(ip);
	}
	
	
	
	public boolean isActuallyLoggedIn()
	{
		if(Security.isUserLogged(user)){
			user = usersService.getUserById(user.getId());
		}
		return isLoggedIn();
	}
	
	public boolean isLoggedIn(){
		return (Security.isUserLogged(user) && user.getLoginState() != null && user.getLoginState() >= User.NOT_VALIDATED) ;
	}
	
	public void checkUserLogged(){
		SecurityUtil.checkUserLogged(this);
	}
	
	public Role getRole(){
		return user.getRole();
	}
	
	public boolean isAdmin(){
		return getRole() != null ? user.getRole().getAdmin() : false;
	}
	public boolean isValidateItems(){
		return getRole() != null ? user.getRole().getValidateItems() : false;
	}
	public boolean isModifyKeywords(){
		return getRole() != null ? user.getRole().getModifyKeywords() : false;
	}

	public Integer getId() {
		return  user != null ? user.getId() : null;
	}

	public String getLogin() {
		return user != null ?  user.getLogin() : null;
	}
	
	public void setLogin(String login) {
		user.setLogin(login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return user.getMail();
	}
	public void setMail(String mail) {
		user.setMail(mail);
	}

	public Timestamp getLastDateLogin() {
		return user.getLastDateLogin();
	}

	public String getLastIpLogin() {
		return user.getLastIpLogin();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the previousQueryString
	 */
	public String getPreviousQueryString() {
		return previousQueryString;
	}

	/**
	 * @param previousQueryString the previousQueryString to set
	 */
	public void setPreviousQueryString(String previousQueryString) {
		this.previousQueryString = previousQueryString;
	}

	public XMPPSession getXmppSession() {
		return xmppSession;
	}

	/**
	 * @return the previousURI
	 */
	public String getPreviousURI() {
		return previousURI;
	}

	/**
	 * @param previousURI the previousURI to set
	 */
	public void setPreviousURI(String previousURI) {
		this.previousURI = previousURI;
	}
	
	/**
	 * @return the displayLoginBox
	 */
	public String getDisplayLoginBox() {
		return displayLoginBox;
	}

	/**
	 * @param displayLoginBox the displayLoginBox to set
	 */
	public void setDisplayLoginBox(boolean displayLoginBox) {
		if(displayLoginBox){
			this.displayLoginBox = "true";
		}else{
			this.displayLoginBox = "";
		}
	}
	
	public void loadLogin(){
		String register = FacesUtil.getStringParameter("register");
		setDisplayLoginBox(Boolean.valueOf(register));
	}

	public String getToken() {
		if(Security.isUserLogged(user)){
			return user.getToken();
		}
		return null;
	}
	
	public String revalidate(){
		usersService.sendVerificationMail(user);
		//TODO display a confirm message
		return "/index";
	}
	public String resetPassword(){
		usersService.sendResetPasswordMail(mailConfirm);
		//TODO display a confirm message
		return "/index";
	}

	public boolean isValidated(){
		return isLoggedIn() && user.getLoginState() > User.NOT_VALIDATED;
	}
}
