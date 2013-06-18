package net.latroquette.common.database.data.keyword;

import com.adi3000.common.database.hibernate.data.DataObject;

public interface Keyword extends DataObject{
	
	public static final Integer MAIN_ANCESTOR_RELATIONSHIP = 1;
	public static final Integer CHILDREN_RELATIONSHIP = 2;
	public static final Integer EXTERNAL_KEYWORD_RELATIONSHIP = 3;


	/**
	 * @return the name
	 */
	public String getName();
	
}
