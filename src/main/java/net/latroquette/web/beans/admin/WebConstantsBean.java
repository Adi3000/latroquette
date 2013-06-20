package net.latroquette.web.beans.admin;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

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
}
