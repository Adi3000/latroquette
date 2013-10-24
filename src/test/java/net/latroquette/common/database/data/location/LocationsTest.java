package net.latroquette.common.database.data.location;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.latroquette.common.test.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class LocationsTest {
	@Autowired
	private LocationsService locationsService;
	/**
	 * @param locationsService the locationsService to set
	 */
	public void setLocationsService(LocationsService locationsService) {
		this.locationsService = locationsService;
	}


	@Test
	public void testgetLocationByType() {
		List<Location> countryLocation = locationsService.getLocationByType(LocationType.COUNTRY);
		assertEquals(1, countryLocation.size());
		assertEquals("France", countryLocation.get(0).getName());
	}

}
