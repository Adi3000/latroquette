package net.latroquette.web.beans.search;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class SearchBean {
	private String locationIds;

	/**
	 * @return the locationIds
	 */
	public String getLocationIds() {
		return locationIds;
	}

	/**
	 * @param locationIds the locationIds to set
	 */
	public void setLocationIds(String locationIds) {
		this.locationIds = locationIds;
	}
	

}
