package net.latroquette.common.util.parameters;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.latroquette.common.database.session.DatabaseSession;
import net.latroquette.common.util.optimizer.CommonValues;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.googlecode.ehcache.annotations.Cacheable;

public class Parameters extends DatabaseSession {
	private static final Logger LOGGER = Logger.getLogger(Parameters.class.getName());

	@Cacheable(cacheName="parameters")
	private String getValue(String name){
		Criteria req = this.session.createCriteria(Parameter.class)
				.setMaxResults(1)
				.add(Restrictions.eq("name", name)) ;
		return (String)req.uniqueResult();
	}
	
	/**
	 * Return an integer representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@link CommonValues.ERROR_OR_INFINITE} if not able to parse
	 */
	public int getIntValue(String name){
		int value = CommonValues.ERROR_OR_INFINITE;
		try{
			value = Integer.valueOf(getValue(name));
		}catch(NumberFormatException e){
			value = CommonValues.ERROR_OR_INFINITE;
			LOGGER.log(Level.WARNING,"No suitable integer found for parameter "+ name+ ", value forced to : "+ value , e);
		}
		return value;
	}
	/**
	 * Return a String representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@code null} if parameter {@code name} not found
	 */
	public String getStringValue(String name){
		String value = null;
		try{
			value = getValue(name).toString();
		}catch(NullPointerException e){
			value = null;
			LOGGER.log(Level.WARNING,"No suitable string value found for parameter "+ name+ ", value forced to : "+ value , e);
		}
		return value;
	}

}
