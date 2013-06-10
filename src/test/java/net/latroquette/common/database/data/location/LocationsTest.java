package net.latroquette.common.database.data.location;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LocationsTest {

	@Test
	public void testgetLocationByType() {
		LocationsService locations = new LocationsService();
		List<Location> countryLocation = locations.getLocationByType(LocationType.COUNTRY);
		locations.closeSession();
		assertEquals(1, countryLocation.size());
		assertEquals("France", countryLocation.get(0).getName());
	}

}
