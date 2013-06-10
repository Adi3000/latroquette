package net.latroquette.web.beans.admin;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.adi3000.common.util.optimizer.CommonValues;


@ManagedBean(eager=true)
@ApplicationScoped
public class WebConstantsBean {
	private final String separator = CommonValues.SEPARATOR;
	private final String externalPrefix = CommonValues.EXTERNAL_KEYWORD_PREFIX;
	public String getSeparator(){
		return separator;
	}
	/**
	 * @return the externalPrefix
	 */
	public String getExternalPrefix() {
		return externalPrefix;
	}
}
