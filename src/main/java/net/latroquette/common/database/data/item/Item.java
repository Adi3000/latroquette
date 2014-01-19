package net.latroquette.common.database.data.item;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.item.wish.SuitableItem;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.util.CommonUtil;

@Entity
@Table(name = "items")
@XmlRootElement
@SequenceGenerator(name = "items_item_id_seq", sequenceName = "items_item_id_seq", allocationSize=1)
public class Item extends AbstractDataObject implements SuitableItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3249513332241986469L;
	public static final String ITEM_SOURCE = "itm";
	private Integer id;
	private User user;
	private Integer statusId;
	private Timestamp creationDate;
	private Timestamp updateDate;
	private String title;
	private String description;
	private Set<File> imageSet;
	private Set<MainKeyword> keywordSet;
	private Set<ExternalKeyword> externalKeywordSet;
	
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
	public Set<MainKeyword> getKeywordSet() {
		return keywordSet;
	}
	@Transient
	@XmlTransient
	public List<MainKeyword> getKeywordList() {
		return new ArrayList<>(getKeywordSet());
	}
	/**
	 * @param keywordList the keywordList to set
	 */
	public void setKeywordSet(Set<MainKeyword> keywordList) {
		this.keywordSet = keywordList;
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
	@XmlTransient
	public Set<File> getImageSet() {
		return imageSet;
	}
	public void setImageSet(Set<File> imageList) {
		this.imageSet = imageList;
	}
	@Transient
	public List<File> getImageList(){
		return new ArrayList<File>(getImageSet());
	}
	/**
	 * @return the externalKeywordList
	 */
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="items_external_keywords", 
	joinColumns={@JoinColumn(name="item_id")}, 
	inverseJoinColumns={@JoinColumn(name="ext_keyword_id")})
	@XmlTransient
	public Set<ExternalKeyword> getExternalKeywordSet() {
		return externalKeywordSet;
	}
	@Transient
	@XmlTransient
	public List<ExternalKeyword> getExternalKeywordList() {
		return new ArrayList<>(getExternalKeywordSet());
	}
	/**
	 * @param externalKeywordList the externalKeywordList to set
	 */
	public void setExternalKeywordSet(Set<ExternalKeyword> externalKeywordList) {
		this.externalKeywordSet = externalKeywordList;
	}
	@Transient
	public String getHtmlDescription(){
		return CommonUtil.parsePlainTextToHtml(getDescription());
	}
	@Override
	@Transient
	public String getName() {
		return getTitle();
	}
	@Override
	@Transient
	public String getSource() {
		return KeywordSource.ITEM_SOURCE.getSourceId();
	}
	@Override
	public boolean equals(Object object){
		if(object == null){
			return false;
		}
		return equals((SuitableItem)object);
	}
	@Override
	public boolean equals(SuitableItem suitableItem) {
		return this.getId().equals(suitableItem.getId()) &&
				this.getSource().equals(suitableItem.getSource());
	}
	@Override
	public void setName(String name) {
		setTitle(name);
	}

}
