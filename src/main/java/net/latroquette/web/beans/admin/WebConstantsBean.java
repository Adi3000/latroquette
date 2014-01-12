package net.latroquette.web.beans.admin;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.KeywordType;
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
	private final String innerSeparator = CommonValues.INNER_SEPARATOR;
	private final String externalKeywordPrefix = EXTERNAL_KEYWORD_PREFIX;
	private final String additionnalKeywordPrefix = ADDITIONNAL_KEYWORD_PREFIX;
	private final String additionnalExternalKeywordPrefix = ADDITIONNAL_EXTERNAL_KEYWORD_PREFIX;
	private final String amazonSource = KeywordSource.AMAZON_SOURCE.getSourceId();
	private final String keywordSource = KeywordSource.MAIN_KEYWORD_SOURCE.getSourceId();
	private final String externalKeywordSource = KeywordSource.EXTERNAL_KEYWORD_SOURCE.getSourceId();
	private final String wishesSource = KeywordSource.WISHES_SOURCE.getSourceId();
	private final String booleanTrue = Boolean.TRUE.toString();
	private final String booleanFalse = Boolean.FALSE.toString();
	
	private Map<String, Navigation> navigationMap;
	
	@ManagedProperty(value=Services.LA_TROQUETTE_SERVICE_JSF)
	private LaTroquetteService laTroquetteService;
	
	/**
	 * @param laTroquetteService the laTroquetteService to set
	 */
	public void setLaTroquetteService(LaTroquetteService laTroquetteService) {
		this.laTroquetteService = laTroquetteService;
	}

	public String getSeparator(){
		return separator;
	}
	public String getInnerSeparator(){
		return innerSeparator;
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
	
	/**
	 * @return the booleanTrue
	 */
	public String getBooleanTrue() {
		return booleanTrue;
	}
	/**
	 * @return the booleanFalse
	 */
	public String getBooleanFalse() {
		return booleanFalse;
	}
	/**
	 * @return {@link KeywordType}.getId() of {@code EXTERNAL_KEYWORD}
	 */
	public String getExternalKeywordTypeId(){
		return String.valueOf(KeywordType.EXTERNAL_KEYWORD.getId());
	}
	/**
	 * @return {@link KeywordType}.getId() of {@code MAIN_KEYWORD}
	 */
	public String getMainKeywordTypeId(){
		return String.valueOf(KeywordType.MAIN_KEYWORD.getId());
	}
	/**
	 * @return {@link KeywordType}.getId() of {@code SYNONYM_KEYWORD}
	 */
	public String getSynonymKeywordTypeId(){
		return String.valueOf(KeywordType.SYNONYME_KEYWORD.getId());
	}
	
	public String getAmazonSource(){
		return amazonSource;
	}
	public String getKeywordSource(){
		return keywordSource;
	}
	public String getExternalKeywordSource(){
		return externalKeywordSource;
	}
	public String getWishesSource(){
		return wishesSource;
	}

	@PostConstruct
	public void initWebConstants(){
		navigationMap = laTroquetteService.getNavigationMap();
	}
	

	
}
