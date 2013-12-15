package net.latroquette.common.database.data.item;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.file.FilesService;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.place.PlacesService;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.Services;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;
import net.latroquette.service.amazon.AmazonWService;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.database.spring.TransactionalUpdate;
import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.util.data.HibernateUtil;
import com.adi3000.common.util.optimizer.CommonValues;
import com.adi3000.common.util.security.Security;
import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearchRequest;

@Repository(value=Services.ITEMS_SERVICE)
public class ItemsServiceImpl extends AbstractDAO<Item> implements ItemsService{
	
	@Autowired
	private transient Parameters parameters;
	@Autowired
	private transient KeywordsService keywordsService;
	@Autowired
	private transient PlacesService placesService;
	@Autowired
	private transient FilesService filesService;
	@Autowired
	private transient AmazonWService amazonWService;
	
	/**
	 * @param filesService the filesService to set
	 */
	public void setFilesService(FilesService filesService) {
		this.filesService = filesService;
	}

	/**
	 * @param amazonWService the amazonWService to set
	 */
	public void setAmazonWService(AmazonWService amazonWService) {
		this.amazonWService = amazonWService;
	}

	/**
	 * @return the placesService
	 */
	public PlacesService getPlacesService() {
		return placesService;
	}

	/**
	 * @param placesService the placesService to set
	 */
	public void setPlacesService(PlacesService placesService) {
		this.placesService = placesService;
	}

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
	
