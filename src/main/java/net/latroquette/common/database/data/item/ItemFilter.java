package net.latroquette.common.database.data.item;

/**
 * Filter for itemSearch
 *
 */
public class ItemFilter {

	private String pattern;
	private boolean searchOnDescription;
	private Integer ownerId;
	private Integer itemStatusId;
	private Integer placeId;
	private Double distance;
	private Integer keywordId;
	
	public ItemFilter(){}
	public ItemFilter(String pattern, boolean searchOnDescription, 
			Integer ownerId, Integer placeId, Double distance, Integer keywordId){
		this.distance = distance;
		this.pattern = pattern;
		this.searchOnDescription = searchOnDescription;
		this.ownerId = ownerId;
		this.placeId = placeId;
		this.keywordId = keywordId;
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
	public Double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Double distance) {
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

	
}
