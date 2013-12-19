package net.latroquette.common.database.data.item;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.item.wish.Wish;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.parameters.ParameterName;

import com.adi3000.common.database.hibernate.session.DAO;

public interface ItemsService extends DAO<Item> {

	
	/**
	 * Remove an image from an item
	 * @param cat
	 * @param pattern
	 * @return
	 */
	public void deleteImageFromItem(File image, Item item, User user);
	/**
	 * Search an item via Amazon Webservice by its uid
	 * @param cat
	 * @param pattern
	 * @return
	 */
	public AmazonItem searchAmazonItemById(String id);
	/**
	 * Search an item via Amazon Webservice
	 * @param cat
	 * @param pattern
	 * @return
	 */
	public List<AmazonItem> searchAmazonItems(String cat, String pattern);
	
	/**
	 * Get an item by its Id
	 * @param itemId
	 * @return
	 */
	public Item getItemById(Integer itemId);
	
	/**
	 * Modify an item and update its user modification and its update date
	 * @param item
	 * @param user
	 * @return
	 */
	public Item modifyItem(Item item, User user);
	
	/**
	 * Make a filter from a request map with applying some authorization rules
	 * @param itemFilter
	 * @param parameterMap
	 * @param user
	 */
	public void setFiltersFromParameter(ItemFilter itemFilter, Map<String, String> parameterMap, User user);
	
	/**
	 * Search an item in database, return only {@link ParameterName}.NB_RESULT_TO_LOAD
	 * @param searchString
	 * @param searchOnDescription
	 * @param page
	 * @param countOnly
	 * @return
	 */
	public List<Item> searchItem(ItemFilter itemFilter, Integer page, boolean forAutocomplete);
	
	/**
	 * Return the number of item which match with the searchString request in database
	 * @param searchString
	 * @param searchOnDescription
	 * @param page
	 * @param countOnly
	 * @return
	 */
	public Integer countItem(ItemFilter itemFilter);
	/**
	 * Search item by their status
	 * @param page
	 * @param itemStatus
	 * @return
	 */
	public List<Item> searchItemByStatus(Integer page, ItemStatus itemStatus);
	/**
	 * Search a wishes within multiple resources
	 * @param pattern
	 * @return
	 */
	public List<Wish> searchWishes(String pattern);
	/**
	 * Get the max loaded result by page 
	 * @param searchString
	 * @param searchOnDescription
	 * @param page
	 * @param countOnly
	 * @return
	 */
	@Transactional(readOnly=true)
	public int getNbResultByPage();	
}
