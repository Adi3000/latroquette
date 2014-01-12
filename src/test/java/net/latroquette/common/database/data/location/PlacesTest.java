package net.latroquette.common.database.data.location;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import net.latroquette.common.database.data.place.Place;
import net.latroquette.common.database.data.place.PlaceType;
import net.latroquette.common.database.data.place.PlacesService;
import net.latroquette.common.test.LatroquetteTest;
import net.latroquette.common.test.dbunit.ListDbUnitTestCase;
import net.latroquette.common.test.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class PlacesTest extends ListDbUnitTestCase implements LatroquetteTest{
	
	private static List<String> DBU_FILES = Arrays.asList(
			 PLACES_DBU_RESOURCE
		);
	@Inject
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


	@Override
	protected List<String> getDataSetsPath() {
		// TODO Auto-generated method stub
		return DBU_FILES;
	}

}
