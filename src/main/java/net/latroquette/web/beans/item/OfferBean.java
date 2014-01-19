package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.wish.Offer;
import net.latroquette.common.database.data.item.wish.OffersService;
import net.latroquette.common.database.data.item.wish.SuitableItem;
import net.latroquette.common.database.data.item.wish.WishedItem;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.KeywordType;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.common.util.Services;
import net.latroquette.web.beans.profile.UserBean;

import org.apache.commons.lang3.StringUtils;

import com.adi3000.common.util.CommonUtil;
import com.adi3000.common.util.optimizer.CommonValues;

@ManagedBean
@ViewScoped
public class OfferBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9016870137112241729L;
	private String message;
	@ManagedProperty(value="#{userBean}")
	private UserBean creator;
	@ManagedProperty(value="#{itemBean.item}")
	private Item item;
	private User recipient;
	private Offer offer;
	private String newItem1;
	private String newItem1Source;
	private String newItem1Code;
	private String newItem1KeywordId;
	private List<SuitableItem> itemList1;
	
	@ManagedProperty(value=Services.OFFERS_SERVICE_JSF)
	private OffersService offersService; 
	
	/**
	 * @param offerService the offerService to set
	 */
	public void setOffersService(OffersService offersService) {
		this.offersService = offersService;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the creator
	 */
	public UserBean getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(UserBean creator) {
		this.creator = creator;
	}
	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
	/**
	 * @param itemBean the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	/**
	 * @return the offer
	 */
	public Offer getOffer() {
		return offer;
	}
	/**
	 * @param offer the offer to set
	 */
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	/**
	 * @return the recipient
	 */
	public User getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the newItem1Source
	 */
	public String getNewItem1Source() {
		return newItem1Source;
	}
	/**
	 * @param newItem1Source the newItem1Source to set
	 */
	public void setNewItem1Source(String newItem1Source) {
		this.newItem1Source = newItem1Source;
	}
	/**
	 * @return the newItem1Code
	 */
	public String getNewItem1Code() {
		return newItem1Code;
	}
	/**
	 * @param newItem1Code the newItem1Code to set
	 */
	public void setNewItem1Code(String newItem1Code) {
		this.newItem1Code = newItem1Code;
	}
	/**
	 * @return the newItem1KeywordId
	 */
	public String getNewItem1KeywordId() {
		return newItem1KeywordId;
	}
	/**
	 * @param newItem1KeywordId the newItem1KeywordId to set
	 */
	public void setNewItem1KeywordId(String newItem1KeywordId) {
		this.newItem1KeywordId = newItem1KeywordId;
	}
	/**
	 * @return the itemList1
	 */
	public List<SuitableItem> getItemList1() {
		return itemList1;
	}
	/**
	 * @param itemList1 the itemList1 to set
	 */
	public void setItemList1(List<SuitableItem> itemList1) {
		this.itemList1 = itemList1;
	}

	/**
	 * @return the newItem1
	 */
	public String getNewItem1() {
		return newItem1;
	}
	/**
	 * @param newItem1 the newItem1 to set
	 */
	public void setNewItem1(String newItem1) {
		this.newItem1 = newItem1;
	}
	public String removeItem1(String itemId, String source){
		SuitableItem item = null;
		if(KeywordSource.ITEM_SOURCE.equals(source)){
			item = new Item();
			item.setId(Integer.valueOf(itemId));
		}else if(StringUtils.isNotEmpty(source)){
			item = new WishedItem();
			item.setId(Integer.valueOf(itemId));
		}
		offer.getItemList1().remove(item);
		offersService.modify(offer);
		return null;
	}
	public void addItem1(AjaxBehaviorEvent event){
		if(StringUtils.isNotEmpty(newItem1)){
			MainKeyword mainKeyword = null;
			ExternalKeyword externalKeyword = null;
			if(StringUtils.isNotEmpty(newItem1KeywordId)){
				String[] keywordInfo = newItem1KeywordId.split(CommonValues.INNER_SEPARATOR);
				switch (KeywordType.get(Integer.valueOf(keywordInfo[0]))) {
					case MAIN_KEYWORD:
						mainKeyword = new MainKeyword();
						mainKeyword.setId(Integer.valueOf(keywordInfo[1]));
						break;
					case EXTERNAL_KEYWORD:
						externalKeyword = new ExternalKeyword();
						externalKeyword.setId(Integer.valueOf(keywordInfo[1]));
						break;
					default:
						break;
				}
			}
			SuitableItem item = offersService.getSuitableItem(newItem1Code, KeywordSource.get(newItem1Source), newItem1, 
					mainKeyword, externalKeyword);
			offer.getItemList1().add(item);
			//Re-init field 
			newItem1 = null;
			newItem1Source = null;
			newItem1Code = null;
			newItem1KeywordId = null;
		}
	}
}
