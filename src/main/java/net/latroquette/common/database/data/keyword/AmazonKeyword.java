package net.latroquette.common.database.data.keyword;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.latroquette.common.database.data.AbstractDataObject;

@Entity
@Table(name = "amazon_keywords")
public class AmazonKeyword extends AbstractDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7777833322309686347L;
	private String id;
	private String name;

	private String fullname;
	private AmazonKeyword ancestor;
	
	@Override
	@Id
	@Column(name="amz_key_id")
	public String getId() {
		return this.id;
	}
	/**
	 * @return the name
	 */
	@Column(name="amz_key_name")
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
	@Column(name="amz_key_full_name")
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
	@JoinColumn(name="amz_key_parent_id")
	public AmazonKeyword getAncestor() {
		return ancestor;
	}
	
	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(AmazonKeyword ancestor) {
		this.ancestor = ancestor;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
