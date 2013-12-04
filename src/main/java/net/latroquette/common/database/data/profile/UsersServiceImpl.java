package net.latroquette.common.database.data.profile;


import java.util.ArrayList;
import java.util.List;

import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;
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
import com.adi3000.common.util.optimizer.CommonValues;
import com.adi3000.common.util.security.Security;

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

	@Transactional(readOnly=true)
	public User getUserByLogin(String login){
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login).ignoreCase()) ;
		return (User)req.uniqueResult();
	}

	@TransactionalUpdate
	public User registerNewUser(User newUser)  throws ServiceException{
		String clearPassword = newUser.getPassword();
		//Encrypt Password
		newUser.setPassword(Security.encryptPassword(newUser.getPassword(), null));
		newUser.setLoginState(User.NOT_VALIDATED);
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
		String authenticationRestriction = null; 
		if(byToken){
			authenticationRestriction = "token";
		}else{
			authenticationRestriction = "password";
			password = Security.encryptPassword(password, null);
		}
		Criteria req = createCriteria(User.class)
				.setMaxResults(1)
				.add(Restrictions.eq("login", login).ignoreCase())
				.add(Restrictions.eq(authenticationRestriction, password));
		return (User)req.uniqueResult();
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

	public XMPPSession prebindXMPP(User user, String password){
		//TODO export values to parameters
		BOSHConfiguration boshConfiguration = new BOSHConfiguration(
				false,  "jabber.latroquette.net", 5280, "/http-bind", "jabber.latroquette.net");
		boshConfiguration.setReconnectionAllowed(false);
		BOSHConnectionExtended boshConnection = new BOSHConnectionExtended(boshConfiguration);
		boshConfiguration.setDebuggerEnabled(logger.isDebugEnabled());
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
	@Transactional(readOnly=false)
	public User validateUser(String login, AuthenticationMethod method) {
		User user = getUserByLogin(login);
		user.setLoginState(User.NEW_USER_VALIDATED);
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
	
	@SuppressWarnings("unchecked")
	public List<UserStatistics> getUserStatistics(UserStatistics filter, int page){
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ; 
		int cursor = CommonValues.ERROR_OR_INFINITE;
		nbResultToLoad = parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
		cursor = nbResultToLoad * (page -1);
		Criteria req = createCriteria(UserStatistics.class);
		if(filter.isIdSet()){
			req.add(Restrictions.idEq(filter.getId()));
		}
		if(StringUtils.isNotEmpty(filter.getMail())){
			req.add(Restrictions.like("mail",filter.getMail()).ignoreCase());
		}
		if(StringUtils.isNotEmpty(filter.getLogin())){
			req.add(Restrictions.like("login",filter.getLogin()).ignoreCase());
		}
		if(filter.getSmfId() != null){
			req.add(Restrictions.eq("smfId",filter.getSmfId()));
		}
		if(filter.getNbDraft() != null){
			if(filter.getNbDraft() > 0){
				req.add(Restrictions.ge("nbDraft", filter.getNbDraft()));
			}else if (filter.getNbDraft() < 0){
				req.add(Restrictions.le("nbDraft", -filter.getNbDraft().intValue()));
			}
		}
		if(filter.getNbItems() != null){
			if(filter.getNbItems() > 0){
				req.add(Restrictions.ge("nbItems", filter.getNbItems()));
			}else if (filter.getNbDraft() < 0){
				req.add(Restrictions.le("nbItems", -filter.getNbItems().intValue()));
			}
		}
		req.setFirstResult(cursor).setMaxResults(nbResultToLoad);
		return req.list();
	}
}
