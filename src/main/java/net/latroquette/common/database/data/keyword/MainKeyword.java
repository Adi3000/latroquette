package net.latroquette.common.database.data.keyword;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.FetchProfile.FetchOverride;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;

import com.adi3000.common.database.hibernate.data.AbstractTreeNodeDataObject;


@Entity
@Table(name = "keywords")
@SequenceGenerator(name = "keywords_keyword_id_seq", sequenceName = "keywords_keyword_id_seq", allocationSize=1)
@FilterDefs({
	@FilterDef(name=KeywordsService.MENU_KEYWORD_ONLY_FILTER),
	@FilterDef(name=KeywordsService.MENU_KEYWORD_CHILDREN_ONLY_FILTER)
})
@Filter(name=KeywordsService.MENU_KEYWORD_ONLY_FILTER, condition="keyword_in_menu = 'Y'")
@FetchProfile(name=KeywordsService.FETCH_CHILDREN_PROFILE, fetchOverrides={
		@FetchOverride(entity=MainKeyword.class, association="children", mode=FetchMode.JOIN)
})
@Cache(region = "keywords", usage = CacheConcurrencyStrategy.READ_WRITE)
public class MainKeyword extends AbstractTreeNodeDataObject<MainKeyword> implements Keyword{
	public static final Integer MAIN_ANCESTOR_RELATIONSHIP = 1;
	public static final Integer CHILDREN_RELATIONSHIP = 2;
	public static final Integer EXTERNAL_KEYWORD_RELATIONSHIP = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039813933386523713L;

	private Integer id;
	private String name;
	private List<MainKeyword> children;
	private MainKeyword ancestor;
	private Boolean isSynonym;
	private Boolean inMenu;
	private List<ExternalKeyword> externalKeywords;
	@Id
	@Column(name = "keyword_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keywords_keyword_id_seq")
	public Integer getId() {
		return id;
	}
	/**
	 * @return the label
	 */
	@Column(name = "keyword_name")
	public String getName() {
		return name;
	}
	/**
	 * @param name the label to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the mainAncestor
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="keyword_parent_id")
	@Cache(region = "keywords", usage = CacheConcurrencyStrategy.READ_ONLY)
	public MainKeyword getAncestor(){
		return ancestor;
	}
	
	/**
	 * @param ancestor the mainAncestor to set
	 */
	public void setAncestor(MainKeyword ancestor){
		this.ancestor = ancestor;
	}
	 
	/**
	 * @return the children
	 */
	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="keywords_relationship", 
		joinColumns={@JoinColumn(name="keyword_from_id")}, 
	    inverseJoinColumns={@JoinColumn(name="keyword_to_id")}
	)
	@Filter(name=KeywordsService.MENU_KEYWORD_ONLY_FILTER, condition="keyword_in_menu = 'Y'")
	@Cache(region = "keywords", usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<MainKeyword> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MainKeyword> children) {
		this.children = children;
	}
	/**
	 * @return the isSynonym
	 */
	@Column(name="keyword_is_synonym")
	@Type(type="yes_no")
	public Boolean getSynonym() {
		return isSynonym;
	}
	/**
	 * @param isSynonym the isSynonym to set
	 */
	public void setSynonym(Boolean isSynonym) {
		this.isSynonym = isSynonym;
	}
	/**
	 * @return the inMenu
	 */
	@Column(name = "keyword_in_menu")
	@Type(type="yes_no")
	public Boolean getInMenu() {
		return inMenu;
	}
	/**
	 * @param inMenu the inMenu to set
	 */
	public void setInMenu(Boolean inMenu) {
		this.inMenu = inMenu;
	}
	/**
	 * Get the {@link ExternalKeyword} linked to this keyword
	 * @return
	 */
	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="external_keywords_relationship", 
	joinColumns={@JoinColumn(name="keyword_id")}, 
    inverseJoinColumns={@JoinColumn(name="ext_keyword_id")})
	public List<ExternalKeyword> getExternalKeywords() {
		return externalKeywords;
	}
	/**
	 * @param externalKeywords the externalKeywords to set
	 */
	public void setExternalKeywords(List<ExternalKeyword> externalKeywords) {
		this.externalKeywords = externalKeywords;
	}
	
	@Transient
	public boolean isRoot(){
		return ancestor == null;
	}
	@Override
	public void removeAncestor() {
		setAncestor(null);
	}
}
