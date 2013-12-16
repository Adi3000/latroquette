package net.latroquette.common.database.data.keyword;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.adi3000.common.database.hibernate.session.DAO;
import com.amazon.ECS.client.jax.BrowseNode;

public interface KeywordsService extends DAO<Keyword>{
	
	public static final String KEYWORD_ANCESTOR_SEPARATOR = " > ";
	public static final String MENU_KEYWORD_ONLY_FILTER = "keyword-menu-only";
	public static final String MENU_KEYWORD_EXCLUDE_SYNONYME_FILTER = "keyword-menu-exclude-synonym";
	public static final String MENU_KEYWORD_CHILDREN_ONLY_FILTER = "keyword-children-menu-only";
	public static final String FETCH_CHILDREN_PROFILE = "fetch-children-profile";
	
	public MainKeyword getKeywordById(Integer id);
	
	public MainKeyword getKeywordById(Integer id, boolean deep);	
	
	public MainKeyword getKeyword(MainKeyword keyword);
	
	public Keyword modifyKeyword(Keyword keyword);
	
	public MainKeyword modifyKeyword(MainKeyword keyword, MainKeyword parent);
	
	public Collection<Keyword> modifyKeywords(Collection<Keyword> keyword);
	
	/**
	 * Delete a keyword removing all children and updating children for their relation chips
	 * @param keyword
	 * @return
	 */
	public boolean deleteKeyword(Keyword keyword);
	
	/**
	 * Exclude a externalKeyword and all its children
	 * @param title
	 * @return
	 */
	public boolean excludeExternalKeyword(ExternalKeyword keyword);
	
	
	/**
	 * Fill database with a title
	 * @param title
	 * @return
	 */
	public List<ExternalKeyword> getAmazonKeywordsFromItem(String title, boolean createNotFound);
	
	/**
	 * Return an Amazon Keyword already copied in database
	 * @param amazonBrowseNode
	 * @return
	 */
	public ExternalKeyword getAmazonKeyword(BrowseNode amazonBrowseNode);
	
	/**
	 * Create a new {@link ExternalKeyword} from Amazon {@link BrowseNode} (given by AmazonWebservice
	 * @param amazonBrowseNode
	 * @return
	 */
	public ExternalKeyword createAmazonKeyword(BrowseNode amazonBrowseNode);
	
	
	/**
	 * Return {@link ExternalKeyword} without ancestor
	 * @return
	 */
	public List<ExternalKeyword> getOrphanExternalKeywords();
	
	/**
	 * Return MainKeyword without ancestor
	 * @return
	 */
	public List<MainKeyword> getOrphanMainKeywords();
	
	/**
	 * Return {@link MainKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	public List<MainKeyword> getMainKeywordByIds(List<Integer> ids);
	
	/**
	 * Return {@link ExternalKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	public List<ExternalKeyword> getExternalKeywordByIds(List<Integer> ids);
	/**
	 * Return {@link ExternalKeyword} list filtered by an Id
	 * @param ids
	 * @return
	 */
	public ExternalKeyword getExternalKeywordById(Integer id);
	/**
	 * Return all root menu entry (with no ancestor and marked as displayed in menu)
	 * @return
	 */
	public List<MainKeyword> getMenuRootEntries();
	
	/**
	 * Return a Keyword with no name and an ID set to 0
	 * @return
	 */
	public MainKeyword getRootForMenu();
	/**
	 * Search a Keyword in database
	 */
	public List<Keyword> searchKeyword(String name);
	/**
	 * Search a Keyword in database (External or not)
	 */
	public Collection<MainKeyword> searchMainKeyword(String name);
	
	public Collection<ExternalKeyword> searchExternalKeyword(String name, boolean createIfNotFound);
	
	public List<Keyword> getDirectChildrenOf(Integer id, KeywordType isExternal);
	
	/**
	 * Return all root categories entry (with no ancestor and marked as displayed in menu)
	 * @return
	 */
	public List<MainKeyword> getRootCategoriesEntries();
	
	public Set<Keyword> getAllChildrenOf(Keyword keyword, Set<Keyword> keywordSet);


}
