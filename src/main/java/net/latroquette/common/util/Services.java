package net.latroquette.common.util;


public interface Services{

	String FILES_SERVICE = "filesService";
	String FILES_SERVICE_JSF = "#{"+FILES_SERVICE+"}";
	String ITEMS_SERVICE = "itemsService";
	String ITEMS_SERVICE_JSF = "#{"+ITEMS_SERVICE+"}";
	String KEYWORDS_SERVICE = "keywordsService";
	String KEYWORDS_SERVICE_JSF = "#{"+KEYWORDS_SERVICE+"}";
	String PLACES_SERVICE = "placesService";
	String PLACES_SERVICE_JSF = "#{"+PLACES_SERVICE+"}";
	String USERS_SERVICE = "usersService";
	String USERS_SERVICE_JSF = "#{"+USERS_SERVICE+"}";
	String PARAMETERS_SERVICE = "parametersService";
	String PARAMETERS_SERVICE_JSF = "#{"+PARAMETERS_SERVICE+"}";
	String ITEM_SEARCH_WEB_SERVICE = "itemSearch";
	String ITEM_SEARCH_WEB_SERVICE_JSF = "#{"+ITEM_SEARCH_WEB_SERVICE+"}";
	String LA_TROQUETTE_SERVICE = "laTroquetteService";
	String LA_TROQUETTE_SERVICE_JSF = "#{"+LA_TROQUETTE_SERVICE+"}";
	String OFFERS_SERVICE = "offerService";
	String OFFERS_SERVICE_JSF = "#{"+OFFERS_SERVICE+"}";
	String AUTHENTICATION_WEB_SERVICE = "authenticationSearch";
}
