/**
 * Account which will interact with {@link DreamedElement} to be owner and author
 */
package net.latroquette.common.database.data.profile;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.latroquette.common.database.data.AbstractDataObject;



@Entity
@Table(name = "users")
public class User extends AbstractDataObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1345564181714215141L;
	public static final User ANONYMOUS_USER = new User();
	public static final String TABLE_AND_ENTITY_NAME = "USERS";
	
	private Integer id;
	private String login;
	private Integer token;
	private Timestamp lastDateLogin;
	private String password;
	private String mail;
	private String lastHostNameLogin;
	private String lastIpLogin;
	
	@Id
	@Column(name="user_id")
	public Integer getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the login
	 */
	@Column(name="user_login")
	public String getLogin() {
		return login;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(Integer token) {
		this.token = token;
	}
	/**
	 * @return the token
	 */
	@Column(name="user_token")
	public Integer getToken() {
		return token;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the password
	 */
	@Column(name="user_password")
	public String getPassword() {
		return password;
	}
	/**
	 * @return the lastHostNameLogin
	 */
	@Column(name="user_last_host_name_login")
	public String getLastHostNameLogin() {
		return lastHostNameLogin;
	}
	/**
	 * @param lastHostNameLogin the lastHostNameLogin to set
	 */
	public void setLastHostNameLogin(String lastHostNameLogin) {
		this.lastHostNameLogin = lastHostNameLogin;
	}
	/**
	 * @return the mail
	 */
	@Column(name="user_mail")
	public String getMail() {
		return mail;
	}
	/**
	 * @return the lastDateLogin
	 */
	@Column(name="user_last_date_login")
	public Timestamp getLastDateLogin() {
		return lastDateLogin;
	}
	/**
	 * @param lastDateLogin the lastDateLogin to set
	 */
	public void setLastDateLogin(Timestamp lastDateLogin) {
		this.lastDateLogin = lastDateLogin;
	}
	/**
	 * @return the lastIpLogin
	 */
	@Column(name="user_last_ip_login")
	public String getLastIpLogin() {
		return lastIpLogin;
	}
	/**
	 * @param lastIpLogin the lastIpLogin to set
	 */
	public void setLastIpLogin(String lastIpLogin) {
		this.lastIpLogin = lastIpLogin;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
}
