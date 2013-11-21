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

import net.latroquette.common.database.data.place.Place;
import net.latroquette.common.database.data.place.PlacesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author adi
 *
 */
@Path("/search/places")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@WebService
public class PlacesSearch extends SpringBeanAutowiringSupport{
	
	@Autowired
	private transient PlacesService placesService;
	
	/**
	 * @param placesService the placesService to set
	 */
	public void setPlacesService(PlacesService placesService) {
		this.placesService = placesService;
	}

	@GET
	@WebMethod
	public GenericEntity<List<Place>> getPlaces (@QueryParam("term") String pattern){
		List<Place> placesFound = placesService.getPlacesByString(pattern);
		return new GenericEntity<List<Place>>(placesFound) {};
	}
	@GET
	@Path("/by")
	@WebMethod
	public GenericEntity<List<Place>> getPlacesByPlaces (@QueryParam("place") String placeId, @QueryParam("radix") String radix){
		List<Place> placesFound = placesService.getPlacesByPlace(placeId, Double.valueOf(radix));
		return new GenericEntity<List<Place>>(placesFound) {};
	}
}
