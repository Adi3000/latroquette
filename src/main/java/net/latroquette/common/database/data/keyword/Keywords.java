package net.latroquette.common.database.data.keyword;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.amazon.ECS.client.jax.BrowseNode;
import com.googlecode.ehcache.annotations.Cacheable;

import net.latroquette.common.database.data.AbstractDAO;

public class Keywords extends AbstractDAO {
/*
	@Cacheable(cacheName="amazoneKeyword")
	private Keyword getAmazonKeyword(String amazonBrowseNodeId){
		Criteria req = this.session.createCriteria(Parameter.class)
				.setMaxResults(1)
				.add(Restrictions.eq("name", name.toString())) ;
		Parameter parameter =  (Parameter)req.uniqueResult();
		if(parameter == null){
			return null;
		}else{
			return parameter.getValue();
		}
	}*/
}
