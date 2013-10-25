package net.latroquette.web.beans.admin;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import net.latroquette.common.util.LaTroquetteService;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.web.Navigation;

import com.adi3000.common.util.optimizer.CommonValues;


@ManagedBean(eager=true)
@ApplicationScoped
public class WebConstantsBean {
	public static String EXTERNAL_KEYWORD_PREFIX = "ext_";
	public static String ADDITIONNAL_KEYWORD_PREFIX = "add_";
	public static String ADDITIONNAL_EXTERNAL_KEYWORD_PREFIX = ADDITIONNAL_KEYWORD_PREFIX.concat(EXTERNAL_KEYWORD_PREFIX);
	private final String separator = CommonValues.SEPARATOR;
	private final String externalKeywordPrefix = EXTERNAL_KEYWORD_PREFIX;
	private final String additionnalKeywordPrefix = ADDITIONNAL_KEYWORD_PREFIX;
	private final String additionnalExternalKeywordPrefix = ADDITIONNAL_EXTERNAL_KEYWORD_PREFIX;
	
	private Map<String, Navigation> navigationMap;
	
	@ManagedProperty(value=Services.LA_TROQUETTE_SERVICE_JSF)
	private transient LaTroquetteService laTroquetteService;
	
	/**
	 * @param laTroquetteService the laTroquetteService to set
	 */
	public void setLaTroquetteService(LaTroquetteService laTroquetteService) {
		this.laTroquetteService = laTroquetteService;
	}

	public String getSeparator(){
		return separator;
	}
	/**
	 * @return the externalPrefix
	 */
	public String getExternalKeywordPrefix() {
		return externalKeywordPrefix;
	}
	/**
	 * @return the additionnalKeywordPrefix
	 */
	public String getAdditionnalKeywordPrefix() {
		return additionnalKeywordPrefix;
	}
	/**
	 * @return the additionnalExternalKeywordPrefix
	 */
	public String getAdditionnalExternalKeywordPrefix() {
		return additionnalExternalKeywordPrefix;
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
	
	@PostConstruct
	public void initWebConstants(){
		navigationMap = laTroquetteService.getNavigationMap();
	}
	
}
