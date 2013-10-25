package net.latroquette.common.database.data.location;

import java.util.List;

import com.adi3000.common.database.hibernate.session.DAO;

public interface LocationsService extends DAO<Location> {

	public List<Location> getLocationByType(LocationType locationType);
	/**
	 * Return a {@link Location} by its name or code postal
	 * @param value
	 * @return
	 */
	public List<Location> getLocationByString(String value);
}
