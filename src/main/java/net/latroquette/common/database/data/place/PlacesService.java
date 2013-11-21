package net.latroquette.common.database.data.place;

import java.util.List;

import com.adi3000.common.database.hibernate.session.DAO;

public interface PlacesService extends DAO<Place> {

	public List<Place> getPlacesByType(PlaceType placeType);
	/**
	 * Return a {@link Place} by its name or code postal
	 * @param value
	 * @return
	 */
	public List<Place> getPlacesByString(String value);
	
	/**
	 * Return a {@link List} of {@link Place} close by {@code radix} kilometers
	 * from a {@link Place} retrived by its id {@code placeId} 
	 * @param placeId
	 * @param radix
	 * @return
	 */
	public List<Place> getPlacesByPlace(String placeId, double radix);
	/**
	 * Return a {@link List} of {@link Place} close by {@code radix} kilometers
	 * from {@code place} 
	 * @param placeId
	 * @param radix
	 * @return
	 */
	public List<Place> getPlacesByPlace(Place place, double radix);
}
