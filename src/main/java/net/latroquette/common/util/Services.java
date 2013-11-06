package net.latroquette.common.util;


public interface Services{

	String FILES_SERVICE = "filesService";
	String ITEMS_SERVICE = "itemsService";
	String KEYWORDS_SERVICE = "keywordsService";
	String PLACES_SERVICE = "placesService";
	String USERS_SERVICE = "usersService";
	String PARAMETERS_SERVICE = "parametersService";
	String FILES_SERVICE_JSF = "#{"+FILES_SERVICE+"}";
	String ITEMS_SERVICE_JSF = "#{"+ITEMS_SERVICE+"}";
	String KEYWORDS_SERVICE_JSF = "#{"+KEYWORDS_SERVICE+"}";
	String PLACES_SERVICE_JSF = "#{"+PLACES_SERVICE+"}";
	String USERS_SERVICE_JSF = "#{"+USERS_SERVICE+"}";
	String PARAMETERS_SERVICE_JSF = "#{"+PARAMETERS_SERVICE+"}";
	String LA_TROQUETTE_SERVICE = "laTroquetteService";
	String LA_TROQUETTE_SERVICE_JSF = "#{"+LA_TROQUETTE_SERVICE+"}";
}
