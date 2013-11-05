package net.latroquette.common.util.web;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.adi3000.common.database.hibernate.data.AbstractTreeNodeDataObject;

@Entity
@Table(name="navigation")
public class Navigation extends AbstractTreeNodeDataObject<Navigation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594364662214111694L;
	private Integer id;
	private String path;
	private String label;
	private Navigation ancestor;
	private List<Navigation> children;
	private boolean translated;
	
	public Navigation(){
		this.translated = true;
	}

	@Override
	@Id
	@Column(name="nav_id")
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the path
	 */
	@Column(name="nav_path")
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the label
	 */
	@Column(name="nav_label")
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the ancestor
	 */
	@ManyToOne
	@JoinColumn(name="nav_parent_id")
	public Navigation getAncestor() {
		return ancestor;
	}

	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(Navigation ancestor) {
		this.ancestor = ancestor;
	}
	@Override
	@OneToMany(mappedBy="ancestor",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Navigation> getChildren() {
		return children;
	}
	public void setChildren(List<Navigation> children) {
		this.children = children;
	}
	/**
	 * @return the translated
	 */
	@Transient
	public boolean isTranslated() {
		return translated;
	}
	/**
	 * @param translated the translated to set
	 */
	public void setTranslated(boolean translated) {
		this.translated = translated;
	}
}
