package net.latroquette.common.util.parameters;

import com.adi3000.common.database.hibernate.session.DAO;
import com.adi3000.common.util.optimizer.CommonValues;

public interface Parameters extends DAO<Parameter>{
	
	/**
	 * Return an integer representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@link CommonValues.ERROR_OR_INFINITE} if not able to parse
	 */
	public int getIntValue(ParameterName name);
	/**
	 * Return a String representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@code null} if parameter {@code name} not found
	 */
	public String getStringValue(ParameterName name);
	/**
	 * Return a boolean representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@code null} if parameter {@code name} not found
	 */
	public boolean getBooleanValue(ParameterName name);
}
