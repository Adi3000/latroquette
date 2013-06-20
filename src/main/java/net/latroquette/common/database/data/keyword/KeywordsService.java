package net.latroquette.common.database.data.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.ItemsService;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.util.optimizer.CommonValues;
import com.amazon.ECS.client.jax.BrowseNode;

public class KeywordsService extends AbstractDAO<Keyword> {

	public static final String KEYWORD_ANCESTOR_SEPARATOR = " > ";
	public static final String MENU_KEYWORD_ONLY_FILTER = "keyword-menu-only";
	public static final String MENU_KEYWORD_CHILDREN_ONLY_FILTER = "keyword-children-menu-only";
	public static final String FETCH_CHILDREN_PROFILE = "fetch-children-profile";
	public MainKeyword getKeywordById(Integer id){
		return getKeywordById(id, false);
	}
	public MainKeyword getKeywordById(Integer id, boolean deep){
		MainKeyword keyword = null;
		if(deep){
			Criteria req = createCriteria(MainKeyword.class)
					.add(Restrictions.eq("id",id))
					.setFetchMode("children", FetchMode.SELECT)
					.setCacheable(true)
					.setCacheRegion("keywords");
			keyword = (MainKeyword) req.uniqueResult();
			keyword.initializeRecursively();
			Hibernate.initialize(keyword.getExternalKeywords());
			//For admin keywords : only one level is needed
			for(MainKeyword child : keyword.getChildren()){
				Hibernate.initialize(child.getExternalKeywords());
			}
		}else{
			keyword = (MainKeyword) super.getDataObjectById(id, MainKeyword.class);
		}		
		return keyword; 
	}
	
	
	public MainKeyword getKeyword(MainKeyword keyword){
		return (MainKeyword)super.getDataObject(keyword);
	}
	
	public Keyword modifyKeyword(Keyword keyword){
		return modifyDataObject(keyword);
	}
	public MainKeyword modifyKeyword(MainKeyword keyword, MainKeyword parent){
		modify(keyword);
		if(parent != null){
			modify(parent);
		}
		if(sendForCommit()){
			return keyword;
		}else{
			return null;
		}
	}
	public Collection<Keyword> modifyKeywords(Collection<Keyword> keyword){
		return modifyDataObject(keyword);
	}
	
	/**
	 * Delete a keyword removing all children and updating children for their relation chips
	 * @param keyword
	 * @return
	 */
	public boolean deleteKeyword(Keyword keyword){
		HashSet<Keyword> modifiedKeywords = new HashSet<Keyword>();
		if(keyword.getChildren() != null){
			//Remove all relation with children
			for(Keyword child : keyword.getChildren()){
				child.removeAncestor();
				child.setDatabaseOperation(DatabaseOperation.UPDATE);
			}
			modifiedKeywords.addAll(keyword.getChildren());
		}
		modifiedKeywords.add(keyword);
		keyword.setDatabaseOperation(DatabaseOperation.DELETE);
		
		return modifyKeywords(modifiedKeywords) != null;
	}
	
	/**
	 * Exclude a externalKeyword and all its children
	 * @param title
	 * @return
	 */
	public boolean excludeExternalKeyword(ExternalKeyword keyword){
		Collection<Keyword> modifiedKeywords = new HashSet<Keyword>();
		ExternalKeyword keywordToExclude = (ExternalKeyword) getDataObject(keyword);
		excludeExternalKeyword(keywordToExclude, modifiedKeywords);		
		return modifyKeywords(modifiedKeywords) != null;
	}
	private void excludeExternalKeyword(ExternalKeyword keyword, Collection<Keyword> modifiedKeyword){
		keyword.setExcluded(CommonValues.TRUE);
		keyword.setDatabaseOperation(DatabaseOperation.UPDATE);
		modifiedKeyword.add(keyword);
		if(keyword.getChildren() != null){
			for(ExternalKeyword child : keyword.getChildren()){
				excludeExternalKeyword(child, modifiedKeyword);
			}
		}
		
	}
	
