package net.latroquette.web.beans.admin;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.profile.Role;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.beans.profile.UserBean;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.web.faces.FacesUtil;

@ManagedBean
@ViewScoped
public class RoleBean {
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	private Role newRole;
	private List<Role> roleList;
	/**
	 * @return the roleList
	 */
	public List<Role> getRoleList() {
		return roleList;
	}

	public RoleBean() {
		newRole = new Role();
	}
	
	/**
	 * @return the newRole
	 */
	public Role getNewRole() {
		return newRole;
	}
	/**
	 * @param newRole the newRole to set
	 */
	public void setNewRole(Role newRole) {
		this.newRole = newRole;
	}
	public void loadRoles(){
		if(userBean.isLoggedIn() && userBean.isAdmin()){
			roleList = usersService.getAllRoles();
		}else if(userBean.isLoggedIn()){
			FacesUtil.navigationForward("/error404");
		}else{
			FacesUtil.navigationRedirect("/profile/login");
		}
	}
	
	public String create(){
		newRole.setDatabaseOperation(DatabaseOperation.INSERT);
		modify(newRole);
		newRole = new Role();
		return null;
	}
	
	public String modify(String id){
		Role role = (Role) CommonUtil.findById(roleList, Integer.valueOf(id));
		role.setDatabaseOperation(DatabaseOperation.UPDATE);
		modify(role);
		return null;
	}
	
	
	private String modify(Role role){
		usersService.modifyRole(role);
		return null;
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
