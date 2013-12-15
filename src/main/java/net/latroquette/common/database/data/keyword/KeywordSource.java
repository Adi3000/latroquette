package net.latroquette.common.database.data.keyword;

import net.latroquette.common.database.data.item.wish.WishedItem;

public enum KeywordSource {
	AMAZON_SOURCE(ExternalKeyword.AMAZON_SOURCE),
	WISHES_SOURCE(WishedItem.WISHES_SOURCE);
	private final String sourceId;

	private KeywordSource(String sourceId){
		this.sourceId = sourceId;
	}

	/**
	 * @return the sourceId
	 */
	public String getSourceId() {
		return sourceId;
	}
	
	
}
