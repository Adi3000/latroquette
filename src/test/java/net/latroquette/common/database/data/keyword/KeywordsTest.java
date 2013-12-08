package net.latroquette.common.database.data.keyword;

import static org.junit.Assert.assertFalse;

import java.util.List;

import net.latroquette.common.test.utils.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {TestUtils.SPRING_CONFIG})
public class KeywordsTest {

	@Autowired
	private transient KeywordsService keywordsService;
	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}


	@Test
	public void testgetAmazonKeywordsForTitle() {
		//TODO wait for secured AWS key
//		List<ExternalKeyword> keywords = keywordsService.getAmazonKeywordsFromItem("Lego", true);
//		assertFalse(keywords.isEmpty());
	}

}
