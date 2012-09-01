package net.latroquette.common.database.data.location;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import net.latroquette.common.database.session.DatabaseSession;

public class Locations extends DatabaseSession {
	
	public List<Location> getLocationByType(LocationType locationType){
		Criteria req = this.session.createCriteria(Location.class)
				.add(Restrictions.eq("locationTypeId", locationType.getId())) ;
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		
		return locations;
	}
	
	public List<Location> getLocationByString(String value){
		String likeValue1 = value.concat("%");
		String likeValue2 = "(-| )".concat(value);
		String postalCode1 = value.concat("%");
		String postalCode2 = "%,".concat(postalCode1);
		
		String sql = 
				" SELECT {location.*} FROM location {location} ".concat(
				" WHERE location_name ILIKE ? or location_name ~* ?").concat(
				" UNION SELECT {location.*} FROM location {location} ").concat(
				" WHERE location_postal_codes LIKE ?  or location_postal_codes LIKE ? ");
		//Can't ordering this for now.
		//.concat(	" ORDER BY {location.location_name}");
		Query req = this.session.createSQLQuery(sql).addEntity("location",Location.class);
		req		.setString(0, likeValue1)
				.setString(1, likeValue2)
				.setString(2, postalCode1)
				.setString(3, postalCode2);
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		return locations;
	}

}
