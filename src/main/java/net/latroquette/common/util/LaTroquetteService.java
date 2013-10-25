package net.latroquette.common.util;

import java.util.List;
import java.util.Map;

import net.latroquette.common.util.web.Navigation;

public interface LaTroquetteService {

	public Map<String, Navigation> getNavigationMap();
	public List<Navigation> getAllNavigation();
}
