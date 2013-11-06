package net.latroquette.common.database.data.item;

import java.sql.Timestamp;
import java.util.List;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Table(name = "items")
@XmlRootElement
@SequenceGenerator(name = "items_item_id_seq", sequenceName = "items_item_id_seq", allocationSize=1)
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
	private List<File> imageList;
	private List<MainKeyword> keywordList;
	private List<ExternalKeyword> externalKeywordList;
	
	@Override
	@Id
	@Column(name="item_id")    
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_item_id_seq")
	public Integer getId() {
		return id;
	}
	/**
	 * @return the user
	 */
	@ManyToOne(optional=false)
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
	@ManyToMany(cascade = {CascadeType.ALL},fetch=FetchType.EAGER)
	@JoinTable(name="items_keywords", 
	joinColumns={@JoinColumn(name="item_id")}, 
	inverseJoinColumns={@JoinColumn(name="keyword_id")})
	@XmlTransient
	public List<MainKeyword> getKeywordList() {
		return keywordList;
	}
	/**
	 * @param keywordList the keywordList to set
	 */
	public void setKeywordList(List<MainKeyword> keywordList) {
		this.keywordList = keywordList;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="items_files", 
                joinColumns={@JoinColumn(name="item_id")}, 
                inverseJoinColumns={@JoinColumn(name="file_id")})
	public List<File> getImageList() {
		return imageList;
	}
	public void setImageList(List<File> imageList) {
		this.imageList = imageList;
	}
	/**
	 * @return the externalKeywordList
	 */
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="items_external_keywords", 
	joinColumns={@JoinColumn(name="item_id")}, 
	inverseJoinColumns={@JoinColumn(name="ext_keyword_id")})
	@XmlTransient
	public List<ExternalKeyword> getExternalKeywordList() {
		return externalKeywordList;
	}
	/**
	 * @param externalKeywordList the externalKeywordList to set
	 */
	public void setExternalKeywordList(List<ExternalKeyword> externalKeywordList) {
		this.externalKeywordList = externalKeywordList;
	}

}
