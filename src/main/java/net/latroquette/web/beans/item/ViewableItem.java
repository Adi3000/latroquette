package net.latroquette.web.beans.item;

import java.util.List;

import net.latroquette.common.database.data.keyword.MainKeyword;

public interface ViewableItem {
	
	String getImageUrl();
	String getSmallImageUrl();
	String getName();
	Integer getId();
	/**
	 * Return the origin of where cames this Item information 
	 * (using {@code AMAZON_SOURCE} or  {@code LATROQUETTE_SOURCE})
	 * @return  {@code AMAZON_SOURCE} or  {@code LATROQUETTE_SOURCE}
	 */
	String getSource();
	List<MainKeyword> getCategories();

}
