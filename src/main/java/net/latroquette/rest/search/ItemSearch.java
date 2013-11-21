package net.latroquette.rest.search;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.KeywordType;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.Services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private transient ItemsService itemsService;
	@Autowired
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
		MainKeyword keyword = category != null ? keywordsService.getKeywordById(category) : null;
		List<Item> itemsFound = itemsService.searchItem(pattern, !Boolean.valueOf(onlyTitle), page, keyword,Boolean.valueOf(autocomplete));
		return new GenericEntity<List<Item>>(itemsFound) {};
	}
	
	@GET
	@Path("/count")
	@WebMethod
	public GenericEntity<Integer> countItems (
			@QueryParam("r") String pattern, 
			@QueryParam("ot") String onlyTitle,
			@QueryParam("c") String category){
		MainKeyword keyword = StringUtils.isNotEmpty(category) ? keywordsService.getKeywordById(Integer.valueOf(category)) : null;
		Integer itemsFound = itemsService.countItem(pattern, !Boolean.valueOf(onlyTitle), keyword);
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
}
