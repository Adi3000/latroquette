package net.latroquette.web.security;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.web.beans.profile.UserBean;

import com.adi3000.common.util.security.Security;
import com.adi3000.common.web.faces.FacesUtil;
import com.adi3000.common.web.jsf.UtilsBean;

public class SecurityUtil {
	public static final String LOGIN_VIEW_PATH = "/profile/login";
	//TODO set this one on parameter
	private static final String TOKEN_COOKIE = "ltq_token";
	private static final String USER_COOKIE = "ltq_user";
	private static final String SMF_COOKIE = "SMFCookie455";
	public static boolean checkUserLogged(UserBean user){
		if (user == null || !Security.isUserLogged(user.getUser()) || !user.isActuallyLoggedIn()){
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			user.logoutUser();
			user.setDisplayLoginBox(true);
			user.setPreviousURI(request.getRequestURI());
			user.setPreviousQueryString(UtilsBean.urlEncode(request.getQueryString()));
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
	public static boolean checkCookies(){
		return FacesUtil.getCookie(TOKEN_COOKIE) != null && FacesUtil.getCookie(USER_COOKIE) != null;
	}
	
	public static void setTokenCookie(User user){
		user.setToken(Security.generateTokenID(user));
		FacesUtil.setCookie(TOKEN_COOKIE, user.getToken(), -1, false);
		FacesUtil.setCookie(USER_COOKIE, user.getLogin(), -1, false);
	}
	
}
