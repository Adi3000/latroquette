package net.latroquette.rest.search;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemsService;

import org.apache.commons.lang.StringUtils;

/**
 * @author adi
 *
 */
@Path("/search/item")
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ItemSearch {
	
	
	@GET
	public GenericEntity<List<Item>> getItems (@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle, @QueryParam("p") String page  ){
		ItemsService itemsService = new ItemsService();
		Integer pageNum = StringUtils.isNotEmpty(page)  ? Integer.valueOf(page) : Integer.valueOf(1);
		List<Item> itemsFound = itemsService.searchItem(pattern, !Boolean.valueOf(onlyTitle), pageNum);
		itemsService.closeSession();
		return new GenericEntity<List<Item>>(itemsFound) {};
	}
	
	@GET
	@Path("/count")
	public GenericEntity<Integer> getItems (@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle){
		ItemsService itemsService = new ItemsService();
		Integer itemsFound = itemsService.countItem(pattern, !Boolean.valueOf(onlyTitle));
		itemsService.closeSession();
		return new GenericEntity<Integer>(itemsFound) {};
	}
}
