package net.latroquette.common.database.data.keyword;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.wish.WishedItem;

public enum KeywordSource {
	AMAZON_SOURCE(ExternalKeyword.AMAZON_SOURCE),
	WISHES_SOURCE(WishedItem.WISHES_SOURCE),
	MAIN_KEYWORD_SOURCE(MainKeyword.MAIN_KEYWORD_SOURCE),
	EXTERNAL_KEYWORD_SOURCE(ExternalKeyword.EXTERNAL_KEYWORD_SOURCE),
	ITEM_SOURCE(Item.ITEM_SOURCE);
	
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
	
	public static KeywordSource get(String sourceId){
		for(KeywordSource source : KeywordSource.values()){
			if(source.getSourceId().equals(sourceId)){
				return source;
			}
		}
		return null;
	}
	
	
}
