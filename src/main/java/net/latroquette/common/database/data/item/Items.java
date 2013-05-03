package net.latroquette.common.database.data.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDAO;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.CommonUtils;
import net.latroquette.service.amazon.AmazonWServiceClient;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearchRequest;

public class Items extends AbstractDAO{
	
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
			item.setDatabaseOperation(IDatabaseConstants.INSERT);
		}else{
			item.setDatabaseOperation(IDatabaseConstants.UPDATE);
		}
		persist(item);
		if(!commit()){
			return null;
		}else{
			return item;
		}
	}
}
