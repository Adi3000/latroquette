package net.latroquette.web.beans.item;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.profile.User;
import net.latroquette.web.beans.profile.UserBean;

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
	private OfferBean offer;
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
	public OfferBean getOffer() {
		return offer;
	}
	/**
	 * @param offer the offer to set
	 */
	public void setOffer(OfferBean offer) {
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
	
}
