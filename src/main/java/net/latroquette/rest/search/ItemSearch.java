package net.latroquette.rest.search;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemFilter;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.item.wish.Wish;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.KeywordType;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.Services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.adi3000.common.util.tree.Breadcrumb;

/**
 * @author adi
 *
 */
@Path("/search/item")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@WebService(name=Services.ITEM_SEARCH_WEB_SERVICE)
public class ItemSearch extends SpringBeanAutowiringSupport {
	
	@Inject
	private transient ItemsService itemsService;
	@Inject
	private transient KeywordsService keywordsService;
	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}

	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}

	@GET
	@WebMethod
	public GenericEntity<List<Item>> getItems (
			@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle,
			@QueryParam("p") Integer page, 
			@QueryParam("c") Integer category, 
			@QueryParam("a") String autocomplete ){
		ItemFilter itemFilter = new ItemFilter();
		itemFilter.setPattern(pattern);
		itemFilter.setSearchOnDescription(!Boolean.valueOf(onlyTitle));
		itemFilter.setKeywordId(category);
		List<Item> itemsFound = itemsService.searchItem(itemFilter, page, Boolean.valueOf(autocomplete));
		return new GenericEntity<List<Item>>(itemsFound) {};
	}
	
	@GET
	@Path("/count")
	@WebMethod
	public GenericEntity<Integer> countItems (
			@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle,
			@QueryParam("c") Integer category){
		ItemFilter itemFilter = new ItemFilter();
		itemFilter.setPattern(pattern);
		itemFilter.setSearchOnDescription(!Boolean.valueOf(onlyTitle));
		itemFilter.setKeywordId(category);
		Integer itemsFound = itemsService.countItem(itemFilter);
		return new GenericEntity<Integer>(itemsFound) {};
	}
	
	@GET
	@Path("/children")
	@WebMethod
	public GenericEntity<List<Keyword>> getChilden (@QueryParam("i") String idString, 
			@QueryParam("t") String keywordType){
		if(!StringUtils.isNumeric(idString) || StringUtils.isEmpty(idString)  ||
				StringUtils.isEmpty(keywordType)   || !StringUtils.isNumeric(keywordType)){
			return null;
		}
		Integer id = StringUtils.isNotEmpty(idString)  ? Integer.valueOf(idString) : Integer.valueOf(0);
		List<Keyword> keywordsFound = keywordsService.getDirectChildrenOf(id, KeywordType.get(Integer.valueOf(keywordType)));
		return new GenericEntity<List<Keyword>>(keywordsFound) {};
	}
	@GET
	@Path("/breadcrumb")
	@WebMethod
	public GenericEntity<List<Keyword>> getBreadCrumb (@QueryParam("i") String idString, 
			@QueryParam("t") String keywordType){
		if(!StringUtils.isNumeric(idString) || StringUtils.isEmpty(idString)  ||
				StringUtils.isEmpty(keywordType)   || !StringUtils.isNumeric(keywordType)){
			return null;
		}
		Integer id = StringUtils.isNotEmpty(idString)  ? Integer.valueOf(idString) : Integer.valueOf(0);
		Keyword keywordFound = null;
		List<Keyword> result = new ArrayList<>();
		switch (KeywordType.get(Integer.valueOf(keywordType))) {
			case MAIN_KEYWORD:
				Breadcrumb<MainKeyword> breadcrumbMain = null;
				keywordFound = keywordsService.getKeywordById(Integer.valueOf(idString));
				breadcrumbMain = new Breadcrumb<MainKeyword>((MainKeyword)keywordFound); 
				result.addAll(breadcrumbMain.getBreadcrumb());
				break;
			case EXTERNAL_KEYWORD:
				Breadcrumb<ExternalKeyword> breadcrumbExternal = null;
				keywordFound = keywordsService.getExternalKeywordById(id);
				breadcrumbExternal = new Breadcrumb<ExternalKeyword>((ExternalKeyword)keywordFound);
				result.addAll(breadcrumbExternal.getBreadcrumb());
				break;
			default:
				break;
			}
		return new GenericEntity<List<Keyword>>(result) {};
	}
	@GET
	@Path("/amazon")
	@WebMethod
	public GenericEntity<List<AmazonItem>> getAmazonItems (@QueryParam("term") String pattern, 
			@QueryParam("cat") String category ){
		List<AmazonItem> itemsFound = itemsService.searchAmazonItems(category, pattern);
		return new GenericEntity<List<AmazonItem>>(itemsFound) {};
	}
	@GET
	@Path("/amazon/byId")
	@WebMethod
	public GenericEntity<AmazonItem> getAmazonItemsById(@QueryParam("id") String id){
		AmazonItem itemFound = itemsService.searchAmazonItemById(id);
		if(itemFound == null){
			return null;
		}
		return new GenericEntity<AmazonItem>(itemFound) {};
	}
	@GET
	@Path("/wish")
	@WebMethod
	public GenericEntity<List<Wish>> getWish (@QueryParam("term") String pattern){
		List<Wish> wishesFound = itemsService.searchWishes(pattern);
		return new GenericEntity<List<Wish>>(wishesFound) {};
	}
}
