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
	
	
	@GET
	public GenericEntity<List<Location>> getLocations (@QueryParam("term") String pattern){
		LocationsService locationsService = new LocationsService();
		List<Location> locationsFound = locationsService.getLocationByString(pattern);
		locationsService.close();
		return new GenericEntity<List<Location>>(locationsFound) {};
	}
}
