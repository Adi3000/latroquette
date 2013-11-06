package net.latroquette.common.database.data.location;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.latroquette.common.database.data.place.Place;
import net.latroquette.common.database.data.place.PlaceType;
import net.latroquette.common.database.data.place.PlacesService;
import net.latroquette.common.test.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class PlacesTest {
	@Autowired
	private transient PlacesService placesService;
	/**
	 * @param locationsService the locationsService to set
	 */
	public void setPlacesService(PlacesService placesService) {
		this.placesService = placesService;
	}


	@Test
	public void testgetLocationByType() {
		List<Place> countryLocation = placesService.getPlacesByType(PlaceType.COUNTRY);
		assertEquals(1, countryLocation.size());
		assertEquals("France", countryLocation.get(0).getName());
	}

}
