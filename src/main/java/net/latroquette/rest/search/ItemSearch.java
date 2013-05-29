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
import net.latroquette.common.database.data.item.Items;

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
		Items items = new Items();
		Integer pageNum = StringUtils.isNotEmpty(page)  ? Integer.valueOf(page) : Integer.valueOf(1);
		List<Item> itemsFound = items.searchItem(pattern, !Boolean.valueOf(onlyTitle), pageNum, false);
		items.closeSession();
		return new GenericEntity<List<Item>>(itemsFound) {};
	}
	
	@GET
	@Path("/count")
	public GenericEntity<Integer> getItems (@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle){
		Items items = new Items();
		Integer itemsFound = items.countItem(pattern, !Boolean.valueOf(onlyTitle));
		items.closeSession();
		return new GenericEntity<Integer>(itemsFound) {};
	}
}
