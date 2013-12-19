package net.latroquette.common.database.data.profile;

import javax.xml.bind.annotation.XmlElement;

/**
 * Utility to get a {@link User} copy for info with 
 * some additionnal XmlInformation like mail
 */
public class UserAdminInfo extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5330321265720514011L;

	@XmlElement(name="mail")
	public String getMailExported(){
		return super.getMail();
	}
	
	public UserAdminInfo(User user){
		if(user != null){
			this.setId(user.getId());
			this.setLogin(user.getLogin());
			this.setMail(user.getMail());
		}
	}
}
