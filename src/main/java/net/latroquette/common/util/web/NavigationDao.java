package net.latroquette.common.util.web;

import java.util.List;

import com.adi3000.common.database.hibernate.session.DAO;

public interface NavigationDao extends DAO<Navigation>{

	/**
	 * Return all {@link Navigation} point
	 * @return
	 */
	public List<Navigation> getAllNavigation();

}
