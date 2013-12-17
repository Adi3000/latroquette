package net.latroquette.common.database.data.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Table(name="roles")
@SequenceGenerator(name = "roles_role_id_seq", sequenceName = "roles_role_id_seq", allocationSize=1)
@Cache(region = "roles", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends AbstractDataObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7793418632652574160L;
	private Integer id;
	private String label;
	private Boolean modifyKeywords;
	private Boolean admin;
	private Boolean validateItems;
	/**
	 * @return the id
	 */
	@Id
	@Column(name="role_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_role_id_seq")
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
	 * @return the label
	 */
	@Column(name="role_label")
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
	 * @return the modifyKeywords
	 */
	@Column(name="modify_keywords")
	@Type(type="yes_no")
	public Boolean getModifyKeywords() {
		return modifyKeywords;
	}
	/**
	 * @param modifyKeywords the modifyKeywords to set
	 */
	public void setModifyKeywords(Boolean modifyKeywords) {
		this.modifyKeywords = modifyKeywords;
	}
	/**
	 * @return the admin
	 */
	@Column(name="is_admin")
	@Type(type="yes_no")
	public Boolean getAdmin() {
		return admin;
	}
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	/**
	 * @return the validateItems
	 */
	@Column(name="validate_items")
	@Type(type="yes_no")
	public Boolean getValidateItems() {
		return validateItems;
	}
	/**
	 * @param validateItems the validateItems to set
	 */
	public void setValidateItems(Boolean validateItems) {
		this.validateItems = validateItems;
	}

}
