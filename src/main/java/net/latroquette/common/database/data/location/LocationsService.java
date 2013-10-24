package net.latroquette.common.database.data.location;

import java.util.List;

import net.latroquette.common.database.data.Repositories;

public interface LocationsService extends Repositories<Location> {

	public List<Location> getLocationByType(LocationType locationType);
	/**
	 * Return a {@link Location} by its name or code postal
	 * @param value
	 * @return
	 */
	public List<Location> getLocationByString(String value);
}
