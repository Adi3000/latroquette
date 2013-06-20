package net.latroquette.common.database.data.location;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

public class LocationsService extends AbstractDAO<Location> {
	
	public List<Location> getLocationByType(LocationType locationType){
		Criteria req = createCriteria(Location.class)
				.add(Restrictions.eq("locationTypeId", locationType.getValue())) ;
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		
		return locations;
	}
	
	/**
	 * Return a {@link Location} by its name or code postal
	 * @param value
	 * @return
	 */
	public List<Location> getLocationByString(String value){
		
		String likeValue1 = value.replace(' ', '_').replace('-', '_').concat("%");
		String replacing = "(-| |')";
		String likeValue2 = replacing.concat(value.replace(" ", replacing).replace("-", replacing));
		String postalCode1 = value.concat("%");
		String postalCode2 = "%,".concat(postalCode1);
		
		String sql = 
				" SELECT {locations.*} FROM locations {locations} ".concat(
				" WHERE location_name ILIKE :nameStart or location_name ~* :nameInner").concat(
				" UNION SELECT {locations.*} FROM locations {locations} ").concat(
				" WHERE location_postal_codes LIKE :codeStart  or location_postal_codes LIKE :oneCodeStart ");
		//Can't ordering this for now.
		//.concat(	" ORDER BY {location.location_name}");
		Query req = createSQLQuery(sql).addEntity("locations",Location.class);
		req		.setString(":nameStart", likeValue1)
				.setString(":nameInner", likeValue2)
				.setString(":codeStart", postalCode1)
				.setString(":oneCodeStart", postalCode2);
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		return locations;
	}

}
