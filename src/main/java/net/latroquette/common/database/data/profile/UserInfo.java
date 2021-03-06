package net.latroquette.common.database.data.profile;

import javax.xml.bind.annotation.XmlElement;

import net.latroquette.common.database.data.place.Place;

/**
 * Utility to get a {@link User} copy for info with 
 * some additionnal XmlInformation like mail
 */
public class UserInfo extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5330321265720514011L;

	@XmlElement(name="place")
	public Place getPlaceExported(){
		return super.getPlace();
	}
	
	public UserInfo(User user){
		if(user != null){
			this.setId(user.getId());
			this.setLogin(user.getLogin());
			this.setPlace(user.getPlace());
		}
	}
}
