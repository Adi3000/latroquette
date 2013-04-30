package net.latroquette.common.util.parameters;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.latroquette.common.database.data.AbstractDataObject;

@Entity
@Table(name="parameters")
public class Parameter extends AbstractDataObject{

	private static final long serialVersionUID = -3918158862432820274L;
	
	private Integer id;
	private String name;
	private String description;
	private String value;
	private Boolean isList;
	private List<String> valueList;
	
	@Id
	@Column(name="param_id")    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="param_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="param_value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Column(name="param_is_list")
	public Boolean getIsList() {
		return isList;
	}
	public void setIsList(Boolean isList) {
		this.isList = isList;
	}

	@Transient
	public List<String> getValueList() {
		return valueList;
	}
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}
	@Column(name="param_desc")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
