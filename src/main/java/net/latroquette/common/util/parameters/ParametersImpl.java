package net.latroquette.common.util.parameters;



import net.latroquette.common.util.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.util.optimizer.CommonValues;

@Repository(value=Services.PARAMETERS_SERVICE)
public class ParametersImpl extends AbstractDAO<Parameter> implements Parameters {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParametersImpl.class.getName());

	public ParametersImpl(){
		super();
	}
	//TODO make this work and enable load in memory for this cache for fast access
	@Transactional(readOnly=true)
	private String getValue(ParameterName name){
		Parameter parameter =  (Parameter)getSession().bySimpleNaturalId(Parameter.class).load(name.toString());
		if(parameter == null){
			return null;
		}else{
			return parameter.getValue();
		}
	}
	
	/**
	 * Return an integer representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@link CommonValues.ERROR_OR_INFINITE} if not able to parse
	 */
	@Cacheable(value="parameters", key="#name")
	@Transactional(readOnly=true)
	public int getIntValue(ParameterName name){
		int value = CommonValues.ERROR_OR_INFINITE;
		try{
			value = Integer.valueOf(getValue(name));
		}catch(NumberFormatException e){
			value = CommonValues.ERROR_OR_INFINITE;
			LOGGER.warn("No suitable integer found for parameter "+ name+ ", value forced to : "+ value , e);
		}
		return value;
	}
	/**
	 * Return a String representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@code null} if parameter {@code name} not found
	 */
	@Cacheable(value="parameters", key="#name")
	@Transactional(readOnly=true)
	public boolean getBooleanValue(ParameterName name){
		String value = null;
		try{
			value = getValue(name).toString();
		}catch(NullPointerException e){
			value = null;
			LOGGER.warn("No suitable string value found for parameter "+ name+ ", value forced to : "+ value , e);
		}
		return CommonValues.TRUE.toString().equals(value);
	}
	/**
	 * Return a String representation of the parameter {@code name}
	 * @param name
	 * @return the value or {@code null} if parameter {@code name} not found
	 */
	@Cacheable(value="parameters", key="#name")
	@Transactional(readOnly=true)
	public String getStringValue(ParameterName name){
		String value = null;
		try{
			value = getValue(name).toString();
		}catch(NullPointerException e){
			value = null;
			LOGGER.warn("No suitable string value found for parameter "+ name+ ", value forced to : "+ value , e);
		}
		return value;
	}

}
