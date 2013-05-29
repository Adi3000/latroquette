package net.latroquette.common.database.data;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.session.DatabaseSession;

public abstract class AbstractDAO<T extends AbstractDataObject> extends DatabaseSession{
	
	public AbstractDAO(DatabaseSession db){
		super(db);
	}
	
	public AbstractDAO(){
		super();
	}
	public AbstractDAO(boolean initDbSession){
		super(initDbSession);
	}
	
	public T getDataObjectById(Integer id, Class<? extends T> clazz){
		@SuppressWarnings("unchecked")
		T data = (T)this.session.get(clazz, id);
		return data ;
	}
	
	public T  modifyDataObject(T data){
		if(data.getId() == null){
			data.setDatabaseOperation(IDatabaseConstants.INSERT);
		}else{
			data.setDatabaseOperation(IDatabaseConstants.UPDATE);
		}
		persist(data);
		if(commit()){
			return data;
		}else{
			return null;
		}
	}
	
	public boolean deleteDataObject(T data){
		data.setDatabaseOperation(IDatabaseConstants.DELETE);
		persist(data);
		return commit();
	}
	
	public T getDataObject(AbstractDataObject model)
	{
		@SuppressWarnings("unchecked")
		T toReturn = (T)session.get(model.getClass(), model.getId());

		return toReturn;
	}
}
