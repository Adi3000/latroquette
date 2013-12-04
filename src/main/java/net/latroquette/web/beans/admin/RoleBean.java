package net.latroquette.web.beans.admin;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.profile.Role;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.util.CommonUtil;

@ManagedBean
@ViewScoped
public class RoleBean {
	@ManagedProperty(value=Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;
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
		roleList = usersService.getAllRoles();
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
}
