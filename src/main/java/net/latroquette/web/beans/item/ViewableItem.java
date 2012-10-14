package net.latroquette.web.beans.item;

import java.util.List;

import net.latroquette.common.database.data.keyword.Keyword;

public interface ViewableItem {
	
	public static final int AMAZON_SOURCE = 2; 
	public static final int LATROQUETTE_SOURCE = 1; 
	
	String getImageUrl();
	String getSmallImageUrl();
	String getName();
	Integer getId();
	/**
	 * Return the origin of where cames this Item information 
	 * (using {@code AMAZON_SOURCE} or  {@code LATROQUETTE_SOURCE})
	 * @return  {@code AMAZON_SOURCE} or  {@code LATROQUETTE_SOURCE}
	 */
	int getSource();
	List<Keyword> getCategories();

}
