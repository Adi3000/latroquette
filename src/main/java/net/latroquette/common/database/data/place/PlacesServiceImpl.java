package net.latroquette.common.database.data.place;

import java.util.List;


import net.latroquette.common.util.Services;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

@Repository(value=Services.PLACES_SERVICE)
public class PlacesServiceImpl extends AbstractDAO<Place> implements PlacesService {
	
	@Transactional(readOnly=true)
	public List<Place> getPlacesByType(PlaceType placeType){
		Criteria req = createCriteria(Place.class)
				.add(Restrictions.eq("placeTypeId", placeType.getValue())) ;
		@SuppressWarnings("unchecked")
		List<Place> places = (List<Place>)req.list();
		
		return places;
	}
	
	/**
	 * Return a {@link Place} by its name or code postal
	 * @param value
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Place> getPlacesByString(String value){
		
		String likeValue1 = value.replace(' ', '_').replace('-', '_').concat("%");
		String replacing = "(-| |')";
		String likeValue2 = replacing.concat(value.replace(" ", replacing).replace("-", replacing));
		String postalCode1 = value.concat("%");
		String postalCode2 = "%,".concat(postalCode1);
		
		String sql = 
				" SELECT {place.*} FROM locations {place} ".concat(
				" WHERE location_name ILIKE :nameStart or location_name ~* :nameInner").concat(
				" UNION SELECT {place.*} FROM locations {place} ").concat(
				" WHERE location_postal_codes LIKE :codeStart  or location_postal_codes LIKE :oneCodeStart ");
		//Can't ordering this for now.
		//.concat(	" ORDER BY {place.location_name}");
		Query req = createSQLQuery(sql).addEntity("place",Place.class);
		req		.setString("nameStart", likeValue1)
				.setString("nameInner", likeValue2)
				.setString("codeStart", postalCode1)
				.setString("oneCodeStart", postalCode2);
		@SuppressWarnings("unchecked")
		List<Place> places = (List<Place>)req.list();
		return places;
	}
	
/*	select l2.* from locations l1 ,locations l2 
	where l1.location_id = 63373
	and abs(l2.location_longitude - l1.location_longitude ) < 0.5
	and abs(l2.location_latitude - l1.location_latitude ) < 0.5
*/

}
