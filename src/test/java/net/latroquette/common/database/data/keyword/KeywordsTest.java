package net.latroquette.common.database.data.keyword;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public class KeywordsTest {


	@Test
	public void testgetAmazonKeywordsForTitle() {
		KeywordsService keywordService = new KeywordsService();
		List<ExternalKeyword> keywords = keywordService.getAmazonKeywordsFromItem("Lego", true);
		assertFalse(keywords.isEmpty());
	}

}
