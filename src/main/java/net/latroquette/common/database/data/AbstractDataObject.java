package net.latroquette.common.database.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

public abstract class AbstractDataObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int databaseOperation;
	
	//TODO constructor with parameter FOR_INSERT FOR_DELETE or FOR UPDATE

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
}
