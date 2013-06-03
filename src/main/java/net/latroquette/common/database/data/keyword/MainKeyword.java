package net.latroquette.common.database.data.keyword;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.latroquette.common.database.data.AbstractDataObject;

import org.hibernate.annotations.WhereJoinTable;

@Entity
@Table(name = "keywords")
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
	@Override
	@Column(name = "keyword_id")
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
	@ManyToOne
	@JoinTable(name="keywords_relationship", 
			joinColumns={@JoinColumn(name="keyword_to_id")}, 
            inverseJoinColumns={@JoinColumn(name="keyword_from_id")})
	@WhereJoinTable(clause= "relationship_id = 1")
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
	@ManyToMany
	@JoinTable(name="keywords_relationship", 
	joinColumns={@JoinColumn(name="keyword_from_id")}, 
    inverseJoinColumns={@JoinColumn(name="keyword_to_id")})
	@WhereJoinTable(clause= "relationship_id = 2")
	public List<MainKeyword> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<MainKeyword> children) {
		this.children = children;
	}
	
}
