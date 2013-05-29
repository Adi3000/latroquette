package net.latroquette.common.database.data.keyword;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public class KeywordsTest {


	@Test
	public void testgetAmazonKeywordsForTitle() {
		Keywords keywordService = new Keywords();
		List<AmazonKeyword> keywords = keywordService.getAmazonKeywordsForTitle("Lego");
		assertFalse(keywords.isEmpty());
	}

}
