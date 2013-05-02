package net.latroquette.common.database.data.location;

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

import net.latroquette.common.database.data.AbstractDataObject;

@Entity
@Table(name = "locations")
@XmlRootElement
public class Location extends AbstractDataObject {

	
	private Integer id; 
	private String name;
	private String postalCodes;
	private List<String> postalCodesList;
	private LocationType locationType;
	private Double longitude;
	private Double latitude;
	private Integer locationTypeId;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3600820714557739956L;

	/**
	 * @return the namelocationType
	 */
	@Column(name = "location_name")
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
	@Column(name = "location_postal_codes")
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
	@Column(name = "location_longitude")
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
	@Column(name = "location_latitude")
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
	@Column(name = "location_id")
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/**
	 * @return the locationType
	 */
	@Transient
	public LocationType getLocationType() {
		return locationType;
	}
	@Column(name = "location_type_id")
	public Integer getLocationTypeId() {
		return locationTypeId;
	}
	

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(LocationType locationType) {
		setLocationTypeId(locationType.getValue());
	}
	public void setLocationTypeId(Integer locationTypeId) {
		this.locationTypeId = locationTypeId;
		this.locationType = LocationType.valueOf(locationTypeId);
	}

	

}
