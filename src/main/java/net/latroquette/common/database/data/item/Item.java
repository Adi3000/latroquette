package net.latroquette.common.database.data.item;

import java.sql.Timestamp;
import java.util.List;

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
import javax.xml.bind.annotation.XmlRootElement;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.profile.User;

@Entity
@Table(name = "items")
@XmlRootElement
@SequenceGenerator(name = "items_item_id_seq", sequenceName = "items_item_id_seq")
public class Item extends AbstractDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3249513332241986469L;
	private Integer id;
	private User user;
	private Integer statusId;
	private Timestamp creationDate;
	private Timestamp updateDate;
	private String title;
	private String description;
	private transient List<Keyword> keywordList;
	@Override
	@Id
	@Column(name="item_id")    
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_item_id_seq")
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}
	/**
	 * @return the user
	 */
	@ManyToOne
	@JoinColumn(name="user_id")
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
	 * @param statusId the statusId to set
	 */
	public void setStatusId(ItemStatus status) {
		this.statusId = status.getValue();
	}
	/**
	 * @return the creationDate
	 */
	@Column(name="item_creation_date")
	public Timestamp getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the updateDate
	 */
	@Column(name="item_update_date")
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
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
	@Transient
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
