package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemFilter;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.place.Place;
import net.latroquette.common.database.data.place.PlacesService;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;
import net.latroquette.web.beans.profile.UserBean;

import com.adi3000.common.util.security.User;
import com.adi3000.common.web.faces.FacesUtil;
@ManagedBean
@RequestScoped
public class ItemSearchBean implements Serializable {

	private static final long serialVersionUID = 6018699553817370156L;
	
	@ManagedProperty(Services.ITEMS_SERVICE_JSF)
	private transient ItemsService itemsService;
	@ManagedProperty(Services.PLACES_SERVICE_JSF)
	private transient PlacesService placesService;
	@ManagedProperty(Services.USERS_SERVICE_JSF)
	private transient UsersService usersService;

	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}
	private ItemFilter itemFilter;
	@ManagedProperty(value="#{param.p}")
	private Integer page;
	private Integer count;
	private List<Item> itemsFound; 
	private String memberNameFilter; 
	
	@ManagedProperty(value="#{navigationBean.actualKeyword}")
	private MainKeyword actualKeyword;
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	private String placeNameFilter;
	public ItemSearchBean(){
		itemFilter = new ItemFilter();
	}

	@PostConstruct
	public void initItemFilter(){
		if(actualKeyword != null){
			itemFilter.setKeywordId(actualKeyword.getId());
		}
		fillItemFilter();
		
	}

	//TODO accessibility feature : add a parameter to not load item via JS
	public String search(){
		String path = "/item/index.xhtml?".concat(itemFilter.getRequestURI());
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
	
	/**
	 * @return the memberNameFilter
	 */
	public String getMemberNameFilter() {
		return memberNameFilter;
	}

	/**
	 * @param memberNameFilter the memberNameFilter to set
	 */
	public void setMemberNameFilter(String memberNameFilter) {
		this.memberNameFilter = memberNameFilter;
	}

	/**
	 * @return the placeNameFilter
	 */
	public String getPlaceNameFilter() {
		return placeNameFilter;
	}

	/**
	 * @param placeNameFilter the placeNameFilter to set
	 */
	public void setPlaceNameFilter(String placeNameFilter) {
		this.placeNameFilter = placeNameFilter;
	}

	/**
	 * @param placesService the placesService to set
	 */
	public void setPlacesService(PlacesService placesService) {
		this.placesService = placesService;
	}

	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	/**
	 * Fill a request {@link ItemFilter} for an item
	 */
	private void fillItemFilter(){
		Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance() 
                .getExternalContext().getRequestParameterMap();
		itemsService.setFiltersFromParameter(itemFilter, parameterMap, userBean.getUser());
		if(itemFilter.getOwnerId() != null){
			User user = usersService.getUserById(itemFilter.getOwnerId());
			memberNameFilter = user.getLogin();
		}
		if(itemFilter.getPlaceId() != null){
			Place place = placesService.getPlaceById(itemFilter.getOwnerId());
			placeNameFilter = place.getName().concat(" (").concat(place.getPostalCodes()).concat(")");
		}
	}

}
