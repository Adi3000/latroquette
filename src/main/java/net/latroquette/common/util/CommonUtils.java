package net.latroquette.common.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.util.optimizer.CommonValues;

import org.apache.commons.lang.StringUtils;

public class CommonUtils {
	private static final Logger LOGGER = Logger.getLogger(CommonUtils.class.getName());
	public static List<String> parseStringToList(String stringList){
		if(StringUtils.isEmpty(stringList)){
			return null;
		}else{
			return Arrays.asList(stringList.split(CommonValues.SEPARATOR));
		}
	}
	public static List<Integer> parseStringToIntegerList(String stringList){
		List<String> listString = parseStringToList(stringList);
		List<Integer> listInt = new ArrayList<Integer>(listString.size());
		try{
			for(String s : listString){
				listInt.add(Integer.valueOf(s));
			}
		}catch(NumberFormatException e){
			LOGGER.log(Level.WARNING,"One of the input cannot be converted" + stringList,e );
			listInt = null;
		}
		return listInt;
	}
	
	public static String formatListToString(List<?> list){
		if(list == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean firstLoop = true;
		for(Object obj : list ){
			if(!firstLoop){
				sb.append(CommonValues.SEPARATOR);
			}
			sb.append(obj.toString());
			firstLoop = false;
		}
		return sb.toString();
	}

	public static Character toChar(Boolean b){
		return b != null && b ? CommonValues.TRUE : CommonValues.FALSE; 
	}
	public static Boolean isTrue(CharSequence b){
		return Boolean.valueOf(b != null && CommonValues.TRUE.toString().equals(b.toString())); 
	}
	public static Boolean isTrue(Character b){
		return Boolean.valueOf(CommonValues.TRUE.equals(b)) ; 
	}
	/**
	 * Get timestamp for Now
	 * @return timestamp
	 */
	public static Timestamp getTimestamp(){
		return new Timestamp(new Date().getTime());
	}
	
	public static AbstractDataObject findById(Collection<? extends AbstractDataObject> list,Serializable id){
		if(id == null){
			return null;
		}
		for(AbstractDataObject o : list){
			if(o != null && o.getId() != null){
				if(id.equals(o.getId()) || id.toString().equals(o.getId().toString())){
					return o;
				}
			}
		}
		return null;
	}
}
