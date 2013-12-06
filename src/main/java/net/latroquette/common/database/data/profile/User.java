/**
 * Account which will interact with {@link DreamedElement} to be owner and author
 */
package net.latroquette.common.database.data.profile;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.place.Place;

import org.hibernate.annotations.NaturalId;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.util.security.Security;




@Entity
@Table(name = "users")
@SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize=1)
public class User extends AbstractDataObject implements com.adi3000.common.util.security.User, UserBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1345564181714215141L;
	public static final String TABLE_AND_ENTITY_NAME = "USERS";
	
	private Integer id;
	private String login;
	private Timestamp lastDateLogin;
	private transient String currentToken;
	private Integer loginState;
	private String password;
	private Place place;
	private String mail;
	private String lastHostNameLogin;
	private String lastIpLogin;
	private Integer smfId;
	private Role role;
	
	@Id
	@Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
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
	@NaturalId
	@Column(name="user_login")
	public String getLogin() {
		return login;
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
	@XmlTransient
	public String getPassword() {
		return password;
	}
	/**
	 * @return the lastHostNameLogin
	 */
	@Column(name="user_last_host_name_login")
	@XmlTransient
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
	@XmlTransient
	public String getMail() {
		return mail;
	}
	/**
	 * @return the lastDateLogin
	 */
	@Column(name="user_last_date_login")
	@XmlTransient
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
	@XmlTransient
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
	
	public String toString(){
		return this.login + "@" + this.id;
	}
	@Override
	public void setCurrentToken() {
		this.currentToken = Security.generateTokenID(this);		
	}
	@Override
	@Transient
	@XmlTransient
	public String getCurrentToken() {
		return currentToken;
	}
	/**
	 * @return the smfId
	 */
	@Column(name="smf_id_member")
	public Integer getSmfId() {
		return smfId;
	}
	/**
	 * @param smfId the smfId to set
	 */
	public void setSmfId(Integer smfId) {
		this.smfId = smfId;
	}
	/**
	 * @return the loginState
	 */
	@Column(name="user_login_state")
	public Integer getLoginState() {
		return loginState;
	}
	/**
	 * @param loginState the loginState to set
	 */
	public void setLoginState(Integer loginState) {
		this.loginState = loginState;
	}
	/**
	 * @return the role
	 */
	@ManyToOne(optional=true)
	@JoinColumn(name="role_id")
	@XmlTransient
	public Role getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}
	/**
	 * @return the place
	 */
	@ManyToOne(optional=true)
	@JoinColumn(name="place_id")
	@XmlTransient
	public Place getPlace() {
		return place;
	}
	/**
	 * @param place the place to set
	 */
	public void setPlace(Place place) {
		this.place = place;
	}
	
}
