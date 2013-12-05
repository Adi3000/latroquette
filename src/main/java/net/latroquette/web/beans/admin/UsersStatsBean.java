package net.latroquette.web.beans.admin;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.profile.Role;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UserStatistics;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.security.AuthenticationMethod;

@ManagedBean
@ViewScoped
public class UsersStatsBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127848308811905982L;

	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	
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
		search();
	}
	
	public String search(){
		result = usersService.getUserStatistics(filter, page);
		roles = usersService.getAllRoles();
		return null;
	}
	
	public String blockUser(String id){
		return null;
	}
	public String forceValidation(String id){
		User user = usersService.getUserById(Integer.valueOf(id));
		usersService.validateUser(user.getLogin(), AuthenticationMethod.ADMIN);
		return null;
	}
	public String applyProfile(){
		User user =null;
		for(UserStatistics userStat : result){
			user = usersService.getUserById(userStat.getId());
			user.setRole(userStat.getRole());
			usersService.updateUser(user);
		}
		return "/admin/users";
	}
	public String blockXmpp(String id){
		return null;
	}
	public String forceSMFRegister(String id){
		return null;
	}
	public String forceSMFValidation(String id){
		return null;
	}
	public String changeProfile(String userId,String profileId){
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
}
