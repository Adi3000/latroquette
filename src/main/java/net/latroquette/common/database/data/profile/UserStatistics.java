package net.latroquette.common.database.data.profile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Subselect;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Subselect("select * from user_statistics")
public class UserStatistics extends AbstractDataObject implements UserBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436576772177823092L;

	private Integer nbItems;
	private Integer nbDrafts;
	private Integer loginState;
	private Date lastItemUpdate;
	private Integer smfId;
	private Boolean isSmfActivated;
	private Integer id;
	private String mail;
	private String login;
	private Role role;
	private Integer roleId;
	/**
	 * @return the roleId
	 */
	@Column(name="role_id", insertable=false, updatable=false)
	public Integer getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the login
	 */
	@Column(name="user_login")
	public String getLogin() {
		return login;
	}
	/**
	 * @return the mail
	 */
	@Column(name="user_mail")
	public String getMail() {
		return mail;
	}
	/**
	 * @return the isSmfActivated
	 */
	@Column(name="smf_is_activated")
	public Boolean getIsSmfActivated() {
		return isSmfActivated;
	}
	/**
	 * @return the lastItemUpdate
	 */
	@Column(name="last_item_update")
	public Date getLastItemUpdate() {
		return lastItemUpdate;
	}
	/**
	 * @return the nbDrafts
	 */
	@Column(name="nb_drafts")
	public Integer getNbDrafts() {
		return nbDrafts;
	}
	/**
	 * @return the nbItems
	 */
	@Column(name="nb_items")
	public Integer getNbItems() {
		return nbItems;
	}
	/**
	 * @return the smfId
	 */
	@Column(name="smf_id_member")
	public Integer getSmfId() {
		return smfId;
	}
	/**
	 * @param isSmfActivated the isSmfActivated to set
	 */
	public void setIsSmfActivated(Boolean isSmfActivated) {
		this.isSmfActivated = isSmfActivated;
	}
	/**
	 * @param lastItemUpdate the lastItemUpdate to set
	 */
	public void setLastItemUpdate(Date lastItemUpdate) {
		this.lastItemUpdate = lastItemUpdate;
	}
	/**
	 * @param nbDrafts the nbDraft to set
	 */
	public void setNbDrafts(Integer nbDrafts) {
		this.nbDrafts = nbDrafts;
	}
	/**
	 * @param nbItems the nbItems to set
	 */
	public void setNbItems(Integer nbItems) {
		this.nbItems = nbItems;
	}
	/**
	 * @param smfId the smfId to set
	 */
	public void setSmfId(Integer smfId) {
		this.smfId = smfId;
	}
	@Override
	@Id
	@Column(name = "user_id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
		
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
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
	public Role getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}
	
	

}
