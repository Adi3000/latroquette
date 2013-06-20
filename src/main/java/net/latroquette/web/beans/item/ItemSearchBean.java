package net.latroquette.web.beans.item;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import net.latroquette.common.database.data.item.ItemsService;
@ManagedBean
@RequestScoped
public class ItemSearchBean implements Serializable {

	private static final long serialVersionUID = 6018699553817370156L;
	
	private String request;
	private Integer page;
	private Integer count;
	private Boolean searchOnDescription = true;
	
	//TODO accessibility feature : add a parameter to not load item via JS
	public String search(){

		return "/item/index.xhtml?faces-redirect=true&includeViewParams=true&r=".concat(request);
	}
	
	public void initCount(){
		ItemsService itemsService = new ItemsService();
		this.count = itemsService.countItem(request, searchOnDescription);
		itemsService.close();
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
