package net.latroquette.common.util.parameters;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.util.optimizer.DataType;

@Entity
@Table(name="parameters")
@SequenceGenerator(name = "parameters_param_id_seq", sequenceName = "parameters_param_id_seq")
public class Parameter extends AbstractDataObject{

	private static final long serialVersionUID = -3918158862432820274L;
	
	private Integer id;
	private String name;
	private String description;
	private String value;
	private String dataTypeId;
	private List<String> valueList;
	
	@Id
	@Column(name="param_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parameters_param_id_seq")
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

	@Column(name="param_data_type")
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	@Transient
	public DataType getDataType(){
		return DataType.getDataType(dataTypeId);
	}

	@Transient
	public List<String> getValueList() {
		return valueList;
	}
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}
	@Column(name="param_description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
