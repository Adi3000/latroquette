package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.data.item.Items;

import org.apache.commons.lang.StringUtils;
@ManagedBean
@RequestScoped
public class ItemSearchBean implements Serializable {

	private static final long serialVersionUID = 6018699553817370156L;
	
	private String request;
	private Integer page;
	private Integer count;
	private Boolean searchOnDescription;
	
	//TODO accessibility feature : add a parameter to not load item via JS
	@PostConstruct
	public void init(){
		Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String request = parameterMap.get("r");
		String page = parameterMap.get("p");
		this.searchOnDescription = !Boolean.valueOf(parameterMap.get("ot"));
		this.page = StringUtils.isNotEmpty(page)  ? Integer.valueOf(page) : Integer.valueOf(1);
		this.request = request;
		this.count = 0;
	}
	
	public String search(){
		Items items = new Items();
		this.count = items.countItem(request, searchOnDescription);
		items.closeSession();
		return "/item/index.xhtml?faces-redirect=true&includeViewParams=true&r=".concat(request);
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
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * @return the searchOnDescription
	 */
	public Boolean getSearchOnDescription() {
		return searchOnDescription;
	}
	/**
	 * @param searchOnDescription the searchOnDescription to set
	 */
	public void setSearchOnDescription(Boolean searchOnDescription) {
		this.searchOnDescription = searchOnDescription;
	}
}
