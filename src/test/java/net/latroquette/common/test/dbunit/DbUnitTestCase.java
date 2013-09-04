package net.latroquette.common.test.dbunit;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DbUnitTestCase extends DBTestCase{
	protected static final Logger LOG = LoggerFactory.getLogger(ListDbUnitTestCase.class);  
	protected static final String BUNDLE_NAME = "dbunit.database";
	protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	protected static final String DB_USER = RESOURCE_BUNDLE.getString(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME);
	protected static final String DB_PASSWORD = RESOURCE_BUNDLE.getString(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD);
	protected static final String DB_SCHEMA = RESOURCE_BUNDLE.getString(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA);
	protected static final String DB_URL = RESOURCE_BUNDLE.getString(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL);
	protected static final String DB_DRIVER = RESOURCE_BUNDLE.getString(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS);
	protected static final String DBU_FOLDER = "dbunit";
	protected static final String DBU_EXTENSION= ".dbu.xml";
	
	
	protected static IDataSet getDataSetFromResource(String resourceName){
		FlatXmlDataSet dataSet = null;
		try {
			dataSet = new FlatXmlDataSet(DbUnitTestCase.class.getClassLoader().getResourceAsStream(
					DBU_FOLDER.concat(File.separator).concat(resourceName).concat(DBU_EXTENSION)));
		} catch (DataSetException | IOException e) {
			fail("Cannot find dbu file ".concat(resourceName));
		}
		return dataSet;
	}

	public DbUnitTestCase(){
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, DB_DRIVER);  
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_URL);  
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, DB_USER);  
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, DB_PASSWORD); 
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, DB_SCHEMA); 
	}
	
	/**
	 * Charge un DataSet en base
	 * @param dataSet Données à charger
	 */
    @Before
    public void setUp() {
    	try {
			super.setUp();
		} catch (Exception e) {
			LOG.error("Error on setUp test : ",e);
			fail();
		}
	}
    
	/**
	 * Charge un DataSet en base
	 * @param dataSet Données à charger
	 */
    @After
	public void tearDown() {
    	try {
    		super.tearDown();
    	} catch (Exception e) {
    		LOG.error("Error on tearDown test : ",e);
    		fail();
    	}
	}
    

    /**
     * Returns the database operation executed in test setup.
     */
    protected DatabaseOperation getSetUpOperation() 
    {
        return DatabaseOperation.INSERT;
    }

    /**
     * Returns the database operation executed in test cleanup.
     */
    protected DatabaseOperation getTearDownOperation() 
    {
        return DatabaseOperation.DELETE;
    }
    
    /**
     * Designed to be overridden by subclasses in order to set additional configuration
     * parameters for the {@link IDatabaseConnection}.
     * @param config The settings of the current {@link IDatabaseConnection} to be configured
     */
    protected void setUpDatabaseConfig(DatabaseConfig config) 
    {
       config.setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, Boolean.TRUE);
    }
}
