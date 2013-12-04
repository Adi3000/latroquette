package net.latroquette.common.database.data.profile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Subselect("select * from user_statistics")
public class UserStatistics extends AbstractDataObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436576772177823092L;

	private Integer id;
	private String login;
	private String mail;
	private Integer nbItems;
	private Integer nbDraft;
	private Date lastItemUpdate;
	private Integer smfId;
	private Boolean isSmfActivated;
	@Override
	public Integer getId() {
		return id;
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
	 * @return the nbDraft
	 */
	@Column(name="nb_drafts")
	public Integer getNbDraft() {
		return nbDraft;
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
	 * @param id the id to set
	 */
	@Column(name="user_id")
	public void setId(Integer id) {
		this.id = id;
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
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @param nbDraft the nbDraft to set
	 */
	public void setNbDraft(Integer nbDraft) {
		this.nbDraft = nbDraft;
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
	
	

}
