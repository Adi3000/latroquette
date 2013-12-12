package net.latroquette.common.database.data.profile;

import java.util.List;

import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.rest.forum.SMFRestException;
import net.latroquette.web.security.AuthenticationMethod;

import com.adi3000.common.database.hibernate.session.DAO;

public interface UsersService extends DAO<User>{

	/**
	 * Get an user by its login
	 * @param login
	 * @return
	 */
	public User getUserByLogin(String login);
	/**
	 * Get an user by its id
	 * @param id
	 * @return
	 */
	public User getUserById(Integer id);
	/**
	 * Authenticate an user with login and clear text password
	 * @param login
	 * @param password
	 * @param method
	 * @return
	 */
	public User authenticateUser(String login, String password, AuthenticationMethod method);
	
	/**
	 * Authenticate user with login and password or token (depending on {@code byToken})
	 * @param login
	 * @param passwordOrToken
	 * @param byToken
	 * @param method
	 * @return
	 */
	public User authenticateUser(String login, String passwordOrToken, Boolean byToken, AuthenticationMethod method);
	
	/**
	 * Search for user with a regexe {@code '*<pattern>*'}
	 * @param pattern
	 * @return
	 */
	public List<User> searchUsers(String pattern);
	
	/**
	 * Register a new user
	 * @param newUser
	 * @return
	 * @throws ServiceException
	 */
	public User registerNewUser(User newUser, Integer loginState, AuthenticationMethod method)  throws ServiceException;

	/**
	 * Register a new user here and everywhere (especially on forum)
	 * @param newUser
	 * @return
	 * @throws ServiceException
	 */
	public User registerNewUser(User newUser)  throws ServiceException;
	
	/**
	 * Modify user attribute and information
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * Prebind user to XMPP server to login with a {@link XMPPSession} session (with JID, RID and SID)
	 * @param user
	 * @param password
	 * @return
	 */
	public XMPPSession prebindXMPP(User user, String password);
	
	/**
	 * Register user into SimpleMachine Forum with a random password
	 * @param userId
	 * @param clearPassword
	 * @throws ServiceException
	 */
	public void smfRegisterNewUser(Integer userId) throws ServiceException;
	/**
	 * Register user into SimpleMachine Forum
	 * @param user
	 * @param clearPassword
	 * @throws ServiceException
	 */
	public void smfRegisterNewUser(User user, String clearPassword) throws SMFRestException;

	/**
	 * Force the validation of an user
	 * @param login
	 * @param method
	 * @return
	 */
	public User forceValidateUser(String login, AuthenticationMethod method);
	
	/**
	 * Validate user by mail token
	 * @param login
	 * @param method
	 * @return
	 */
	public User validateUser(String login, String token, AuthenticationMethod method);
	
	/**
	 * Return all {@link Role} declared
	 * @return
	 */
	public List<Role> getAllRoles();
	/**
	 * Modify or create a role
	 * @return
	 */
	public void modifyRole(Role role);
	
	/**
	 * Return statistic information of users matching filter<br />
	 * if {@code filter.nbDraft/filter.nbItems > 0} filter will match user with more than {@code [filter.nbDraft/filter.nbItems]}<br />
	 * if {@code filter.nbDraft/filter.nbItems < 0} filter will match user with less than {@code [filter.nbDraft/filter.nbItems]}<br />
	 * Will retrive only the {@link ParameterName}{@code .NB_RESULT_TO_LOAD} element of the {@code page}
	 * @param filter
	 * @param page
	 * @return
	 */
	public List<UserStatistics> getUserStatistics(UserStatistics filter, int page);
	/**
	 * Update roles from a list of {@link UserBase}
	 * @param list
	 */
	public void updateRoles(List<UserBase> list);
	
	/**
	 * Regenerate a mail and a token verification
	 * @param user
	 */
	public void sendResetPasswordMail(String mail);
	/**
	 * Regenerate a mail and a token verification
	 * @param user
	 */
	public void sendVerificationMail(User user);

	/**
	 * Validat mail token to allow password change
	 * @param login
	 * @param token
	 * @param method
	 * @return
	 */
	public User validateMailTokenForPassword(String login, String token, AuthenticationMethod method);
	
	public void changePassword(String password, User user);
	/**
	 * Unblock an user with specified id
	 * @param id
	 */
	public void unblockUser(Integer id);
	/**
	 * Block an user with specified id
	 * @param id
	 */
	public void blockUser(Integer id);
	/**
	 * Activate user on SMF
	 * @param user
	 * @throws ServiceException
	 */
	void smfActivateUser(User user) throws ServiceException;
	/**
	 * Activate user on SMF
	 * @param userId
	 * @throws ServiceException
	 */
	void smfActivateUser(Integer userId) throws ServiceException;
}
