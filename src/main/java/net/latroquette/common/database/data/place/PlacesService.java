package net.latroquette.common.database.data.place;

import java.awt.geom.Point2D;
import java.util.List;

import com.adi3000.common.database.hibernate.session.DAO;

public interface PlacesService extends DAO<Place> {

	public List<Place> getPlacesByType(PlaceType placeType);
	/**
	 * Return a {@link Place} by its id
	 * @param value
	 * @return
	 */
	public Place getPlaceById(Integer placeId);
	
	/**
	 * Return a {@link Place} by its name or code postal
	 * @param value
	 * @return
	 */
	public List<Place> getPlacesByString(String value);
	
	/**
	 * Return a {@link Point2D.Double} with the longitude and latitude with
	 * {@code radix} in kilometers
	 * from a {@link Place} retrived by its id {@code placeId} 
	 * @param placeId
	 * @param radix
	 * @return
	 */
	public Point2D.Double getRadixByPlaceId(Integer placeId, double radixInKilometer);
}