	/**
	 * Fill database with a title
	 * @param title
	 * @return
	 */
	public List<ExternalKeyword> getAmazonKeywordsFromItem(String title, boolean createNotFound){
		ItemsService itemsService = new ItemsService(this);
		List<AmazonItem> amazonItems = itemsService.searchAmazonItems(null, title);
		List<ExternalKeyword> amazonKeywords = new ArrayList<ExternalKeyword>();
		boolean newKeywordCreated = false;
		for(AmazonItem amazonItem : amazonItems){
			if(amazonItem.getBrowseNodes() !=null){
				for(BrowseNode browseNode : amazonItem.getBrowseNodes()){
					ExternalKeyword amazonKeyword= getAmazonKeyword(browseNode);
					if(amazonKeyword == null){
						//Create a new keyword if asked to do so
						if(createNotFound){
							amazonKeyword = createAmazonKeyword(browseNode);
							newKeywordCreated = true;
						}
					}
					if(amazonKeyword != null && !amazonKeywords.contains(amazonKeyword)){
						amazonKeywords.add(amazonKeyword);
					}
				}
			}
		}
		//Commit if necessary (avability to commit at the end of transaction with setWillCommit(false)
		if(newKeywordCreated){
			sendForCommit();
		}
		return amazonKeywords;
	}
	
	/**
	 * Return an Amazon Keyword already copied in database
	 * @param amazonBrowseNode
	 * @return
	 */
	public ExternalKeyword getAmazonKeyword(BrowseNode amazonBrowseNode){
		Criteria req = createCriteria(ExternalKeyword.class)
				.add(Restrictions.eq("uid", amazonBrowseNode.getBrowseNodeId()))
				.add(Restrictions.eq("source", ExternalKeyword.AMAZON_SOURCE))
				.setFetchMode("", FetchMode.SELECT);
		return (ExternalKeyword)req.uniqueResult();
	}
	
	/**
	 * Create a new {@link ExternalKeyword} from Amazon {@link BrowseNode} (given by AmazonWebservice
	 * @param amazonBrowseNode
	 * @return
	 */
	public ExternalKeyword createAmazonKeyword(BrowseNode amazonBrowseNode){
		ExternalKeyword newAmazonKeyWord = new ExternalKeyword();
		newAmazonKeyWord.setUid(amazonBrowseNode.getBrowseNodeId());
		newAmazonKeyWord.setName(amazonBrowseNode.getName());
		newAmazonKeyWord.setSource(ExternalKeyword.AMAZON_SOURCE);
		newAmazonKeyWord.setExcluded(CommonUtils.toChar(false));
		//Search and build hierarchy
		BrowseNode parentNode =  
				(amazonBrowseNode.isIsCategoryRoot() == null || ! amazonBrowseNode.isIsCategoryRoot() ) &&
				amazonBrowseNode.getAncestors() != null && 
				amazonBrowseNode.getAncestors().getBrowseNode() != null &&
				! amazonBrowseNode.getAncestors().getBrowseNode().isEmpty() ? 
					amazonBrowseNode.getAncestors().getBrowseNode().get(0) : null;
		if(parentNode != null){
			
			ExternalKeyword amazonKeyWordAncestor = getAmazonKeyword(parentNode);
			if(amazonKeyWordAncestor == null){
				amazonKeyWordAncestor = createAmazonKeyword(parentNode);
			}
			newAmazonKeyWord.setAncestor(amazonKeyWordAncestor);
			newAmazonKeyWord.setFullname(
					amazonKeyWordAncestor.getFullname().concat(KEYWORD_ANCESTOR_SEPARATOR).concat(newAmazonKeyWord.getName()));
		}else{
			newAmazonKeyWord.setAncestor(null);
			newAmazonKeyWord.setFullname(amazonBrowseNode.getName());
		}
		newAmazonKeyWord.setDatabaseOperation(DatabaseOperation.INSERT);
		persist(newAmazonKeyWord);
		return newAmazonKeyWord;
	}
	
	
	/**
	 * Return {@link ExternalKeyword} without ancestor
	 * @return
	 */
	public List<ExternalKeyword> getOrphanExternalKeywords(){
		String hql = 
					" SELECT DISTINCT keyword FROM ExternalKeyword keyword ".concat(
					" WHERE keyword.excluded = '").concat(CommonValues.FALSE.toString()).concat("' and NOT EXISTS ").concat(
							"( FROM MainKeyword mainKeyword INNER JOIN  mainKeyword.externalKeywords linkedKeyword where linkedKeyword = keyword )");
		Query req = createQuery(hql);
		

		@SuppressWarnings("unchecked")
		List<ExternalKeyword> list = (List<ExternalKeyword>) req.list(); 
		return list; 
	}
	
