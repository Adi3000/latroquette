package net.latroquette.common.database;

import java.util.ArrayList;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.session.DatabaseSession;
import net.latroquette.common.engine.EngineLog;




public abstract class AbstractBusinessObject {
	
	private DatabaseSession session;
	private ArrayList<AbstractBusinessObject> shareSessionWith;
	private ArrayList<AbstractDataObject> dataToSave;
	
	public AbstractBusinessObject()
	{
		shareSessionWith = new ArrayList<AbstractBusinessObject>();
	}
	
	public void shareSessionWith(AbstractBusinessObject bo){
		this.shareSessionWith.add(bo);
		bo.shareSessionWith.add(this);
		if(this.session.isSetForCommitting()){
			for(AbstractBusinessObject businessObject : bo.shareSessionWith){
				businessObject.session = this.session;
				if(businessObject.session.isSetForCommitting())
					EngineLog.SERVER.warning("A session was initied by " + businessObject.getClass().toString() + " and will be closed to share connection with " + this.getClass().toString());
			}
		}else{
			for(AbstractBusinessObject businessObject : this.shareSessionWith){
				businessObject.session = this.session;
				if(businessObject.session.isSetForCommitting())
					EngineLog.SERVER.warning("A session was initied by " + businessObject.getClass().toString() + " and will be closed to share connection with " + bo.getClass().toString());
			}
		}
	}
	
	public boolean persist(AbstractBusinessObject data){
		
		return false;
	}
	
	public AbstractDataObject getWithFilter(AbstractDataObject filter,boolean unique)
	{
		//TODO Make a function to retrieve some element with one ModelData
		//this.session.
		return null;
	}
	
	

}
