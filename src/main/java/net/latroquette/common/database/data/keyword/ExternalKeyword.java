package net.latroquette.common.database.data.keyword;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.item.wish.SuitableItem;
import net.latroquette.common.database.data.item.wish.Wish;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.adi3000.common.database.hibernate.data.AbstractTreeNodeDataObject;
import com.adi3000.common.util.tree.TreeNode;


@Entity
@Table(name = "external_keywords")
@SequenceGenerator(name = "external_keywords_ext_keyword_id_seq", sequenceName = "external_keywords_ext_keyword_id_seq", allocationSize=1)
@Cache(region = "keywords", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExternalKeyword extends AbstractTreeNodeDataObject<ExternalKeyword> implements Keyword, TreeNode<ExternalKeyword> {
	public static final String EXTERNAL_KEYWORD_SOURCE= "xey";
	/**
	 * Amazon id for source_id field 
	 */
	public static final String AMAZON_SOURCE = "amz";
	private static final long serialVersionUID = -7777833322309686347L;
	private Integer id;
	private String uid;
	private String source;
	private String name;
	private List<ExternalKeyword> children;
	private List<MainKeyword> mainKeywords;
	private String fullname;
	private ExternalKeyword ancestor;
	private Boolean excluded;
	
	@Override
	@Id
	@Column(name="ext_keyword_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "external_keywords_ext_keyword_id_seq")
	public Integer getId() {
		return this.id;
	}
	/**
	 * @return the name
	 */
	@Column(name="ext_keyword_name")
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the fullname
	 */
	@Column(name="ext_keyword_fullname")
	public String getFullname() {
		return fullname;
	}
	
	/**
	 * @param fullname the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	/**
	 * @return the ancestor
	 */
	@ManyToOne
	@JoinColumn(name="ext_keyword_parent_id")
	@XmlTransient
	public ExternalKeyword getAncestor() {
		return ancestor;
	}
	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(ExternalKeyword ancestor) {
		this.ancestor = ancestor;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the uid
	 */
	@Column(name="ext_uid")
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the source
	 */
	@Column(name="source_id")
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	@OneToMany(mappedBy="ancestor",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@XmlTransient
	public List<ExternalKeyword> getChildren() {
		return children;
	}
	public void setChildren(List<ExternalKeyword> children) {
		this.children = children;
	}
	/**
	 * @return the excluded
	 */
	@Column(name = "ext_keyword_excluded")
	@Type(type="yes_no")
	public Boolean getExcluded() {
		return excluded;
	}
	/**
	 * @param excluded the excluded to set
	 */
	public void setExcluded(Boolean excluded) {
		this.excluded = excluded;
	}
	
	@Override
	public void removeAncestor() {
		setAncestor(null);
	}
	/**
	 * @return the mainKeyword
	 */
	@Transient
	@XmlTransient
	public List<MainKeyword> getMainKeywords() {
		return mainKeywords;
	}
	/**
	 * @param mainKeyword the mainKeyword to set
	 */
	public void setMainKeyword(List<MainKeyword> mainKeywords) {
		this.mainKeywords = mainKeywords;
	}
	@Transient	
	public KeywordType getKeywordType(){
		return KeywordType.EXTERNAL_KEYWORD;
	}
	@Transient
	public int getKeywordTypeId(){
		return getKeywordType().getId();
	}
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		return this.equals((Wish)o);
	}
	public boolean equals(Wish wish){
		return this.getSource().equals(wish.getSource()) &&
				this.getUid().equals(wish.getUid());
	}
	@Override
	public boolean equals(SuitableItem suitableItem) {
		return this.getSource().equals(suitableItem.getSource()) &&
				this.getId().equals(suitableItem.getId());
	}
}
