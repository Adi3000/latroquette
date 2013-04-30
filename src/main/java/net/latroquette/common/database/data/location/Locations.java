package net.latroquette.common.database.data.location;

import java.util.List;

import net.latroquette.common.database.data.AbstractDAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

public class Locations extends AbstractDAO {
	
	public List<Location> getLocationByType(LocationType locationType){
		Criteria req = this.session.createCriteria(Location.class)
				.add(Restrictions.eq("locationTypeId", locationType.getId())) ;
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		
		return locations;
	}
	
	public List<Location> getLocationByString(String value){
		
		String likeValue1 = value.replace(' ', '_').replace('-', '_').concat("%");
		String replacing = "(-| |')";
		String likeValue2 = replacing.concat(value.replace(" ", replacing).replace("-", replacing));
		String postalCode1 = value.concat("%");
		String postalCode2 = "%,".concat(postalCode1);
		
		String sql = 
				" SELECT {locations.*} FROM locations {locations} ".concat(
				" WHERE location_name ILIKE ? or location_name ~* ?").concat(
				" UNION SELECT {locations.*} FROM locations {locations} ").concat(
				" WHERE location_postal_codes LIKE ?  or location_postal_codes LIKE ? ");
		//Can't ordering this for now.
		//.concat(	" ORDER BY {location.location_name}");
		Query req = this.session.createSQLQuery(sql).addEntity("locations",Location.class);
		req		.setString(0, likeValue1)
				.setString(1, likeValue2)
				.setString(2, postalCode1)
				.setString(3, postalCode2);
		@SuppressWarnings("unchecked")
		List<Location> locations = (List<Location>)req.list();
		return locations;
	}

}
