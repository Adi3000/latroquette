package net.latroquette.common.database.data.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.optimizer.CommonValues;
import com.amazon.ECS.client.jax.BrowseNode;

@Repository(value=Services.KEYWORDS_SERVICE)
public class KeywordsServiceImpl extends AbstractDAO<Keyword> implements KeywordsService{

	public KeywordsServiceImpl() {
		super(Keyword.class);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -351992532829047773L;
	@Inject
	private transient ItemsService itemsService;
	@Inject
	private transient Parameters parameters;
	@Inject
	private transient MainKeywordDAO mainKeywordDAO;
	@Inject
	private transient ExternalKeywordDAO externalKeywordDAO;
	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}
	
	
	@Transactional(readOnly=true)
	public MainKeyword getKeywordById(Integer id){
		return getKeywordById(id, false);
	}
	@Transactional(readOnly=true)
	public MainKeyword getKeywordById(Integer id, boolean deep){
		MainKeyword keyword = null;
		if(deep){
			Criteria req = getSession().createCriteria(MainKeyword.class)
					.add(Restrictions.eq("id",id))
					.setFetchMode("children", FetchMode.SELECT);
			keyword = (MainKeyword) req.uniqueResult();
			keyword.initializeRecursively();
			keyword.initializeAncestors();
			Hibernate.initialize(keyword.getExternalKeywords());
			//For admin keywords : only one level is needed
			for(MainKeyword child : keyword.getChildren()){
				Hibernate.initialize(child.getExternalKeywords());
			}
		}else{
			keyword = mainKeywordDAO.get(id);
		}		
		return keyword; 
	}
	
	@Transactional(readOnly=true)
	public MainKeyword getKeyword(MainKeyword keyword){
		return (MainKeyword)super.getDataObject(keyword);
	}
	
	@Transactional(readOnly=false)
	public Keyword modifyKeyword(Keyword keyword){
		return modifyDataObject(keyword);
	}
	
	@Transactional(readOnly=false)
	public MainKeyword modifyKeyword(MainKeyword keyword, MainKeyword parent){
		modify(keyword);
		if(parent != null){
			modify(parent);
		}
		return keyword;
	}
	
	@Transactional(readOnly=false)
	public Collection<Keyword> modifyKeywords(Collection<Keyword> keyword){
		return modifyDataObject(keyword);
	}
	
	/**
	 * Delete a keyword removing all children and updating children for their relation chips
	 * @param keyword
	 * @return
	 */
	@Transactional(readOnly=false)
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
	@Transactional(readOnly=false)
	public boolean excludeExternalKeyword(ExternalKeyword keyword){
		Collection<Keyword> modifiedKeywords = new HashSet<Keyword>();
		ExternalKeyword keywordToExclude = (ExternalKeyword) getDataObject(keyword);
		excludeExternalKeyword(keywordToExclude, modifiedKeywords);		
		return modifyKeywords(modifiedKeywords) != null;
	}
	
