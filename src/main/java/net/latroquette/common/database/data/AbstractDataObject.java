package net.latroquette.common.database.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.keyword.DataObject;
import net.latroquette.common.util.optimizer.CommonValues;

public abstract class AbstractDataObject implements DataObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int databaseOperation;
	public AbstractDataObject(){
		this.databaseOperation = IDatabaseConstants.DEFAULT; 
	}

	/**
	 * @param databaseOperation the databaseOperation to set
	 */
	public void setDatabaseOperation(int databaseOperation) {
		this.databaseOperation = databaseOperation;
	}

	/**
	 * @return the databaseOperation
	 */
	@XmlTransient
	public int getDatabaseOperation() {
		return databaseOperation;
	}
	
	public abstract Serializable getId();

	public String toString(){
		return this.getId().toString();
	}
	
	@Override
	public int hashCode(){
		if(getId() != null){
			return getId().hashCode();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object o){
		return this.hashCode() == o.hashCode();
	}
}
