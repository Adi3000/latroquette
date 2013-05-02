package net.latroquette.rest.providers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.Items;

/**
 * @author adi
 *
 */
@Path("/providers/amazon")
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AmazonProducts {
	
	
	@GET
	public GenericEntity<List<AmazonItem>> getLocations (@QueryParam("term") String pattern, 
			@QueryParam("cat") String category ){
		Items items = new Items();
		@SuppressWarnings("static-access")
		List<AmazonItem> itemsFound = items.searchAmazonItems(category, pattern);
		return new GenericEntity<List<AmazonItem>>(itemsFound) {};
	}

}
