package net.latroquette.common.database.data.keyword;

import java.util.List;

public interface Keyword extends DataObject{
	
	public static final Integer MAIN_ANCESTOR_RELATIONSHIP = 1;
	public static final Integer CHILDREN_RELATIONSHIP = 2;
	public static final Integer EXTERNAL_KEYWORD_RELATIONSHIP = 3;


	public Integer getId() ;
	/**
	 * @return the name
	 */
	public String getName();
	
	/**
	 * @return the mainAncestor
	 */
	public Keyword getAncestor();
	
	/**
	 * @return the childrens
	 */
	public List<? extends Keyword> getChildren() ;
	
}