	@TransactionalUpdate
	public void deleteImageFromItem(File image, Item item, User user){
		item.getImageList().remove(image);
		modifyItem(item, user);
		filesService.removeFile(image);
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
		
		AWSECommerceServicePortType port =  amazonWService.getPort();
		ItemSearchRequest itemSearch = new ItemSearchRequest();
		itemSearch.setKeywords(pattern.replaceAll("\\W", "+"));
		if(!AmazonWService.isValideCategory(cat)){
			cat = "All";
		}
		itemSearch.setSearchIndex(cat);
		
		//Retrieve images and title informations
		itemSearch.getResponseGroup().add("Small");
		itemSearch.getResponseGroup().add("Images");
		itemSearch.getResponseGroup().add("OfferSummary");
		itemSearch.getResponseGroup().add("BrowseNodes");
		
		List<com.amazon.ECS.client.jax.Items> results = amazonWService.itemSearch(port,itemSearch);
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
	public List<Item> searchItem(ItemFilter itemFilter, Integer page, boolean forAutocomplete){
		Query req = searchItemQuery(itemFilter, page, false,forAutocomplete);
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
	public Integer countItem(ItemFilter itemFilter){
		Query req = searchItemQuery(itemFilter, CommonValues.ERROR_OR_INFINITE, true, false);
		Integer nbItems = ((Long)req.iterate().next()).intValue();
		return nbItems;
	}
	
	public void setFiltersFromParameter(ItemFilter itemFilter, Map<String, String> parameterMap, User user){
		boolean mayHaveStatusFilterPrivilege = Security.checkLoginState(user);
		itemFilter.setFilters(parameterMap, Security.checkLoginState(user));
		//Control filters
		if(itemFilter.getItemStatusId() != null){
			if(!mayHaveStatusFilterPrivilege){
				//anonymous user, have no right to filter by privileges
				itemFilter.setItemStatusId(null);
			}else if(user.getRole() == null || !user.getRole().getValidateItems()){
				if(	ItemStatus.DRAFT.getValue().equals(itemFilter.getItemStatusId()) ||
					ItemStatus.FINISHED.getValue().equals(itemFilter.getItemStatusId()) ||
					ItemStatus.EXPIRED.getValue().equals(itemFilter.getItemStatusId()) ){
					//Force filter to search under owner id only for its drafts
					itemFilter.setOwnerId(user.getId());
				}else{
					//If no right to validate item, then remove unauthorized filter
					itemFilter.setItemStatusId(null);
				}
			}
		}
		
	}
	
	@Transactional(readOnly=true)
	private Query searchItemQuery(ItemFilter itemFilter, Integer page, boolean countOnly, boolean forAutocomplete){
		//Optimize when just checking for nbResult
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ; 
		String field = null;
		Set<Keyword> relatedKeywords = null;
		//Get keyword if needed;
		MainKeyword keyword = itemFilter.getKeywordId() != null ?  
					keywordsService.getKeywordById(itemFilter.getKeywordId()) : null;
		//If no status id set, default request should grab only activated annonces
		Integer itemStatusId = itemFilter.getItemStatusId() != null 
					? itemFilter.getItemStatusId() : ItemStatus.APPROUVED.getValue();
		//Anyway hide blocked user
		Integer minUserStatusLevel = itemFilter.getItemStatusId() != null ? 
				User.NOT_VALIDATED : User.NEW_USER_VALIDATED ;
					
		//Are we counting ? If true, set select field to, or get item field 
		if(!countOnly){
			field = "item";
			nbResultToLoad = parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
			if(page == null){
				page = Integer.valueOf(1);
			}
		}else{
			 field = "count(item)";
		}
		StringBuffer restriction = new StringBuffer("where 1=1 ");
		
		//-------------------------------------------
		// Setting the search filter for searchString
		//-------------------------------------------
		//Pattern
		if(StringUtils.isNotEmpty(itemFilter.getPattern())){
			String index = itemFilter.isSearchOnDescription() ? "item.title || ' ' || item.description " : "item.title " ;
			restriction.append("and fulltextsearch('french', ").append(index).append(", :search ) = true ");
		}

		//Keyword
		if(keyword != null){
			restriction.append("and keyword in (:relatedKeywords) ");
		}

		//Place
		if(itemFilter.getPlaceId() != null){
			restriction.append("and ( place is null ");
			if(itemFilter.getDistance() > 0){
				restriction
					.append("or exists (from Place place2 where place2.id = :placeId ")
						.append(" and abs(place2.longitude - place.longitude ) < :longitudeRadix")
						.append(" and abs(place2.latitude - place.latitude ) < :latitudeRadix )");
			}else{
				restriction.append("or placeId = :placeId ");
			}
			restriction.append(" ) ");
		}
		//Owner
		if(itemFilter.getOwnerId() != null){
			restriction.append("and user.id = :ownerId ");
		}
		//Status
		restriction.append("and item.statusId = :itemStatusId ");
		restriction.append("and item.user.loginState >= :minUserStatusLevel ");
		//--------------------
		// Building the query
		//--------------------
		String sql = 
				" SELECT "
					.concat(field)
					.concat(" FROM Item item left join item.keywordList keyword ")
					.concat(" INNER JOIN item.user user ")
					.concat(" LEFT JOIN item.keywordList keyword ")
					.concat(" LEFT JOIN item.user.place place ")
					.concat(restriction.toString())
					.concat(countOnly ? "" : " order by item.creationDate");
		Query req = createQuery(sql);
		
		//-----------------------------
		// Setting the query parameters
		//------------------------------
		//Compose the search pattern
		if(StringUtils.isNotEmpty(itemFilter.getPattern())){
			String searchPattern = itemFilter.getPattern().replaceAll("\\W+", " & ").replaceAll(" & $","");
			if(forAutocomplete){
				searchPattern = searchPattern.concat(":*");
			}
			req.setString("search", searchPattern);
		}
		//Make a keyword filter
		if(keyword != null){
			relatedKeywords = new HashSet<>();
			relatedKeywords = keywordsService.getAllChildrenOf(keyword, relatedKeywords);
			req.setParameterList("relatedKeywords", relatedKeywords);
		}
		//Compute radix from kilometer radix
		if(itemFilter.getPlaceId() != null){
			req.setInteger("placeId", itemFilter.getPlaceId());
			if(itemFilter.getDistance() > 0){
				Point2D.Double place = placesService.getRadixByPlaceId( itemFilter.getPlaceId(), itemFilter.getDistance());
				req.setDouble("longitudeRadix", place.getX());
				req.setDouble("latitudeRadix", place.getY());
			}
		}
		//User Id filter
		if(itemFilter.getOwnerId() != null){
			req.setInteger("ownerId", itemFilter.getOwnerId());	
		}
		//filter by Item status
		req.setInteger("itemStatusId", itemStatusId);
		//filter by User status
		req.setInteger("minUserStatusLevel", minUserStatusLevel);
		if(!countOnly){
			req = CommonUtil.setCriteriaPage(req, page, nbResultToLoad);
		}
		return req;
	}
	
	@Transactional(readOnly=true)
	public List<Item> searchItemByStatus(Integer page, ItemStatus itemStatus){
		Criteria req = createCriteria(Item.class)
				.add(Restrictions.eq("statusId", itemStatus.getValue()));
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ; 
		nbResultToLoad = parameters.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
		req = CommonUtil.setCriteriaPage(req, page, nbResultToLoad);
		@SuppressWarnings("unchecked")
		List<Item> items = req.list();
		return items;
	}
	
	@TransactionalUpdate
	public void updateItem(Item item){
		modify(item);
	}
	
}
