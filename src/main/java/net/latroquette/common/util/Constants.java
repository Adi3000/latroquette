package net.latroquette.common.util;

import java.util.ResourceBundle;

public class Constants {

	private static final String BUNDLE_NAME = Constants.class.getPackage().getName() + ".commons";
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME); 
	public static final String SMF_REST_ENDPOINT = RESOURCE_BUNDLE.getString("smf.rest.endpoint");
	private Constants() {}

}
