package net.latroquette.common.database.data.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.latroquette.service.amazon.AmazonWServiceClient;

import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearchRequest;

public class Items/* extends DatabaseSession*/{
	
	
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
}
