/**
 * Account which will interact with {@link DreamedElement} to be owner and author
 */
package net.latroquette.common.database.data.profile;


import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.item.wish.WishedItem;
import net.latroquette.common.database.data.place.Place;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;




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
	private String token;
	private Integer loginState;
	private String password;
	private String salt;
	private Place place;
	private String mail;
	private Role role;
	private String lastIpLogin;
	private String lastIpXmpp;
	private Boolean xmppBlock;
	private Timestamp lastDateXmpp;
	private Integer smfId;
	private Set<WishedItem> wishesList;
	
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
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	@Column(name="user_token")
	@XmlTransient
	public String getToken() {
		return token;
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
	/**
	 * @return the salt
	 */
	@Column(name="user_salt")
	@XmlTransient
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * @return the lastIpXmpp
	 */
	@Column(name="user_last_ip_xmpp")
	@XmlTransient
	public String getLastIpXmpp() {
		return lastIpXmpp;
	}
	/**
	 * @param lastIpXmpp the lastIpXmpp to set
	 */
	public void setLastIpXmpp(String lastIpXmpp) {
		this.lastIpXmpp = lastIpXmpp;
	}
	/**
	 * @return the xmppBlock
	 */
	@Column(name="is_xmpp")
	@XmlTransient
	@Type(type="yes_no")
	public Boolean getXmppBlock() {
		return xmppBlock;
	}
	/**
	 * @param xmppBlock the xmppBlock to set
	 */
	public void setXmppBlock(Boolean xmppBlock) {
		this.xmppBlock = xmppBlock;
	}
	/**
	 * @return the lastDateXmpp
	 */
	@Column(name="user_last_date_xmpp")
	@XmlTransient
	public Timestamp getLastDateXmpp() {
		return lastDateXmpp;
	}
	/**
	 * @param lastDateXmpp the lastDateXmpp to set
	 */
	public void setLastDateXmpp(Timestamp lastDateXmpp) {
		this.lastDateXmpp = lastDateXmpp;
	}
	/**
	 * @return the wishesList
	 */
	@ManyToMany(cascade = {CascadeType.ALL},fetch=FetchType.EAGER)
	@JoinTable(name="users_wishes", 
		joinColumns={@JoinColumn(name="user_id")}, 
		inverseJoinColumns={@JoinColumn(name="wish_id")})
	public Set<WishedItem> getWishesList() {
		return wishesList;
	}
	/**
	 * @param wishesList the wishesList to set
	 */
	public void setWishesList(Set<WishedItem> wishesList) {
		this.wishesList = wishesList;
	}
	
}
