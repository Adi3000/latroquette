package net.latroquette.web.beans.commons;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.util.web.Navigation;

import com.adi3000.common.util.tree.Breadcrumb;

@ManagedBean
@RequestScoped
public class NavigationBean {
	@ManagedProperty(value="#{webConstantsBean.navigationMap}")
	private Map<String, Navigation> navigationMap;
	private Keyword actualKeyword;
	private Navigation navigation;
	private List<Navigation> breadcrumb;
	
	@ManagedProperty(value="#{request.requestURI}")
	private String navigationPath;
	@ManagedProperty(value="#{request.queryString}")
	private String queryString;
	/**
	 * @return the actualKeyword
	 */
	public Keyword getActualKeyword() {
		return actualKeyword;
	}
	/**
	 * @param actualKeyword the actualKeyword to set
	 */
	public void setActualKeyword(Keyword actualKeyword) {
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
	@PostConstruct
	public void refreshNavigation(){
		this.navigationPath = navigationPath.replaceAll("(\\.xhtml|\\.jsf)$", "");
		if(navigationPath.endsWith("/")){
			navigationPath = navigationPath.concat("index");
		}
		this.navigation = navigationMap.get(navigationPath);
		this.breadcrumb = new Breadcrumb<Navigation>(navigation).getBreadcrumb();
	}
	
}
