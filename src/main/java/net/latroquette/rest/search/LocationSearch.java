/**
 * 
 */
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

import net.latroquette.common.database.data.location.Location;
import net.latroquette.common.database.data.location.LocationsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author adi
 *
 */
@Path("/search/locations")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@WebService
public class LocationSearch extends SpringBeanAutowiringSupport{
	
	@Autowired
	private transient LocationsService locationsService;
	
	/**
	 * @param locationsService the locationsService to set
	 */
	public void setLocationsService(LocationsService locationsService) {
		this.locationsService = locationsService;
	}

	@GET
	@WebMethod
	public GenericEntity<List<Location>> getLocations (@QueryParam("term") String pattern){
		List<Location> locationsFound = locationsService.getLocationByString(pattern);
		return new GenericEntity<List<Location>>(locationsFound) {};
	}
}
