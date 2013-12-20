package net.latroquette.common.database.data.item.wish;

import net.latroquette.common.database.data.item.ItemList;
import net.latroquette.common.database.data.profile.User;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

public class Offer extends AbstractDataObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3847188833649436183L;
	private Integer id;
	private User creator;
	private User recipient;
	private ItemList itemList1;
	private ItemList itemList2;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the itemList1
	 */
	public ItemList getItemList1() {
		return itemList1;
	}
	/**
	 * @param itemList1 the itemList1 to set
	 */
	public void setItemList1(ItemList itemList1) {
		this.itemList1 = itemList1;
	}
	/**
	 * @return the itemList2
	 */
	public ItemList getItemList2() {
		return itemList2;
	}
	/**
	 * @param itemList2 the itemList2 to set
	 */
	public void setItemList2(ItemList itemList2) {
		this.itemList2 = itemList2;
	}
	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
}
