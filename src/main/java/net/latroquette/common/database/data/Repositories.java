package net.latroquette.common.database.data;

import com.adi3000.common.database.hibernate.data.DataObject;
import com.adi3000.common.database.hibernate.session.DAO;

public interface Repositories<T extends DataObject> extends DAO<T>{

	String FILES_SERVICE = "filesService";
	String ITEMS_SERVICE = "itemsService";
	String KEYWORDS_SERVICE = "keywordsService";
	String LOCATIONS_SERVICE = "locationsService";
	String USERS_SERVICE = "usersService";
	String PARAMETERS_SERVICE = "parametersService";
	String FILES_SERVICE_JSF = "#{"+FILES_SERVICE+"}";
	String ITEMS_SERVICE_JSF = "#{"+ITEMS_SERVICE+"}";
	String KEYWORDS_SERVICE_JSF = "#{"+KEYWORDS_SERVICE+"}";
	String LOCATIONS_SERVICE_JSF = "#{"+LOCATIONS_SERVICE+"}";
	String USERS_SERVICE_JSF = "#{"+USERS_SERVICE+"}";
	String PARAMETERS_SERVICE_JSF = "#{"+PARAMETERS_SERVICE+"}";
}
