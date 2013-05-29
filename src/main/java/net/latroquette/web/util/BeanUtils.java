package net.latroquette.web.util;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;

public class BeanUtils {
	/**
	 * Used to redirect when using Prerendering for security or error process
	 * @param path
	 */
	public static void navigationRedirect(String path){
		ConfigurableNavigationHandler nav 
		   = (ConfigurableNavigationHandler) 
				   FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		nav.performNavigation(path);
	}
}
