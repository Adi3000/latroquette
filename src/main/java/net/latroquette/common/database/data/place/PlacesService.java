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
}
