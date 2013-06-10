package net.latroquette.common.database.data.item;

import java.util.ArrayList;
import java.util.List;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;
import net.latroquette.service.amazon.AmazonWServiceClient;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.adi3000.common.database.hibernate.data.AbstractDAO;
import com.adi3000.common.database.hibernate.session.DatabaseSession;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.util.optimizer.CommonValues;
import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearchRequest;

public class ItemsService extends AbstractDAO<Item>{
	
	
	public ItemsService() {
		super();
	}
	public ItemsService(boolean initDbSession) {
		super(initDbSession);
	}
	public ItemsService(DatabaseSession db) {
		super(db);
	}
	public static List<AmazonItem> searchAmazonItems(String cat, String pattern){
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
	
	public Item getItemById(Integer itemId){
		Criteria req = this.session.createCriteria(Item.class)
				.add(Restrictions.eq("id", itemId))
				.setFetchMode("imageList", FetchMode.JOIN);
		return (Item)req.uniqueResult();
	}
	
	public Item modifyItem(Item item, User user){
		item.setUser(user);
		item.setUpdateDate(CommonUtils.getTimestamp());
		if(item.getId() == null){
			item.setCreationDate(CommonUtils.getTimestamp());
		}
		return super.modifyDataObject(item);
	}
	
	public List<Item> searchItem(String searchString, boolean searchOnDescription, int page, boolean countOnly){
		Query req = searchItemQuery(searchString, searchOnDescription, page, countOnly);
		@SuppressWarnings("unchecked")
		List<Item> items = (List<Item>)req.list();
		return items;
	}
	public Integer countItem(String searchString, boolean searchOnDescription){
		Query req = searchItemQuery(searchString, searchOnDescription, CommonValues.ERROR_OR_INFINITE, true);
		Integer nbItems = ((Long)req.iterate().next()).intValue();
		return nbItems;
	}
	
	private Query searchItemQuery(String searchString, boolean searchOnDescription, int page, boolean countOnly){
		//Optimize when just checking for nbResult
		Parameters parametersService = null;
		int nbResultToLoad = CommonValues.ERROR_OR_INFINITE ; 
		int cursor = CommonValues.ERROR_OR_INFINITE;
		String field = "count(item)";
		if(!countOnly){
			field = "item";
			parametersService = new Parameters();
			nbResultToLoad = parametersService.getIntValue(ParameterName.NB_RESULT_TO_LOAD);
			cursor = nbResultToLoad * (page -1);
		}
		String searchPattern = searchString.replaceAll("\\W", " & ");
		String index = searchOnDescription ? "item.title || ' ' || item.description " : "item.title " ;
		String sql = 
				" SELECT ".concat(field).concat(" FROM Item item ").concat(
				" WHERE fulltextsearch('french', ").concat(index).concat(", :search ) = true").concat(
				countOnly ? "" : " order by item.creationDate");
		Query req = this.session.createQuery(sql)
						.setString("search", searchPattern);
		
		if(!countOnly){
			req
				.setFirstResult(cursor)
				.setMaxResults(nbResultToLoad);
		}
		return req;
	}
	
}
