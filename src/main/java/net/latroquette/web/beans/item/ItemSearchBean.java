package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.Services;

import com.adi3000.common.web.faces.FacesUtil;
@ManagedBean
@RequestScoped
public class ItemSearchBean implements Serializable {

	private static final long serialVersionUID = 6018699553817370156L;
	
	@ManagedProperty(Services.ITEMS_SERVICE_JSF)
	private transient ItemsService itemsService;
	
	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}
	private String request;
	private Integer page;
	private Integer count;
	private List<Item> itemsFound; 
	
	private Boolean searchOnDescription = true;
	@ManagedProperty(value="#{navigationBean.actualKeyword}")
	private MainKeyword actualKeyword;
	
	//TODO accessibility feature : add a parameter to not load item via JS
	public String search(){
		String path = "/item/index.xhtml?r=".concat(request);
		return FacesUtil.prepareRedirect(path, true);
	}
	
	public void initCount(){
		this.count = itemsService.countItem(request, searchOnDescription, actualKeyword);
	}
	public void loadSearch(){
		initCount();
		if(count > 0){
			this.itemsFound = itemsService.searchItem(request, searchOnDescription, page, actualKeyword, false);
		}
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

	/**
	 * @return the actualKeyword
	 */
	public MainKeyword getActualKeyword() {
		return actualKeyword;
	}

	/**
	 * @param actualKeyword the actualKeyword to set
	 */
	public void setActualKeyword(MainKeyword actualKeyword) {
		this.actualKeyword = actualKeyword;
	}
	
	public String getActualKeywordId(){
		return actualKeyword != null ? actualKeyword.getId().toString() : null;
	}
	/**
	 * @return the itemsFound
	 */
	public List<Item> getItemsFound() {
		return itemsFound;
	}
}
