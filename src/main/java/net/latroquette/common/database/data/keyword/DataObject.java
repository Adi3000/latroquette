package net.latroquette.common.database.data.keyword;

import java.io.Serializable;

public interface DataObject extends Serializable{

	public void setDatabaseOperation(int databaseOperation) ;
	public int getDatabaseOperation() ;
	public Serializable getId();
}
