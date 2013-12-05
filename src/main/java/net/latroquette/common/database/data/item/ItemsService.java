package net.latroquette.common.database.data.item;

import java.util.List;

import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.parameters.ParameterName;

import com.adi3000.common.database.hibernate.session.DAO;

public interface ItemsService extends DAO<Item> {

	
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
	
}
