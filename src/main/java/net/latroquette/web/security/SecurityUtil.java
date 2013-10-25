package net.latroquette.web.security;

import net.latroquette.web.beans.profile.UserBean;

import com.adi3000.common.util.security.Security;
import com.adi3000.common.web.faces.FacesUtil;

public class SecurityUtil {
	private static final String LOGIN_VIEW_PATH = "/profile/login";
	public static boolean checkUserLogged(UserBean user){
		if (!Security.isUserLogged(user) || !user.getLoggedIn()){
			FacesUtil.navigationRedirect(LOGIN_VIEW_PATH);
			return false;
		}
		return true;
	}
	
}
