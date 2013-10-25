package net.latroquette.rest.search;

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

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author adi
 *
 */
@Path("/search/item")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@WebService
public class ItemSearch extends SpringBeanAutowiringSupport {
	
	@Autowired
	private transient ItemsService itemsService;
	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}

	@GET
	@WebMethod
	public GenericEntity<List<Item>> getItems (@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle, @QueryParam("p") String page  ){
		Integer pageNum = StringUtils.isNotEmpty(page)  ? Integer.valueOf(page) : Integer.valueOf(1);
		List<Item> itemsFound = itemsService.searchItem(pattern, !Boolean.valueOf(onlyTitle), pageNum);
		return new GenericEntity<List<Item>>(itemsFound) {};
	}
	
	@GET
	@Path("/count")
	@WebMethod
	public GenericEntity<Integer> getItems (@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle){
		Integer itemsFound = itemsService.countItem(pattern, !Boolean.valueOf(onlyTitle));
		return new GenericEntity<Integer>(itemsFound) {};
	}
}
