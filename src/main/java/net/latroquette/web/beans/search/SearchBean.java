package net.latroquette.web.beans.search;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class SearchBean {
	private String placesIds;

	/**
	 * @return the placesIds
	 */
	public String getPlacesIds() {
		return placesIds;
	}

	/**
	 * @param placesIds the placesIds to set
	 */
	public void setPlacesIds(String placesIds) {
		this.placesIds = placesIds;
	}

}
