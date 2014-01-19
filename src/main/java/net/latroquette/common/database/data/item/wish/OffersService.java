package net.latroquette.common.database.data.item.wish;

import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;

import com.adi3000.common.database.hibernate.session.DAO;

public interface OffersService extends DAO<Offer> {
	
	/**
	 * Create a new offer from {@code sender} to {@code recipient}
	 * @param offer
	 * @param sender
	 * @param recipient
	 */
	public void createOffer(Offer offer, User sender, User recipient);
	/**
	 * Add a wish to a user
	 * @param newWish
	 * @param user
	 */
	public void addNewWish(WishedItem newWish, User user);

	/**
	 * Return a {@link SuitableItem} from criteria specified
	 * If no match in database, a new {@link WishedItem} is returned
	 * @param id
	 * @param source
	 * @param itemName
	 * @param mainKeyword
	 * @param externalKeyword
	 * @return
	 */
	public SuitableItem getSuitableItem(String id, KeywordSource source, String itemName, 
			MainKeyword mainKeyword, ExternalKeyword externalKeyword);
}
