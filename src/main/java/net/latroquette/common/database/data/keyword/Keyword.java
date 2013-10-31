package net.latroquette.common.database.data.keyword;

import java.util.List;

import com.adi3000.common.database.hibernate.data.DataObject;

public interface Keyword extends DataObject{
	
	public static final Integer MAIN_ANCESTOR_RELATIONSHIP = 1;
	public static final Integer CHILDREN_RELATIONSHIP = 2;
	public static final Integer EXTERNAL_KEYWORD_RELATIONSHIP = 3;
	public static final String FETCH_CHILDREN_PROFILE = "keyword-with-children";

	/**
	 * @return the name
	 */
	public String getName();
	/**
	 * Method to retrieve all children of this object
	 * @return list of children
	 */
	public List<? extends Keyword> getChildren();
	/**
	 * Return the parent of this object
	 * @return
	 */
	public Keyword getAncestor();
	
	/**
	 * Remove the ancestor
	 */
	public void removeAncestor();
	
	public KeywordType getKeywordType();

	public int getKeywordTypeId();
}
