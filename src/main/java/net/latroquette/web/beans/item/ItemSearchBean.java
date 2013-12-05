package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemFilter;
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
	private ItemFilter itemFilter;
	private Integer page;
	private Integer count;
	private List<Item> itemsFound; 
	
	@ManagedProperty(value="#{navigationBean.actualKeyword}")
	private MainKeyword actualKeyword;
	
	public ItemSearchBean(){
		itemFilter = new ItemFilter();
	}

	@PostConstruct
	public void initItemFilter(){
		if(actualKeyword != null){
			itemFilter.setKeywordId(actualKeyword.getId());
		}
	}

	//TODO accessibility feature : add a parameter to not load item via JS
	public String search(){
		String path = "/item/index.xhtml?r=".concat(itemFilter.getPattern());
		return FacesUtil.prepareRedirect(path, true);
	}
	
	public void initCount(){
		this.count = itemsService.countItem(itemFilter);
	}
	public void loadSearch(){
		initCount();
		if(count > 0){
			this.itemsFound = itemsService.searchItem(itemFilter, page, false);
		}
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
	
	/**
	 * @return the itemsFound
	 */
	public List<Item> getItemsFound() {
		return itemsFound;
	}
	
	/**
	 * @return the itemFilter
	 */
	public ItemFilter getItemFilter() {
		return itemFilter;
	}

	/**
	 * @param itemFilter the itemFilter to set
	 */
	public void setItemFilter(ItemFilter itemFilter) {
		this.itemFilter = itemFilter;
	}
}
