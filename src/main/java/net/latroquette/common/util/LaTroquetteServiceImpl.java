package net.latroquette.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.latroquette.common.util.web.Navigation;
import net.latroquette.common.util.web.NavigationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(Services.LA_TROQUETTE_SERVICE)
public class LaTroquetteServiceImpl implements LaTroquetteService{

	@Inject
	private NavigationDao navigationDao;
	
	@Transactional(readOnly=true)
	public List<Navigation> getAllNavigation(){
		return navigationDao.getAllNavigation();
	}
	
	@Transactional(readOnly=true)
	public Map<String, Navigation> getNavigationMap(){
		List <Navigation> list = getAllNavigation();
		Map<String, Navigation> map = new HashMap<String, Navigation>(list.size());
		for (Navigation nav : list){
			map.put(nav.getPath(), nav);
		}
		return map;
	}
	
}