	private void excludeExternalKeyword(ExternalKeyword keyword, Collection<Keyword> modifiedKeyword){
		keyword.setExcluded(Boolean.TRUE);
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
	@Transactional(readOnly=false)
	public List<ExternalKeyword> getAmazonKeywordsFromItem(String title, boolean createNotFound){
		//TODO replace searchAmazonItem by a SearchCategorie, this will be more correct
		List<AmazonItem> amazonItems = itemsService.searchAmazonItems(null, title);
		List<ExternalKeyword> amazonKeywords = new ArrayList<ExternalKeyword>();
		for(AmazonItem amazonItem : amazonItems){
			if(amazonItem.getBrowseNodes() !=null){
				for(BrowseNode browseNode : amazonItem.getBrowseNodes()){
					ExternalKeyword amazonKeyword= getAmazonKeyword(browseNode);
					if(amazonKeyword == null){
						//Create a new keyword if asked to do so
						if(createNotFound){
							amazonKeyword = createAmazonKeyword(browseNode);
						}
					}
					if(amazonKeyword != null && !amazonKeyword.getExcluded() && !amazonKeywords.contains(amazonKeyword) ){
						amazonKeywords.add(amazonKeyword);
					}
				}
			}
		}
		return amazonKeywords;
	}
	
	/**
	 * Return an Amazon Keyword already copied in database
	 * @param amazonBrowseNode
	 * @return
	 */
	@Transactional(readOnly=true)
	public ExternalKeyword getAmazonKeyword(BrowseNode amazonBrowseNode){
		Criteria req = createCriteria(ExternalKeyword.class)
				.add(Restrictions.eq("uid", amazonBrowseNode.getBrowseNodeId()))
				.add(Restrictions.eq("source", KeywordSource.AMAZON_SOURCE.getSourceId()))
				.setFetchMode("", FetchMode.SELECT)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		return (ExternalKeyword)req.uniqueResult();
	}
	
	/**
	 * Create a new {@link ExternalKeyword} from Amazon {@link BrowseNode} (given by AmazonWebservice
	 * @param amazonBrowseNode
	 * @return
	 */
	@Transactional(readOnly=false)
	public ExternalKeyword createAmazonKeyword(BrowseNode amazonBrowseNode){
		ExternalKeyword newAmazonKeyWord = new ExternalKeyword();
		newAmazonKeyWord.setUid(amazonBrowseNode.getBrowseNodeId());
		newAmazonKeyWord.setName(amazonBrowseNode.getName());
		newAmazonKeyWord.setSource(ExternalKeyword.AMAZON_SOURCE);
		newAmazonKeyWord.setExcluded(Boolean.FALSE);
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
	@Transactional(readOnly=true)
	public List<ExternalKeyword> getOrphanExternalKeywords(){
		String hql = 
					" SELECT DISTINCT keyword FROM ExternalKeyword keyword ".concat(
					" WHERE keyword.excluded = '").concat(CommonValues.FALSE.toString()).concat("' and NOT EXISTS ").concat(
							"( FROM MainKeyword mainKeyword INNER JOIN  mainKeyword.externalKeywords linkedKeyword where linkedKeyword = keyword )");
		Query req = createQuery(hql)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		

		@SuppressWarnings("unchecked")
		List<ExternalKeyword> list = (List<ExternalKeyword>) req.list(); 
		return list; 
	}
	
	/**
	 * Return MainKeyword without ancestor
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<MainKeyword> getOrphanMainKeywords(){
		String hql = 
				" SELECT DISTINCT keyword FROM MainKeyword keyword ".concat(
				" LEFT JOIN FETCH keyword.children ").concat(
				" WHERE keyword.ancestor is null ");

		Query req = createQuery(hql)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
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
	@Transactional(readOnly=true)
	public List<MainKeyword> getMainKeywordByIds(List<Integer> ids){
		String hql = "SELECT keyword FROM MainKeyword keyword WHERE keyword.id IN :list";
		Query req = createQuery(hql).setParameterList("list", ids)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = (List<MainKeyword>)req.list();
		return list;
	}
	
	/**
	 * Return {@link ExternalKeyword} list filtered by an Id
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public ExternalKeyword getExternalKeywordById(Integer id){
		ExternalKeyword keyword = externalKeywordDAO.get(id);
		return keyword;
	}

	/**
	 * Return {@link ExternalKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
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
	@Transactional(readOnly=true)
	public List<MainKeyword> getMenuRootEntries(){
		getSession().enableFilter(MENU_KEYWORD_ONLY_FILTER);
		Criteria req = createCriteria(MainKeyword.class)
				.add(Restrictions.isNull("ancestor"))
				.setFetchMode("children", FetchMode.SELECT)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		for(MainKeyword keyword : list){
			keyword.initializeRecursively();
		}
		getSession().disableFilter(MENU_KEYWORD_ONLY_FILTER);
		return list;
	}
	/**
	 * Return all root categories entry (with no ancestor and marked as displayed in menu)
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<MainKeyword> getRootCategoriesEntries(){
		getSession().enableFilter(MENU_KEYWORD_EXCLUDE_SYNONYME_FILTER);
		Criteria req = createCriteria(MainKeyword.class)
				.add(Restrictions.isNull("ancestor"))
				.setFetchMode("children", FetchMode.SELECT)
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		for(MainKeyword keyword : list){
			keyword.initializeRecursively();
		}
		getSession().disableFilter(MENU_KEYWORD_EXCLUDE_SYNONYME_FILTER);
		return list;
	}
	
	/**
	 * Return a Keyword with no name and an ID set to 0
	 * @return
	 */
	@Transactional(readOnly=true)
	public MainKeyword getRootForMenu(){
		MainKeyword rootKeyword = new MainKeyword();
		rootKeyword.setId(0);
		return rootKeyword;
	}
	
	/**
	 * Search a Keyword in database (External or not)
	 */
	@Transactional(readOnly=true)
	public List<Keyword> searchKeyword(String name){
		String likeValue = name.replaceAll("\\W", "_");
		Criteria req = getSession().createCriteria(Keyword.class)
				.add(Restrictions.like("name", likeValue, MatchMode.ANYWHERE).ignoreCase())
				.add(Restrictions.isNotNull("ancestor"))
				.setMaxResults(parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD))
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<Keyword> list = req.list();
		return list;
	}
	/**
	 * Search a Keyword in database 
	 */
	@Transactional(readOnly=true)
	public Collection<MainKeyword> searchMainKeyword(String name){
		String likeValue = name.replace(' ', '_').replace('-', '_');
		Criteria req = createCriteria(MainKeyword.class)
				.add(Restrictions.like("name", likeValue, MatchMode.ANYWHERE))
				.add(Restrictions.isNotNull("ancestor"))
				.setCacheable(true)
				.setCacheMode(CacheMode.NORMAL)
				.setCacheRegion("keywords");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public Collection<ExternalKeyword> searchExternalKeyword(String name, boolean createIfNotFound){
		List<ExternalKeyword> list = getAmazonKeywordsFromItem(name, createIfNotFound);
		
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Keyword> getDirectChildrenOf(Integer id, KeywordType keywordType){
		Keyword parentKeyword = null;
		List<Keyword> children = new ArrayList<>();
		switch (keywordType) {
			case EXTERNAL_KEYWORD:
					parentKeyword = (Keyword) externalKeywordDAO.get(id);
				break;
			case MAIN_KEYWORD:
					getSession().enableFilter(MENU_KEYWORD_EXCLUDE_SYNONYME_FILTER);
					parentKeyword = (Keyword) mainKeywordDAO.get(id);
					getSession().enableFilter(MENU_KEYWORD_EXCLUDE_SYNONYME_FILTER);
					children.addAll(((MainKeyword)parentKeyword).getExternalKeywords());
				break;
			default:
				return children;
		}
		children.addAll(0, parentKeyword.getChildren());
		return children;
	}
	
	
	public Set<Keyword> getAllChildrenOf(Keyword keyword, Set<Keyword> keywordSet){
		keywordSet.add(keyword);
		if( keyword.getChildren() != null){
			for(Keyword children : keyword.getChildren()){
				getAllChildrenOf(children, keywordSet);
			}	
		}
		return keywordSet;
	}
}
