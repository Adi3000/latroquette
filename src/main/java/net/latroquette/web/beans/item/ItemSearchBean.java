package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.Items;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.web.beans.profile.UserBean;

public class ItemSearchBean implements Serializable {

	private static final long serialVersionUID = 6018699553817370156L;
	
	private String request;
	private Integer page;
	
	//TODO accessibility feature : add a parameter to not load item via JS
	@PostConstruct
	public void init(){
		Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String request = parameterMap.get("r");
		String page = parameterMap.get("p");
		this.page = StringUtils.isNotEmpty(page)  ? Integer.valueOf(page) : Integer.valueOf(1);
		this.request = request;
	}
	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}
}
