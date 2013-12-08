package net.latroquette.common.test.dbunit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * Base DbUnit Test case that initializes test database. 
 * 
 * @author Denis Pavlov 
 * @since 1.0.0 
 * 
 */  
public abstract class ListDbUnitTestCase extends DbUnitTestCase{  
  
    protected static final Logger LOG = LoggerFactory.getLogger(ListDbUnitTestCase.class);  
    private IDataSet currentDataSet;
    
    public ListDbUnitTestCase(){
        super();
    }
	
    
	protected abstract List<String> getDataSetsPath();
	
    private List<IDataSet> getDataSets(){
    	List<String> dataSetsPath = getDataSetsPath();
    	List<IDataSet> dataSets = new ArrayList<IDataSet>(dataSetsPath.size());
    	
    	for(String dataSetPath : dataSetsPath){
			dataSets.add(DbUnitTestCase.getDataSetFromResource(dataSetPath));
    	}
    	return dataSets;
    }
    
	/**
	 * Charge un DataSet en base
	 * @param dataSet Données à charger
	 */
    @Before
    public void setUp() {
    	List<IDataSet> dataSets = getDataSets();
		executeOperation(getSetUpOperation(),dataSets);
	}
    
	/**
	 * Charge un DataSet en base
	 * @param dataSet Données à charger
	 */
    @After
	public void tearDown() {
    	List<IDataSet> dataSets = getDataSets();
    	Collections.reverse(dataSets);
		executeOperation(getTearDownOperation(),dataSets);
	}
    
	/**
	 * Executes a DatabaseOperation with a IDatabaseConnection supplied by
	 * {@link getConnection()} and the test dataset.
	 */
	private void executeOperation( DatabaseOperation operation, List<IDataSet> dataSets) {
		LOG.debug("executeOperation(operation={}) - start", operation);
		if( operation != DatabaseOperation.NONE ){
			if(dataSets != null){
				IDatabaseConnection connection = null;
				try{
					connection = getConnection();
					for(IDataSet dataSet : dataSets){
						currentDataSet = dataSet;
						operation.execute( connection, dataSet );
					}
				} catch (DatabaseUnitException | SQLException e2 ) {
					LOG.error("Error while executing operation ", e2);
					fail("Error while executing operation ");
					throw new RuntimeException(e2);
				} catch (Exception e) {
					LOG.error("Error while executing operation ",e);
					fail("Error while executing operation ");
					throw new RuntimeException(e);
				} finally{
					if(connection != null){
						try {
							closeConnection( connection );
						} catch (Exception e) {
							LOG.error("Error while closing operation");
							fail("Error while closing operation");
						}
					}
				}
			}
		}
	}
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return currentDataSet;
	}
	
}  
