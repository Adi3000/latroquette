package net.latroquette.common.database.session;

import java.util.ArrayList;
import java.util.List;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.engine.EngineLog;

import org.hibernate.SQLQuery;
import org.hibernate.Session;




public class DatabaseSession {

	protected Session session;

	public DatabaseSession(DatabaseSession db){
		this.session = db.getSession();
	}
	
	public DatabaseSession(){
		this.session = this.getSession();
	}
	/**
	 * Return true if a persist is engaged on this session. (It means 
	 * that a transaction has began and has not finished yet)
	 * @return
	 */
	public boolean isSetForCommitting() {
		// TODO Auto-generated method stub
		return session != null && session.getTransaction().isActive();
	}

	/**
	 * Initialize session for a modification request
	 */
	private void initTransaction() {
		// TODO Auto-generated method stub
		openSession();		
		this.session.beginTransaction();
	}
	
	/**
	 * Return the Hibernate session. Create it if not opened first
	 * @return
	 */
	public Session getSession(){
		openSession();
		return session;
	}
	

	public void persist(ArrayList<AbstractDataObject> toCommitList){
		initTransaction();
		for(AbstractDataObject modelData : toCommitList)
		{
			//TODO Set case for update, delete or insert
			switch(modelData.getDatabaseOperation())
			{
			case IDatabaseConstants.DELETE :
				session.delete(modelData);
				break;
			case IDatabaseConstants.INSERT :
				session.save(modelData);
				break;
			case IDatabaseConstants.INSERT_OR_UPDATE :
				session.saveOrUpdate(modelData);
				break;
			default :
				break;
			}
		}
	}

	public void persist(AbstractDataObject modelData){
		initTransaction();
		//TODO Set case for update, delete or insert
		switch(modelData.getDatabaseOperation())
		{
		case IDatabaseConstants.DELETE :
			session.delete(modelData);
			break;
		case IDatabaseConstants.INSERT :
			session.save(modelData);
			break;
		case IDatabaseConstants.INSERT_OR_UPDATE :
			session.saveOrUpdate(modelData);
			break;
		case IDatabaseConstants.UPDATE :
			session.update(modelData);
			break;
		default :
			break;
		}
	}

	public boolean commit(){
		if(isSetForCommitting()){
			session.getTransaction().commit();
			return true;
		}
		return false;		
	}
	
	public boolean rollback(){
		if(isSetForCommitting()){
			session.getTransaction().rollback();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<AbstractDataObject> getListOfSqlRequest(String sqlRequest)
	{
		openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sqlRequest);
		List<AbstractDataObject> list =  sqlQuery.list();
		return list;
	}

	private void openSession()
	{
		if(session == null)
			session = HibernateUtils.getSessionFactory().openSession();
	}

	public AbstractDataObject getDataObject(AbstractDataObject model)
	{
		openSession();
		AbstractDataObject toReturn = (AbstractDataObject)session.get(model.getClass(), model.getId());

		return toReturn;
	}
}
