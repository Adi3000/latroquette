package net.latroquette.common.database.data.keyword;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.latroquette.common.database.data.AbstractDataObject;

@Entity
@Table(name = "keywords")
public class Keyword extends AbstractDataObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039813933386523713L;

	private Double id;
	private String label;
	private Integer type;
	private Double precision;
	@Override
	@Column(name = "keyword_id")
	public Serializable getId() {
		return id;
	}
	/**
	 * @return the label
	 */
	@Column(name = "keyword_label")
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
	 * @return the type
	 */
	@Column(name = "keyword_type_id")
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the precision
	 */
	@Column(name = "keyword_precision")
	public Double getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(Double precision) {
		this.precision = precision;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Double id) {
		this.id = id;
	}

}
