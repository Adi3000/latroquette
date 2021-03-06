package net.latroquette.rest.providers;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.ItemsService;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author adi
 *
 */
@Path("/providers/amazon")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@WebService
public class AmazonProducts extends SpringBeanAutowiringSupport{
	
	private ItemsService items;
	/**
	 * @param items the items to set
	 */
	public void setItems(ItemsService items) {
		this.items = items;
	}

	@GET
	@WebMethod
	public GenericEntity<List<AmazonItem>> getItems (@QueryParam("term") String pattern, 
			@QueryParam("cat") String category ){
		List<AmazonItem> itemsFound = items.searchAmazonItems(category, pattern);
		return new GenericEntity<List<AmazonItem>>(itemsFound) {};
	}

}
