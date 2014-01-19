package net.latroquette.common.database.data.item.wish;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.database.data.profile.UsersService;
import net.latroquette.common.util.Services;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.database.hibernate.session.AbstractDAO;
import com.adi3000.common.database.spring.TransactionalUpdate;

@Service(value=Services.OFFERS_SERVICE)
public class OffersServiceImpl extends AbstractDAO<Offer> implements OffersService{

	public OffersServiceImpl() {
		super(Offer.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2286250823221513101L;

	@Inject
	private WishedItemDAO wishedItemDAO;
	@Inject
	private ItemsService itemsService;
	@Inject
	private KeywordsService keywordsService;
	@Inject
	private UsersService usersService;
	
	/**
	 * @param wishedItemDAO the wishedItemDAO to set
	 */
	public void setWishedItemDAO(WishedItemDAO wishedItemDAO) {
		this.wishedItemDAO = wishedItemDAO;
	}

	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}

	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}

	/**
	 * @param usersService the usersService to set
	 */
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	@TransactionalUpdate
	public void addNewWish(WishedItem newWish, User user){
		if(StringUtils.isEmpty(newWish.getSource())){
			newWish.setSource(KeywordSource.WISHES_SOURCE.getSourceId());
			if(newWish.getExternalKeyword() != null){
				newWish.setExternalKeyword(
						keywordsService.getExternalKeywordById(newWish.getExternalKeyword().getId())
				);
			}else if(newWish.getMainKeyword() != null){
				newWish.setMainKeyword(
						keywordsService.getKeywordById(newWish.getMainKeyword().getId())
				);
			}
		}else{
			WishedItem wish = (WishedItem) createCriteria(WishedItem.class)
					.add(Restrictions.eq("uid",newWish.getUid()))
					.add(Restrictions.eq("source",newWish.getSource()))
					.setMaxResults(1)
					.uniqueResult();
			if(wish != null){
				newWish = wish;
			}
		}
		user.getWishesSet().add(newWish);
		usersService.updateUser(user);
	}	
	
	private WishedItem getWishedItem(WishedItem newWish){
		if(StringUtils.isEmpty(newWish.getSource())){
			newWish.setSource(KeywordSource.WISHES_SOURCE.getSourceId());
			if(newWish.getExternalKeyword() != null){
				newWish.setExternalKeyword(
						keywordsService.getExternalKeywordById(newWish.getExternalKeyword().getId())
				);
			}else if(newWish.getMainKeyword() != null){
				newWish.setMainKeyword(
						keywordsService.getKeywordById(newWish.getMainKeyword().getId())
				);
			}
		}else{
			WishedItem wish = (WishedItem) createCriteria(WishedItem.class)
					.add(Restrictions.eq("uid",newWish.getUid()))
					.add(Restrictions.eq("source",newWish.getSource()))
					.setMaxResults(1)
					.uniqueResult();
			if(wish != null){
				newWish = wish;
			}
		}
		return newWish;
	}

	@Transactional(readOnly=false)
	public SuitableItem getSuitableItem(String id, KeywordSource source, String itemName, 
			MainKeyword mainKeyword, ExternalKeyword externalKeyword){
		SuitableItem item = null;
		switch (source) {
		case ITEM_SOURCE :
			item = itemsService.get(Integer.valueOf(id));
			break;
		case WISHES_SOURCE :
			item = wishedItemDAO.get(Integer.valueOf(id));
			break;
		case AMAZON_SOURCE :
		case MAIN_KEYWORD_SOURCE :
		case EXTERNAL_KEYWORD_SOURCE :
			WishedItem newWish = new WishedItem();
			newWish.setUid(id);
			newWish.setSource(source.getSourceId());
			newWish.setName(itemName);
			newWish.setMainKeyword(mainKeyword);
			newWish.setExternalKeyword(externalKeyword);
			item = getWishedItem(newWish);
			break;
		default:
			break;
		}
		return item;
	}
	
	public void createOffer(Offer offer, User sender, User recipient){
		offer.setCreator(sender);
		offer.setRecipient(recipient);
		offer.setDatabaseOperation(DatabaseOperation.INSERT);
		super.modify(offer);
	}
}
