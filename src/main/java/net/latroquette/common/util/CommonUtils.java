package net.latroquette.common.util;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.latroquette.common.util.optimizer.CommonValues;

import org.apache.commons.lang.StringUtils;

public class CommonUtils {
	
	public static List<String> parseStringToList(String stringList){
		if(StringUtils.isEmpty(stringList)){
			return null;
		}else{
			if(stringList.contains(CommonValues.SEPARATOR)){
				return Arrays.asList(stringList.split(CommonValues.SEPARATOR_HTML));
			}else if(stringList.contains(CommonValues.SEPARATOR_HTML)){
				return  Arrays.asList(stringList.split(CommonValues.SEPARATOR));
			}else{
				return Arrays.asList(stringList);
			}
		}
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

	/**
	 * Get timestamp for Now
	 * @return timestamp
	 */
	public static Timestamp getTimestamp(){
		return new Timestamp(new Date().getTime());
	}
}
