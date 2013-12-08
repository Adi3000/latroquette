package net.latroquette.web.security;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.web.beans.profile.UserBean;

import com.adi3000.common.util.security.Security;
import com.adi3000.common.web.faces.FacesUtil;

public class SecurityUtil {
	public static final String LOGIN_VIEW_PATH = "/profile/login";
	//TODO set this one on parameter
	private static final String TOKEN_COOKIE = "ltq_token";
	private static final String USER_COOKIE = "ltq_user";
	private static final String SMF_COOKIE = "SMFCookie455";
	public static boolean checkUserLogged(UserBean user){
		if (user == null || !Security.isUserLogged(user.getUser()) || !user.isActuallyLoggedIn()){
			user.logoutUser();
			user.setDisplayLoginBox(true);
			FacesUtil.navigationForward(LOGIN_VIEW_PATH);
			return false;
		}
		return true;
	}
	
	public static void removeSMFCookie(){
		FacesUtil.setCookie(SMF_COOKIE, "", 0, false);
	}
	public static void removeTokenCookie(){
		FacesUtil.setCookie(TOKEN_COOKIE, "", 0, false);
		FacesUtil.setCookie(USER_COOKIE, "", 0, false);
	}
	
	public static void setTokenCookie(User user){
		user.setToken(Security.generateTokenID(user));
		FacesUtil.setCookie(TOKEN_COOKIE, user.getToken(), -1, false);
		FacesUtil.setCookie(USER_COOKIE, user.getLogin(), -1, false);
	}
	
}