	/**
	 * Return MainKeyword without ancestor
	 * @return
	 */
	public List<MainKeyword> getOrphanMainKeywords(){
		String hql = 
				" SELECT DISTINCT keyword FROM MainKeyword keyword ".concat(
				" LEFT JOIN FETCH keyword.children ").concat(
				" WHERE keyword.ancestor is null ");

		Query req = createQuery(hql);
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		for(MainKeyword keyword : list){
			Hibernate.initialize(keyword.getExternalKeywords());
		}
		return list;
	}
	
	/**
	 * Return {@link MainKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	public List<MainKeyword> getMainKeywordByIds(List<Integer> ids){
		String hql = "SELECT keyword FROM MainKeyword keyword WHERE keyword.id IN :list";
		Query req = createQuery(hql).setParameterList("list", ids);
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = (List<MainKeyword>)req.list();
		return list;
	}
	
	/**
	 * Return {@link ExternalKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	public List<ExternalKeyword> getExternalKeywordByIds(List<Integer> ids){
		String hql = "SELECT keyword FROM ExternalKeyword keyword WHERE keyword.id IN :list";
		Query req = createQuery(hql).setParameterList("list", ids);
		@SuppressWarnings("unchecked")
		List<ExternalKeyword> list = (List<ExternalKeyword>)req.list();
		return list;
	}
	
	/**
	 * Return all root menu entry (with no ancestor and marked as displayed in menu)
	 * @return
	 */
	public List<MainKeyword> getMenuRootEntries(){
		getSession().enableFilter(MENU_KEYWORD_ONLY_FILTER);
		Criteria req = createCriteria(MainKeyword.class)
				.add(Restrictions.isNull("ancestor"))
				.setFetchMode("children", FetchMode.SELECT)
				.setCacheable(true)
				.setCacheRegion("menu");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		getSession().disableFilter(MENU_KEYWORD_ONLY_FILTER);
		return list;
	}
	
	/**
	 * Return a Keyword with no name and an ID set to 0
	 * @return
	 */
	public static MainKeyword getRootForMenu(){
		MainKeyword rootKeyword = new MainKeyword();
		rootKeyword.setId(0);
		return rootKeyword;
	}
	
	/**
	 * Search a Keyword in database (External or not)
	 */
	public Collection<MainKeyword> searchMainKeyword(String name){
		String likeValue = name.replace(' ', '_').replace('-', '_');
		Criteria req = createCriteria(MainKeyword.class)
				.add(Restrictions.like("name", likeValue, MatchMode.ANYWHERE))
				.add(Restrictions.isNotNull("ancestor"))
				.setCacheable(true)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		return list;
	}
	
	public Collection<ExternalKeyword> searchExternalKeyword(String name, boolean createIfNotFound){
		List<ExternalKeyword> list = getAmazonKeywordsFromItem(name, createIfNotFound);
		
		return list;
	}
}
