package net.latroquette.common.util.web;

import java.util.List;

import net.latroquette.common.database.data.Repositories;

import org.springframework.stereotype.Repository;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

@Repository(Repositories.DAO_NAVIGATION)
public class NavigationDaoImpl extends AbstractDAO<Navigation> implements NavigationDao{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8029228145758855200L;

	public NavigationDaoImpl() {
		super(Navigation.class);
		// TODO Auto-generated constructor stub
	}

	public List<Navigation> getAllNavigation(){
		@SuppressWarnings("unchecked")
		List<Navigation> list = getSession().createCriteria(Navigation.class).list();
		for(Navigation nav : list){
			nav.initializeAncestors();
		}
		return list;
	}
}
