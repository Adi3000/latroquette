package net.latroquette.common.database.data.place;

import java.awt.geom.Point2D;
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
	
	private static final double KILOMETER_CONVERSION = 1.609;
	private static final double EARTH_RADIUS = 3958.75;
	@Transactional(readOnly=true)
	public List<Place> getPlacesByType(PlaceType placeType){
		Criteria req = createCriteria(Place.class)
				.add(Restrictions.eq("placeTypeId", placeType.getValue())) ;
		@SuppressWarnings("unchecked")
		List<Place> places = (List<Place>)req.list();
		
		return places;
	}
	
	public Place getPlaceById(Integer placeId){
		return getDataObjectById(placeId, Place.class);
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
				" SELECT {place.*} FROM places {place} ".concat(
				" WHERE place_name ILIKE :nameStart or place_name ~* :nameInner").concat(
				" UNION SELECT {place.*} FROM places {place} ").concat(
				" WHERE place_postal_codes LIKE :codeStart  or place_postal_codes LIKE :oneCodeStart ");
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
	
	@Transactional(readOnly=true)
	public Point2D.Double getRadixByPlaceId(Integer placeId, double radixInKilometer){
		Place place = (Place) getSession().get(Place.class, placeId);
		double longitudeToKilometers = distanceFrom(
				place.getLatitude(), place.getLongitude(),
				place.getLatitude(), place.getLongitude()+1);
		double latitudeToKilometers = distanceFrom(
				place.getLatitude(), place.getLongitude(),
				place.getLatitude()+1, place.getLongitude());
		double longitudeRadix = radixInKilometer / longitudeToKilometers;
		double latitudeRadix = radixInKilometer / latitudeToKilometers;
		Point2D.Double point = new Point2D.Double(longitudeRadix,latitudeRadix);
		return point;
	}

//	String hql = 
//			" from Place p1".concat(
//			" where exists (from Place p2 where p2.id = :placeId ").concat(
//			" and abs(p2.longitude - p1.longitude ) < :longitudeRadix").concat(
//			" and abs(p2.latitude - p1.latitude ) < :latitudeRadix )");
//		Query req = createQuery(hql)
//						.setDouble("longitudeRadix", longitudeRadix)
//						.setDouble("latitudeRadix", latitudeRadix)
//						.setInteger("placeId", place.getId());
//		@SuppressWarnings("unchecked")
//		List<Place> places = (List<Place>)req.list();

	 public static double distanceFrom(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
		 double degreeLatitude = Math.toRadians(latitude2-latitude1);
		 double degreeLongitude = Math.toRadians(longitude2-longitude1);
		 double degreeGap = Math.sin(degreeLatitude/2) * Math.sin(degreeLatitude/2) +
				 Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
				 Math.sin(degreeLongitude/2) * Math.sin(degreeLongitude/2);
		 double degreeDistance = 2 * Math.atan2(Math.sqrt(degreeGap), Math.sqrt(1-degreeGap));
		 double distance = EARTH_RADIUS * degreeDistance;


		 return distance * KILOMETER_CONVERSION;
	 }
}
