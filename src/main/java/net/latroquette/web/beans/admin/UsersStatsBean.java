package net.latroquette.web.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.web.faces.FacesUtil;

import net.latroquette.common.database.data.profile.Role;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UserBase;
import net.latroquette.common.database.data.profile.UserStatistics;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.ServiceException;
import net.latroquette.common.util.Services;
import net.latroquette.web.beans.profile.UserBean;
import net.latroquette.web.security.AuthenticationMethod;
import net.latroquette.web.security.SecurityUtil;

@ManagedBean
@ViewScoped
public class UsersStatsBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127848308811905982L;

	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	private UserStatistics filter;
	private List<UserStatistics> result;
	private int page;
	private List<Role> roles;
	
	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	public UsersStatsBean(){
		page = 1;
		filter = new UserStatistics();
	}
	
	/**
	 * @return the filter
	 */
	public UserStatistics getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(UserStatistics filter) {
		this.filter = filter;
	}
	/**
	 * @return the result
	 */
	public List<UserStatistics> getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(List<UserStatistics> result) {
		this.result = result;
	}
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	public void loadUsersStats(){
		if(userBean.isLoggedIn() && userBean.isAdmin()){
			search();
		}else if(userBean.isLoggedIn()){
			FacesUtil.navigationRedirect("/error404");
		}else{
			userBean.setDisplayLoginBox(true);
			FacesUtil.navigationForward(SecurityUtil.LOGIN_VIEW_PATH);
		}
	}
	
	public String search(){
		result = usersService.getUserStatistics(filter, page);
		roles = usersService.getAllRoles();
		return null;
	}
	
	public String unblockUser(String id){
		usersService.unblockUser(Integer.valueOf(id));
		return null;
	}
	public String blockUser(String id){
		usersService.blockUser(Integer.valueOf(id));
		return null;
	}
	public String forceValidation(String id){
		User user = usersService.getUserById(Integer.valueOf(id));
		usersService.forceValidateUser(user.getLogin(), AuthenticationMethod.ADMIN);
		return null;
	}
	public String applyProfile(){
		for(UserStatistics user : result){
			user.setRole((Role) CommonUtil.findById(roles, user.getRoleId()));
		}
		usersService.updateRoles(new ArrayList<UserBase>(result));
		return null;
	}
	public String blockXmpp(String id){
		return null;
	}
	public String forceSMFRegister(String id){
		return null;
	}
	public String forceSMFValidation(String id){
		try{
			usersService.smfActivateUser(Integer.valueOf(id));
		}catch(ServiceException e){}
		return null;
	}
	public String nextPage(){
		page ++;
		return search();
	}
	public String previousPage(){
		page --;
		return search();
	}
	/**
	 * @return the userBean
	 */
	public UserBean getUserBean() {
		return userBean;
	}
	/**
	 * @param userBean the userBean to set
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
}
