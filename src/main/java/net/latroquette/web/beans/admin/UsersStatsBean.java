package net.latroquette.web.beans.admin;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.profile.UserStatistics;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;

@ManagedBean
@ViewScoped
public class UsersStatsBean {
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	
	private UserStatistics filter;
	private List<UserStatistics> result;
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
	
	public String blockUser(String id){
		return null;
	}
	public String revalidateUser(String id){
		return null;
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
}
