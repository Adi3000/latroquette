package net.latroquette.common.database.data.place;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;


@Entity
@Table(name = "places")
@XmlRootElement
public class Place extends AbstractDataObject {

	
	private Integer id; 
	private String name;
	private String postalCodes;
	private List<String> postalCodesList;
	private PlaceType placeType;

	private Double longitude;
	private Double latitude;
	private Integer placeTypeId;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3600820714557739956L;

	/**
	 * @return the namelocationType
	 */
	@Column(name = "place_name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the postalCodes
	 */
	@Column(name = "place_postal_codes")
	public String getPostalCodes() {
		return postalCodes;
	}

	/**
	 * @param postalCodes the postalCodes to set
	 */
	public void setPostalCodes(String postalCodes) {
		this.postalCodes = postalCodes;
		if(postalCodes != null){
			setPostalCodesList(Arrays.asList(postalCodes.split(",")));
		}
	}

	/**
	 * @return the postalCodesList
	 */
	@Transient
	public List<String> getPostalCodesList() {
		return postalCodesList;
	}

	/**
	 * @param postalCodesList the postalCodesList to set
	 */
	public void setPostalCodesList(List<String> postalCodesList) {
		this.postalCodesList = postalCodesList;
	}

	/**
	 * @return the longitude
	 */
	@Column(name = "place_longitude")
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(name = "place_latitude")
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "place_id")
	public Integer getId() {
		return id;
	}

	/**
	 * @return the locationType
	 */
	@Transient
	public PlaceType getPlaceType() {
		return placeType;
	}
	@Column(name = "place_type_id")
	public Integer getPlaceTypeId() {
		return placeTypeId;
	}
	

	/**
	 * @param placeType the locationType to set
	 */
	public void setPlaceType(PlaceType placeType) {
		setPlaceTypeId(placeType.getValue());
	}
	public void setPlaceTypeId(Integer placeTypeId) {
		this.placeTypeId = placeTypeId;
		this.placeType = PlaceType.valueOf(placeTypeId);
	}

	

}
