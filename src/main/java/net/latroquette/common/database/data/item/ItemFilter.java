package net.latroquette.common.database.data.item;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.adi3000.common.util.optimizer.CommonValues;

/**
 * Filter for itemSearch
 *
 */
public class ItemFilter {

	public static final String DISTANCE_PARAM="d"; 
	public static final String PLACE_PARAM="pl"; 
	public static final String OWNER_PARAM="o";
	public static final String ONLY_TITLE_PARAM="ot";
	public static final String STATUS_PARAM="s"; 
	public static final String PATTERN_PARAM="r"; 
	private String pattern;
	private boolean searchOnDescription;
	private Integer ownerId;
	private Integer itemStatusId;
	private Integer placeId;
	private Integer distance;
	private Integer keywordId;
	
	public ItemFilter(){}
	public ItemFilter(String pattern, boolean searchOnDescription, 
			Integer ownerId, Integer placeId, Integer distance, Integer keywordId){
		this.distance = distance;
		this.pattern = pattern;
		this.searchOnDescription = searchOnDescription;
		this.ownerId = ownerId;
		this.placeId = placeId;
		this.keywordId = keywordId;
	}
	/**
	 * Set filter from a Request URI parameter map without authorization on Status filter
	 * @param parameterMap
	 */
	public void setFilters(Map<String,String> parameterMap){
		setFilters(parameterMap, false);
	}
	/**
	 * Set filter from a Request URI parameter map
	 * @param parameterMap
	 * @param statusFilterPrivilege
	 */
	public void setFilters(Map<String,String> parameterMap, boolean statusFilterPrivilege){
		
		setPattern(parameterMap.get(PATTERN_PARAM));
		setSearchOnDescription(!Boolean.valueOf(parameterMap.get(ONLY_TITLE_PARAM)));
		String value = parameterMap.get(DISTANCE_PARAM);
		if(value != null){
			setDistance(Integer.valueOf(value));
		}
		value = parameterMap.get(STATUS_PARAM);
		if(value != null && statusFilterPrivilege){
			setItemStatusId(Integer.valueOf(value));
		}
		value = parameterMap.get(OWNER_PARAM);
		if(value != null){
			setOwnerId(Integer.valueOf(value));
		}
		value = parameterMap.get(PLACE_PARAM);
		if(value != null){
			setPlaceId(Integer.valueOf(value));
		}
	
	}
	
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return the searchOnDescription
	 */
	public boolean isSearchOnDescription() {
		return searchOnDescription;
	}
	/**
	 * @param searchOnDescription the searchOnDescription to set
	 */
	public void setSearchOnDescription(boolean searchOnDescription) {
		this.searchOnDescription = searchOnDescription;
	}
	/**
	 * @return the ownerId
	 */
	public Integer getOwnerId() {
		return ownerId;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * @return the itemStatusId
	 */
	public Integer getItemStatusId() {
		return itemStatusId;
	}
	/**
	 * @param itemStatusId the itemStatusId to set
	 */
	public void setItemStatusId(Integer itemStatusId) {
		this.itemStatusId = itemStatusId;
	}
	/**
	 * @return the placeId
	 */
	public Integer getPlaceId() {
		return placeId;
	}
	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}
	/**
	 * @return the distance
	 */
	public Integer getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	/**
	 * @return the keywordId
	 */
	public Integer getKeywordId() {
		return keywordId;
	}
	/**
	 * @param keywordId the keywordId to set
	 */
	public void setKeywordId(Integer keywordId) {
		this.keywordId = keywordId;
	}

	/**
	 * Return an URI Request parameter representation of the filter
	 * @return
	 */
	public String getRequestURI(){
		
		StringBuilder sb = new StringBuilder(CommonValues.URI_PARAMETER_SEPARATOR);
		
		if(StringUtils.isNotBlank(getPattern())){
			sb.append(PATTERN_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(getPattern()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		if(!isSearchOnDescription()){
			sb.append(ONLY_TITLE_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(!isSearchOnDescription()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		if(getDistance() != null){
			sb.append(DISTANCE_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(getDistance()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		if(getItemStatusId() != null){
			sb.append(STATUS_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(getItemStatusId()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		if(getOwnerId() != null){
			sb.append(OWNER_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(getOwnerId()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		if(getPlaceId() != null){
			sb.append(PLACE_PARAM).append(CommonValues.URI_VALUE_SEPARATOR).append(getPlaceId()).append(CommonValues.URI_PARAMETER_SEPARATOR);
		}
		
		if(sb.length() <= CommonValues.URI_PARAMETER_SEPARATOR.length()){
			return "";
		}else{
			return sb.substring(1);
		}
	}
	
}
