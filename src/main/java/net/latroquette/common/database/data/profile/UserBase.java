package net.latroquette.common.database.data.profile;

import com.adi3000.common.database.hibernate.data.DataObject;

public interface UserBase extends DataObject {

	Integer getId();
	String getLogin();
	String getMail();
	Role getRole();
}
