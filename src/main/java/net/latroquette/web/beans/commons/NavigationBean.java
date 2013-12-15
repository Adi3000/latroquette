package net.latroquette.web.beans.commons;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.web.Navigation;

import org.apache.commons.lang.StringUtils;

import com.adi3000.common.util.tree.Breadcrumb;
import com.adi3000.common.web.faces.FacesUtil;

@ManagedBean
@RequestScoped
public class NavigationBean {
	public static final String EDIT_ITEM_VIEW = "/item/editItem";
	public static final String VIEW_ITEM_VIEW = "/item/viewItem";
	private static final String SEARCH_ITEM_VIEW = "/item/index";
	private static final String HOME_PAGE_VIEW = "/index";
	@ManagedProperty(value="#{webConstantsBean.navigationMap}")
	private Map<String, Navigation> navigationMap;
	@ManagedProperty(value=Services.ITEMS_SERVICE_JSF)
	private ItemsService itemsService;
	@ManagedProperty(value=Services.KEYWORDS_SERVICE_JSF)
	private KeywordsService keywordsService;
	private MainKeyword actualKeyword;
	private Navigation navigation;
	private List<Navigation> breadcrumb;
	@ManagedProperty(value="#{param.item}")
	private String itemId;
	@ManagedProperty(value="#{param.c}")
	private String keywordId;
	@ManagedProperty(value="#{request.requestURI}")
	private String navigationPath;
	@ManagedProperty(value="#{param.r}")
	private String itemPattern;
	@ManagedProperty(value="#{request.queryString}")
	private String queryString;
	private List<AmazonItem> pubItems;
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
	 * @return the navigationPath
	 */
	public String getNavigationPath() {
		return navigationPath;
	}
	/**
	 * @param navigationPath the navigationPath to set
	 */
	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}
	/**
	 * @return the navigation
	 */
	public Navigation getNavigation() {
		return navigation;
	}
	
	/**
	 * @return the navigationMap
	 */
	public Map<String, Navigation> getNavigationMap() {
		return navigationMap;
	}
	/**
	 * @param navigationMap the navigationMap to set
	 */
	public void setNavigationMap(Map<String, Navigation> navigationMap) {
		this.navigationMap = navigationMap;
	}
	
	/**
	 * @return the breadcrumb
	 */
	public List<Navigation> getBreadcrumb() {
		return breadcrumb;
	}
	
	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}
	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the keywordId
	 */
	public String getKeywordId() {
		return keywordId;
	}
	/**
	 * @param keywordId the keywordId to set
	 */
	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}
	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}
	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}
	/**
	 * @return the itemPattern
	 */
	public String getItemPattern() {
		return itemPattern;
	}
	/**
	 * @return the pubItems
	 */
	public List<AmazonItem> getPubItems() {
		return pubItems;
	}
	/**
	 * @param itemPattern the itemPattern to set
	 */
	public void setItemPattern(String itemPattern) {
		this.itemPattern = itemPattern;
	}
	
	public boolean isOnHomePage(){
		return HOME_PAGE_VIEW.equals(navigationPath);
	}
	@PostConstruct
	public void refreshNavigation(){
		this.navigationPath = navigationPath.replaceAll("(\\.xhtml|\\.jsf)$", "");
		if(navigationPath.endsWith("/")){
			navigationPath = navigationPath.concat("index");
		}
		this.navigation = navigationMap.get(navigationPath);
		this.breadcrumb = new Breadcrumb<Navigation>(navigation).getBreadcrumb();
		if(		SEARCH_ITEM_VIEW.equals(navigationPath) ||
				VIEW_ITEM_VIEW.equals(navigationPath) ||
				EDIT_ITEM_VIEW.equals(navigationPath)){
			if(StringUtils.isNotEmpty(keywordId)){
				actualKeyword = keywordsService.getKeywordById(Integer.valueOf(keywordId));
			}else if (StringUtils.isNotEmpty(itemId)){
				Item item = itemsService.getItemById(Integer.valueOf(itemId));
				if(item != null && item.getKeywordList() != null && ! item.getKeywordList().isEmpty()){
					actualKeyword = item.getKeywordList().get(0);
				}
			}
			if(actualKeyword != null){
				List<MainKeyword> expendedBreadcrumb = new Breadcrumb<MainKeyword>(actualKeyword).getBreadcrumb();
				for(MainKeyword keyword : expendedBreadcrumb){
					Navigation additionnalNavigation = new Navigation();
					additionnalNavigation.setLabel(keyword.getName());
					additionnalNavigation.setTranslated(false);
					additionnalNavigation.setPath(
							FacesUtil.prepareRedirect(
									SEARCH_ITEM_VIEW.concat("?c=").concat(keyword.getId().toString())));
					breadcrumb.add(breadcrumb.size()-1,additionnalNavigation);
				}
			}
		}
	}
	
}
