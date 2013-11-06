package net.latroquette.common.database.data.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;
import net.latroquette.service.amazon.AmazonWServiceClient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.util.data.HibernateUtil;
import com.adi3000.common.util.optimizer.CommonValues;
import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearchRequest;

@Repository(value=Services.ITEMS_SERVICE)
public class ItemsServiceImpl extends AbstractDAO<Item> implements ItemsService{
	
	@Autowired
	private transient Parameters parameters;
	@Autowired
	private transient KeywordsService keywordsService;
	
	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public ItemsServiceImpl() {
		super();
	}
	
	/**
	 * Search an item via Amazon Webservice
	 * @param cat
	 * @param pattern
	 * @return
	 */
	@Transactional
	public List<AmazonItem> searchAmazonItems(String cat, String pattern){
		List<AmazonItem> listItem = new ArrayList<AmazonItem>();
		
		AWSECommerceServicePortType port =  AmazonWServiceClient.CLIENT.getPort();
		ItemSearchRequest itemSearch = new ItemSearchRequest();
		itemSearch.setKeywords(pattern.replaceAll("\\W", "+"));
		if(!AmazonWServiceClient.isValideCategory(cat)){
			cat = "All";
		}
		itemSearch.setSearchIndex(cat);
		
		//Retrieve images and title informations
		itemSearch.getResponseGroup().add("Small");
		itemSearch.getResponseGroup().add("Images");
		itemSearch.getResponseGroup().add("BrowseNodes");
		
		List<com.amazon.ECS.client.jax.Items> results = AmazonWServiceClient.itemSearch(port,itemSearch);
		if(results != null && !results.isEmpty()){
			
			for(com.amazon.ECS.client.jax.Items result : results){
				List<com.amazon.ECS.client.jax.Item> itemsResults = result.getItem(); 
				if( itemsResults != null && !itemsResults.isEmpty()){
					for(com.amazon.ECS.client.jax.Item item : itemsResults){
						AmazonItem amazonItem = new AmazonItem(item);
						listItem.add(amazonItem);
					}
				}
			}
		}
		
		return listItem;
	}
	
	/**
	 * Get an item by its Id
	 * @param itemId
	 * @return
	 */
	@Transactional(readOnly=true)
	public Item getItemById(Integer itemId){
		Item item = (Item)getSession().get(Item.class,	itemId);
		HibernateUtil.initialize(item.getImageList());
		HibernateUtil.initialize(item.getKeywordList());
		HibernateUtil.initialize(item.getExternalKeywordList());
		return item;
	}
	
	/**
	 * Modify an item and update its user modification and its update date
	 * @param item
	 * @param user
	 * @return
	 */
	@Transactional
	public Item modifyItem(Item item, User user){
		item.setUser(user);
		item.setUpdateDate(CommonUtil.getTimestamp());
		if(item.getId() == null){
			item.setCreationDate(CommonUtil.getTimestamp());
		}
		return super.modifyDataObject(item);
	}
	
	/**
	 * Search an item in database, return only {@link ParameterName}.NB_RESULT_TO_LOAD
	 * @param searchString
	 * @param searchOnDescription
	 * @param page
	 * @param countOnly
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Item> searchItem(String searchString, boolean searchOnDescription, int page, MainKeyword keyword, boolean forAutocomplete){
		Query req = searchItemQuery(searchString, searchOnDescription, page, keyword,false,forAutocomplete);
		@SuppressWarnings("unchecked")
		List<Item> items = (List<Item>)req.list();
		for(Item item : items){
			HibernateUtil.initialize(item.getImageList());
		}
		return items;
	}
	
	/**
	 * Return the number of item which match with the searchString request in database
	 * @param searchString
	 * @param searchOnDescription
	 * @param page
	 * @param countOnly
	 * @return
	 */
	@Transactional(readOnly=true)
	public Integer countItem(String searchString, boolean searchOnDescription, MainKeyword keyword){
		Query req = searchItemQuery(searchString, searchOnDescription, CommonValues.ERROR_OR_INFINITE, keyword,true, false);
		Integer nbItems = ((Long)req.iterate().next()).intValue();
		return nbItems;
	}
	
	@Transactional(readOnly=true)
	private Query searchItemQuery(String searchString, boolean searchOnDescription, int page, MainKeyword keyword,boolean countOnly, boolean forAutocomplete){
		//Optimize when just checking for nbResult
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ; 
		int cursor = CommonValues.ERROR_OR_INFINITE;
		Set<Keyword> relatedKeywords = null;
		String field = "count(item)";
		if(!countOnly){
			field = "item";
			nbResultToLoad = parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
			cursor = nbResultToLoad * (page -1);
		}
		StringBuffer restriction = new StringBuffer("where 1=1 ");
		//Setting the search filter for searchString
		if(StringUtils.isNotEmpty(searchString)){
			String index = searchOnDescription ? "item.title || ' ' || item.description " : "item.title " ;
			restriction.append("and fulltextsearch('french', ").append(index).append(", :search ) = true ");
		}
		if(keyword != null){
			restriction.append("and keyword in (:relatedKeywords) ");
		}
		String sql = 
				" SELECT "
					.concat(field)
					.concat(" FROM Item item left join item.keywordList keyword ")
					.concat(restriction.toString())
					.concat(countOnly ? "" : " order by item.creationDate");
		Query req = createQuery(sql);
		//Compose the search pattern
		if(StringUtils.isNotEmpty(searchString)){
			String searchPattern = searchString.replaceAll("\\W+", " & ").replaceAll(" & $","");
			if(forAutocomplete){
				searchPattern = searchPattern.concat(":*");
			}
			req.setString("search", searchPattern);
		}
		if(keyword != null){
			relatedKeywords = new HashSet<>();
			relatedKeywords = keywordsService.getAllChildrenOf(keyword, relatedKeywords);
			req.setParameterList("relatedKeywords", relatedKeywords);
		}
		
		if(!countOnly){
			req
				.setFirstResult(cursor)
				.setMaxResults(nbResultToLoad);
		}
		return req;
	}
	
}
