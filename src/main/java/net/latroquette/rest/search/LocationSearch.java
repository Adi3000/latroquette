/**
 * 
 */
package net.latroquette.rest.search;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import net.latroquette.common.database.data.location.Location;
import net.latroquette.common.database.data.location.LocationsService;

/**
 * @author adi
 *
 */
@Path("/search/locations")
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class LocationSearch {
	
	@Autowired
	private LocationsService locationsService;
	
	/**
	 * @param locationsService the locationsService to set
	 */
	public void setLocationsService(LocationsService locationsService) {
		this.locationsService = locationsService;
	}

	@GET
	public GenericEntity<List<Location>> getLocations (@QueryParam("term") String pattern){
		List<Location> locationsFound = locationsService.getLocationByString(pattern);
		return new GenericEntity<List<Location>>(locationsFound) {};
	}
}
