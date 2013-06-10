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

import com.adi3000.common.database.hibernate.data.AbstractDataObject;
import com.adi3000.common.util.CommonUtils;


@Entity
@Table(name = "keywords")
@SequenceGenerator(name = "keywords_keyword_id_seq", sequenceName = "keywords_keyword_id_seq", allocationSize=1)
public class MainKeyword extends AbstractDataObject implements Keyword{
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
	private Character isSynonym;
	private Character inMenu;
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
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="keyword_parent_id")
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
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name="keywords_relationship", 
	joinColumns={@JoinColumn(name="keyword_from_id")}, 
    inverseJoinColumns={@JoinColumn(name="keyword_to_id")})
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
	public Character getIsSynonym() {
		return isSynonym;
	}
	/**
	 * @param isSynonym the isSynonym to set
	 */
	public void setIsSynonym(Character isSynonym) {
		this.isSynonym = isSynonym;
	}
	/**
	 * @return the inMenu
	 */
	@Column(name = "keyword_in_menu")
	public Character getInMenu() {
		return inMenu;
	}
	/**
	 * @param inMenu the inMenu to set
	 */
	public void setInMenu(Character inMenu) {
		this.inMenu = inMenu;
	}
	
	@Transient
	public boolean isSynonym(){
		return CommonUtils.isTrue(isSynonym);
	}
	
	@Transient
	public boolean isMenu(){
		return CommonUtils.isTrue(inMenu);
	}
	public void setMenu(boolean inMenu){
		setInMenu(CommonUtils.toChar(inMenu));
	}
	
}
