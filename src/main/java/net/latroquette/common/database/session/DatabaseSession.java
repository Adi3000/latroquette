package net.latroquette.common.database.session;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.data.keyword.DataObject;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class DatabaseSession {
	private static final Logger LOGGER = Logger.getLogger(DatabaseSession.class.getName());

	protected Session session;
	private boolean initDbSession;
	private Transaction transaction;
	public DatabaseSession(DatabaseSession db){
		setInitDbSession(true);
		this.session = db.getSession();
		this.transaction = db.transaction;
	}
	
	public DatabaseSession(boolean initDbSession){
		if(initDbSession){
			setInitDbSession(true);
			this.session = this.getSession();
		}else{
			setInitDbSession(false);
		}
	}
	public DatabaseSession(){
		this(true);
	}
	/**
	 * Return true if a persist is engaged on this session. (It means 
	 * that a transaction has began and has not finished yet)
	 * @return
	 */
	public boolean isSetForCommitting() {
		return session != null && session.getTransaction().isActive();
	}

	/**
	 * Initialize session for a modification request
	 */
	private void initTransaction() {
		openSession();
		if(transaction == null){
			this.transaction = 	this.session.beginTransaction();
		}
	}
	
	/**
	 * Return the Hibernate session. Create it if not opened first
	 * @return
	 */
	public Session getSession(){
		openSession();
		return session;
	}
	

	public void persist(Collection<DataObject> toCommitList){
		initTransaction();
		for(DataObject modelData : toCommitList)
		{
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
			case IDatabaseConstants.MERGE :
				session.merge(modelData);
				break;
			case IDatabaseConstants.NO_ACTION:
			default :
				break;
			}
		}
	}

	public void persist(DataObject modelData){
		persist(Collections.singleton(modelData));
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

	public List<AbstractDataObject> getListOfSqlRequest(String sqlRequest)
	{
		openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sqlRequest);
		@SuppressWarnings("unchecked")
		List<AbstractDataObject> list =  sqlQuery.list();
		return list;
	}

	private void openSession()
	{
		if(session == null){
			session = HibernateUtils.getSessionFactory().openSession();
		}
	}

	public void closeSession(){
		if(session != null){
			session.clear();
			session.close();
		}
	}

	/**
	 * @return the initDbSession
	 */
	public boolean hasInitDbSession() {
		return initDbSession;
	}

	/**
	 * @param initDbSession the initDbSession to set
	 */
	protected void setInitDbSession(boolean initDbSession) {
		this.initDbSession = initDbSession;
	}
}
