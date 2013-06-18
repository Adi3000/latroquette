package net.latroquette.common.database.data.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.ItemsService;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.adi3000.common.database.hibernate.IDatabaseConstants;
import com.adi3000.common.database.hibernate.data.AbstractDAO;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.util.optimizer.CommonValues;
import com.amazon.ECS.client.jax.BrowseNode;

public class KeywordsService extends AbstractDAO<Keyword> {

	public static final String KEYWORD_ANCESTOR_SEPARATOR = " > ";
	
	public MainKeyword getKeywordById(Integer id){
		return (MainKeyword)super.getDataObjectById(id, MainKeyword.class);
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
		if(commit()){
			return keyword;
		}else{
			return null;
		}
	}
	public Collection<Keyword> modifyKeywords(Collection<Keyword> keyword){
		return modifyDataObject(keyword, true);
	}
	
	public boolean deleteKeyword(MainKeyword keyword){
		return deleteDataObject(keyword);
	}
	
	public List<ExternalKeyword> getAmazonKeywordsForTitle(String title){
		List<AmazonItem> amazonItems = ItemsService.searchAmazonItems(null, title);
		List<ExternalKeyword> amazonKeywords = new ArrayList<ExternalKeyword>();
		for(AmazonItem amazonItem : amazonItems){
			if(amazonItem.getBrowseNodes() !=null){
				for(BrowseNode browseNode : amazonItem.getBrowseNodes()){
					ExternalKeyword amazonKeyword= getAmazonKeyword(browseNode);
					if(amazonKeyword == null){
						amazonKeyword = createAmazonKeyword(browseNode);
					}
					if(!amazonKeywords.contains(amazonKeyword)){
						amazonKeywords.add(amazonKeyword);
					}
				}
			}
		}
		
		if(true){
			commit();
		}else{
			//TODO remove commit : just for testing
		}
		return amazonKeywords;
	}
	
	public ExternalKeyword getAmazonKeyword(BrowseNode amazonBrowseNode){
		Criteria req = this.session.createCriteria(ExternalKeyword.class)
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
		newAmazonKeyWord.setDatabaseOperation(IDatabaseConstants.INSERT);
		persist(newAmazonKeyWord);
		return newAmazonKeyWord;
	}
	
	private List<? extends Keyword> getOrphanKeywords(Class<? extends Keyword> keywordClass){
		
		Criteria req = this.session.createCriteria(keywordClass)
				.add(Restrictions.isNull("ancestor"))
				.setFetchMode("", FetchMode.JOIN);
		@SuppressWarnings("unchecked")
		List<? extends Keyword> list = req.list();
		return list;
	}
	
	/**
	 * Return {@link ExternalKeyword} without ancestor
	 * @return
	 */
	public List<ExternalKeyword> getOrphanExternalKeywords(){
		String sql = 
					" SELECT {keyword.*} FROM external_keywords {keyword} ".concat(
					" WHERE {keyword}.ext_keyword_excluded = '").concat(CommonValues.FALSE.toString()).concat("' and NOT EXISTS ").concat(
							"(SELECT 1 FROM external_keywords_relationship krl where {keyword}.ext_keyword_id = krl.ext_keyword_id )");
		Query req = this.session.createSQLQuery(sql).addEntity("keyword",ExternalKeyword.class);
		@SuppressWarnings("unchecked")
		List<ExternalKeyword> list = (List<ExternalKeyword>) req.list(); 
		return list; 
	}
	
	/**
	 * Return MainKeyword without ancestor
	 * @return
	 */
	public List<MainKeyword> getOrphanMainKeywords(){
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = (List<MainKeyword>) getOrphanKeywords(MainKeyword.class); 
		return list;
	}
	
	/**
	 * Return {@link MainKeyword} list filtered by an Ids list
	 * @param ids
	 * @return
	 */
	public List<MainKeyword> getMainKeywordByIds(List<Integer> ids){
		String hsql = "SELECT keyword FROM MainKeyword keyword WHERE keyword.id IN :list";
		Query req = this.session.createQuery(hsql).setParameterList("list", ids);
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
		String hsql = "SELECT keyword FROM ExternalKeyword keyword WHERE keyword.id IN :list";
		Query req = this.session.createQuery(hsql).setParameterList("list", ids);
		@SuppressWarnings("unchecked")
		List<ExternalKeyword> list = (List<ExternalKeyword>)req.list();
		return list;
	}
	
	/**
	 * Return all root menu entry (with no ancestor and marked as displayed in menu)
	 * @return
	 */
	public List<MainKeyword> getMenuRootEntries(){
		Criteria req = this.session.createCriteria(MainKeyword.class)
				.add(Restrictions.eq("inMenu", CommonValues.TRUE))
				.add(Restrictions.isNull("ancestor"))
				.setCacheable(true)
				.setCacheRegion("menu");
		@SuppressWarnings("unchecked")
		List<MainKeyword> list = req.list();
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

}
