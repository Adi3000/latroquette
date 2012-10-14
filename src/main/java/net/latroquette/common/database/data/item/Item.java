package net.latroquette.common.database.data.item;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.profile.User;

@Entity
@Table(name = "items")
@XmlRootElement
public class Item extends AbstractDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3249513332241986469L;
	private Integer id;
	private User user;
	private Integer statusId;
	private Date creationDate;
	private Date updateDate;
	private String title;
	private String description;
	private List<Keyword> keywordList;
	@Override
	@Column(name="item_id")
	public Serializable getId() {
		// TODO Auto-generated method stub
		return id;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the statusId
	 */
	@Column(name="item_status_id")
	public Integer getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the creationDate
	 */
	@Column(name="item_creation_date")
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the updateDate
	 */
	@Column(name="item_update_date")
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the title
	 */
	@Column(name="item_title")
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	@Column(name="item_description")
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the keywordList
	 */
	public List<Keyword> getKeywordList() {
		return keywordList;
	}
	/**
	 * @param keywordList the keywordList to set
	 */
	public void setKeywordList(List<Keyword> keywordList) {
		this.keywordList = keywordList;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
