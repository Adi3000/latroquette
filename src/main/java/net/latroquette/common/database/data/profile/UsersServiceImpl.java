package net.latroquette.common.database.data.profile;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;
import net.latroquette.mail.MailBody;
import net.latroquette.rest.forum.SMFMethods;
import net.latroquette.rest.forum.SMFRestException;
import net.latroquette.rest.forum.SMFWSClientUtil;
import net.latroquette.web.security.AuthenticationMethod;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.jivesoftware.smack.BOSHConfiguration;
import org.jivesoftware.smack.BOSHConnectionExtended;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.database.spring.TransactionalUpdate;
import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.util.optimizer.CommonValues;
import com.adi3000.common.util.security.Security;
import com.adi3000.common.web.jsf.UtilsBean;

@Repository(value=Services.USERS_SERVICE)
public class UsersServiceImpl extends AbstractDAO<User> implements UsersService{
	
	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	@Autowired
	private transient Parameters parameters;
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	@Autowired
	private RolesDAO rolesDAO;
	/**
	 * @param rolesDAO the rolesDAO to set
	 */
	public void setRolesDAO(RolesDAO rolesDAO) {
		this.rolesDAO = rolesDAO;
	}

	private User getUserByMail(String mail){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("mail", mail).ignoreCase()) ;
		return (User)req.uniqueResult();
	}
	@Transactional(readOnly=true)
	public User getUserByLogin(String login){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login).ignoreCase()) ;
		return (User)req.uniqueResult();
	}
	@Transactional(readOnly=true)
	public User getUserById(Integer id){
		return (User)getSession().get(User.class, id);
	}

	@TransactionalUpdate
	public User registerNewUser(User newUser)  throws ServiceException{
		String clearPassword = newUser.getPassword();
		//Encrypt Password
		Security.encryptPassword(newUser);
		newUser.setLoginState(User.NOT_VALIDATED);
		newUser.setXmppBlock(false);
		newUser.setDatabaseOperation(DatabaseOperation.INSERT);
		persist(newUser);
		smfRegisterNewUser(newUser, clearPassword);
		newUser.setDatabaseOperation(DatabaseOperation.UPDATE);
		persist(newUser);
		return newUser;
	}
	
	@TransactionalUpdate
	public void updateUser(User user){
		user.setDatabaseOperation(DatabaseOperation.UPDATE);
		persist(user);
	}

	@Override
	@Transactional(readOnly=true)
	public User authenticateUser(String login, String password, Boolean byToken,
			AuthenticationMethod method) {
		//TODO centralize blocked user with throwing specific error on unauthorized user
		User user = getUserByLogin(login);
		if(user != null){
			if(byToken){
				if(StringUtils.isNotEmpty(password) && StringUtils.equals(password, user.getToken())){
					return user;
				}
			}else{
				if(Security.validatePassword(password, user)){
					//Create SMF account if not exist
					//TODO : on SMF password change, change also here and vice-versa
					//May be irrelevant in the future : all user must have an account after register
					if(user.getSmfId() == null && Security.checkLoginState(user)){
						try{
							smfRegisterNewUser(user, password);
						}catch(ServiceException e){
							logger.error("Can't create account for " + user + " on SMF ");
						}
					}
					return user;
				}
			}
		}
		return null; 
	}
	
	@Override
	@Transactional(readOnly=true)
	public User authenticateUser(String login, String password, AuthenticationMethod method){
		return authenticateUser(login, password, false, method);
	}
	
	public void smfRegisterNewUser(User user, String clearPassword) throws ServiceException{
		//Register to SMF only if user is allowed in Latroquette
		if(!Security.checkLoginState(user)){
			throw new IllegalStateException("User "+user+" is not allowed on this site"); 
		}
		
		JSONObject regOptions = new JSONObject();
		try{
			regOptions.put("member_name", user.getLogin());
			regOptions.put("email", user.getMail());
			regOptions.put("password", clearPassword);
			regOptions.put("member_ip", user.getLastIpLogin());
			if(User.NOT_VALIDATED.equals(user.getLoginState())){
				regOptions.put("require", "activation");
			}
		}catch(JSONException e ){
			logger.error("Can't parse JSON for " + user );
			throw new ServiceException("Can't parse JSON for " + user , e);
		}
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("regOptions", regOptions.toString()));
		
		JSONObject jsonResult = null;
		try {
			jsonResult = SMFWSClientUtil.sendRequestPostQuery(SMFMethods.SMF_REGISTER_ENDPOINT, postParams);
		} catch (SMFRestException e) {
			logger.error("Error with SMF Rest Webservice", e);
			throw new ServiceException(e);
		}
		
		try {
			user.setSmfId(Integer.valueOf(jsonResult.getInt("data")));
		} catch (JSONException e) {
			logger.error(jsonResult.toString() + "\n : Impossible to extract SMFId from Json result ", e);
			throw new IllegalArgumentException(jsonResult.toString() + "\n : Impossible to extract SMFId from Json result ", e);
		}
	}
	@Override
	@Transactional(readOnly=true)
	public void smfActivateUser(Integer userId) throws ServiceException{
		User user = getUserById(userId);
		smfActivateUser(user);
	}
	@Override
	public void smfActivateUser(User user) throws ServiceException{
		if(user.getSmfId() == null){
			throw new IllegalStateException("Can't activate this user "+ user +", not registerd to SMF yet");
		}
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("member", user.getSmfId().toString()));
		JSONObject jsonResult = null;
		try {
			jsonResult = SMFWSClientUtil.sendRequestPostQuery(SMFMethods.SMF_ACTIVATE_ENDPOINT, postParams);
		} catch (SMFRestException e) {
			logger.error("Error with SMF Rest Webservice", e);
			throw new ServiceException("Error with SMF Rest Webservice", e);
		}
		try {
			if(!jsonResult.getBoolean("data")){
				logger.warn("Can't activate user "+user + "["+user.getSmfId()+"] successfully on SMF");
				throw new ServiceException("Can't activate user "+user + "["+user.getSmfId()+"] successfully on SMF");
			}
		} catch (JSONException e) {
			logger.error("Can't parse result from SMF Webservice"+jsonResult +" for user + " + user + " ["+user.getSmfId()+"] successfully", e);
			throw new ServiceException("Can't parse result from SMF Webservice"+jsonResult +" for user + " + user + " ["+user.getSmfId()+"] successfully", e);
		}
	}
	private void smfChangePasswordUser(User user, String clearPassword) throws ServiceException{
		if(user.getSmfId() == null){
			throw new IllegalStateException("Can't change password of user "+ user +", not registerd to SMF yet");
		}
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("member", user.getSmfId().toString()));
		postParams.add(new BasicNameValuePair("password", clearPassword));
		JSONObject jsonResult = null;
		try {
			jsonResult = SMFWSClientUtil.sendRequestPostQuery(SMFMethods.SMF_PASSWORD_ENDPOINT, postParams);
		} catch (SMFRestException e) {
			logger.error("Error with SMF Rest Webservice", e);
			throw new ServiceException("Error with SMF Rest Webservice", e);
		}
		try {
			if(!jsonResult.getBoolean("data")){
				logger.warn("Can't change password of user "+user + "["+user.getSmfId()+"] successfully on SMF");
				throw new ServiceException("Can't change password of user "+user + "["+user.getSmfId()+"] successfully on SMF");
			}
		} catch (JSONException e) {
			logger.error("Can't parse result from SMF Webservice"+jsonResult +" for user + " + user + " ["+user.getSmfId()+"] successfully", e);
			throw new ServiceException("Can't parse result from SMF Webservice"+jsonResult +" for user + " + user + " ["+user.getSmfId()+"] successfully", e);
		}
	}

	public XMPPSession prebindXMPP(User user, String password){
		if(user.getLoginState() <= User.NOT_VALIDATED){
			return null;
		}
		//TODO export values to parameters
		BOSHConfiguration boshConfiguration = new BOSHConfiguration(
				false,  "jabber.latroquette.net", 5280, "/http-bind", "jabber.latroquette.net");
		boshConfiguration.setReconnectionAllowed(false);
		BOSHConnectionExtended boshConnection = new BOSHConnectionExtended(boshConfiguration);
		boshConfiguration.setDebuggerEnabled(logger.isTraceEnabled());
		try {
			boshConnection.connect();
			boshConnection.login(user.getLogin(), password, null);
		} catch (XMPPException e) {
			logger.error("Can't loggin to " +  "jabber.latroquette.net" + 5280 + " with login : " +user.getLogin() , e);
			return null;
		}
		String jid = boshConnection.getUser();
		String sid = boshConnection.getSessionID(); 
		boshConnection.detach();
		XMPPSession xmppSession = new XMPPSession(
										jid,
										sid,
										boshConnection.getClientNextRid());
		return xmppSession;
		
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<User> searchUsers(String pattern) {		
		Criteria req = createCriteria(User.class)
				.add(Restrictions.like("login", "%"+pattern+"%").ignoreCase()) ;
		@SuppressWarnings("unchecked")
		List<User> result = req.list();
		return result;
	}

	@Override
	@TransactionalUpdate
	public User forceValidateUser(String login, AuthenticationMethod method) {
		User user = getUserByLogin(login);
		return forceValidateUser(user, method);
	}
	private  User forceValidateUser(User user, AuthenticationMethod method) {
		if(!Security.checkLoginState(user)){
			logger.warn("User "+user +" is blocked, should not have access to mail verification");
			throw new IllegalAccessError("User "+user +" is blocked, should not have access to mail verification");
		}
		user.setLoginState(User.NEW_USER_VALIDATED);
		user.setDatabaseOperation(DatabaseOperation.UPDATE);
		updateUser(user);
		setMailToken(null,user);
		try {
			smfActivateUser(user);
		} catch (ServiceException e) {
			logger.warn("Can't activate user "+user + "["+user.getSmfId()+"] successfully",e);
		}
		return user;
	}
	
	@TransactionalUpdate
	public void modifyRole(Role role){
		rolesDAO.modify(role);
	}
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Role> getAllRoles(){
		return (List<Role>)rolesDAO.createCriteria(Role.class).list();
	}
	
	@Transactional(readOnly=true)
	public List<UserStatistics> getUserStatistics(UserStatistics filter, int page){
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ;
		nbResultToLoad = parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
		
		Criteria req = createCriteria(UserStatistics.class);
		if(filter.isIdSet()){
			req.add(Restrictions.idEq(filter.getId()));
		}
		if(StringUtils.isNotEmpty(filter.getMail())){
			req.add(Restrictions.like("mail","%"+filter.getMail()+"%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(filter.getLogin())){
			req.add(Restrictions.like("login","%"+filter.getLogin()+"%").ignoreCase());
		}
		if(filter.getSmfId() != null){
			req.add(Restrictions.eq("smfId",filter.getSmfId()));
		}
		if(filter.getNbDrafts() != null){
			if(filter.getNbDrafts() > 0){
				req.add(Restrictions.ge("nbDraft", filter.getNbDrafts()));
			}else if (filter.getNbDrafts() < 0){
				req.add(Restrictions.le("nbDraft", -filter.getNbDrafts().intValue()));
			}
		}
		if(filter.getNbItems() != null){
			if(filter.getNbItems() > 0){
				req.add(Restrictions.ge("nbItems", filter.getNbItems()));
			}else if (filter.getNbItems() < 0){
				req.add(Restrictions.le("nbItems", -filter.getNbItems().intValue()));
			}
		}
		req = CommonUtil.setCriteriaPage(req, page, nbResultToLoad);
		@SuppressWarnings("unchecked")
		List<UserStatistics> list = (List<UserStatistics>) req.list();
		return list;
	}
	
	@TransactionalUpdate
	public void updateRoles(List<UserBase> list){
		for(UserBase userBase: list){
			User user = getUserById(userBase.getId());
			user.setRole(userBase.getRole());
			updateUser(user);
		}
	}

	@Override
	@TransactionalUpdate
	public User validateMailTokenForPassword(String login, String token, AuthenticationMethod method) {
		return checkMailToken(login, token, method);
	}
	@Override
	@TransactionalUpdate
	public User validateUser(String login, String token, AuthenticationMethod method) {
		User user = checkMailToken(login, token, method);
		if(user != null){
			forceValidateUser(user, method);
		}
		return user;
	}
	
	private User checkMailToken(String login, String token, AuthenticationMethod method) {
		//TODO add this in parameters
		String delay = "2 days";
		String sql = "SELECT {users.*} FROM users {users} "
				+ "where user_verification_token = :token "
				+ "and user_date_verification_token > current_timestamp - interval '"+ delay +"' "
				+ "limit 1";
		Query req = createSQLQuery(sql)
					.addEntity("users",User.class)
					.setString("token", UtilsBean.urlDecode(token, true));
		return (User)req.uniqueResult();
		
	}
	private void setMailToken(String token,User user){
		String sql = "UPDATE users "
				+ "SET user_verification_token = :token, "
				+ "user_date_verification_token = current_timestamp "
				+ "where user_id = :userId ";
		Query req = createSQLQuery(sql)
				.setString("token",	token)
				.setInteger("userId", user.getId());
		req.executeUpdate();
	}
	@TransactionalUpdate
	public void sendResetPasswordMail(String userMail){
		User user = getUserByMail(userMail);
		String mail = "noreply-latroquette@latroquette.net";
		String name = "La Troquette.net";
		String smtp = "smtp.free.fr";
		String subject = "Reinitialisation du mot de passe de %s";
		String body = MailBody.HTML_RESET_PASSWORD_MAIL;
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", smtp);
		Session session = Session.getDefaultInstance(properties);
		String token = Security.generateTokenID(user);
		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail(), name));
			message.setSubject(String.format(subject,user.getLogin()));
			message.setContent(String.format(body, user.getLogin(), UtilsBean.urlEncode(token)), "text/html; charset=utf-8");
			message.setSentDate(new Date());
			setMailToken(token, user);
			Transport.send(message);
		}catch(MessagingException | UnsupportedEncodingException me){
			logger.error("Unable to send reset password mail to " + user.getMail() , me );
		}
	}
	@TransactionalUpdate
	public void sendVerificationMail(User user){
		if(!Security.checkLoginState(user)){
			logger.warn("User "+user +" is blocked, should not have access to mail verification");
			throw new IllegalAccessError("User "+user +" is blocked, should not have access to mail verification");
		}
		//TODO externalize these values to parameters
		String mail = "noreply-latroquette@latroquette.net";
		String name = "La Troquette.net";
		String smtp = "smtp.free.fr";
		String subject = "Vérification de l'adresse email de %s";
		String body = MailBody.HTML_VALIDATION_MAIL;
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", smtp);
		Session session = Session.getDefaultInstance(properties);
		String token = Security.generateTokenID(user);
		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail(), name));
			message.setSubject(String.format(subject,user.getLogin()));
			message.setContent(String.format(body, user.getLogin(), UtilsBean.urlEncode(token)), "text/html; charset=utf-8");
			message.setSentDate(new Date());
			setMailToken(token, user);
			Transport.send(message);
		}catch(MessagingException | UnsupportedEncodingException me){
			logger.error("Unable to send verification mail to " + user.getMail() , me );
		}
	}

	@Override
	@TransactionalUpdate
	public void changePassword(String password, User user) {
		user.setPassword(password);
		user.setSalt(null);
		Security.encryptPassword(user);
		//TODO SMF change password
		updateUser(user);
		try {
			smfChangePasswordUser(user, password);
		} catch (ServiceException e) {
			logger.warn("Can't change password user "+user + "["+user.getSmfId()+"] successfully",e);
		}
		setMailToken(null, user);
	}

	@Override
	@TransactionalUpdate
	public void unblockUser(Integer id){
		User user = getUserById(id);
		user.setLoginState(User.NOT_VALIDATED);
		updateUser(user);
	}
	
	@Override
	@TransactionalUpdate
	public void blockUser(Integer id){
		User user = getUserById(id);
		user.setLoginState(User.BLOCKED);
		updateUser(user);
	}
	
}
