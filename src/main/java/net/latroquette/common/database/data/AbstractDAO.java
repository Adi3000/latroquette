package net.latroquette.common.database.data;

import net.latroquette.common.database.session.DatabaseSession;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractDAO extends DatabaseSession{
	
	public AbstractDAO(DatabaseSession db){
		super(db);
	}
	
	public AbstractDAO(){
		super();
	}
	
	public AbstractDataObject getDataObjectById(Integer id, Class<? extends AbstractDataObject> clazz){
		Criteria req = this.session.createCriteria(clazz)
				.setMaxResults(1)
				.add(Restrictions.eq("id", id)) ;
		return clazz.cast(req.uniqueResult());
	}
	
}
