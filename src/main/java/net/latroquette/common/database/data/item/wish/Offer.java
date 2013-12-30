package net.latroquette.common.database.data.item.wish;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.latroquette.common.database.data.item.ItemList;
import net.latroquette.common.database.data.profile.User;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Table(name="offers")
@SequenceGenerator(name = "offers_offer_id_seq", sequenceName = "offers_offer_id_seq", allocationSize=1)
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
	@Id
	@Column(name="offer_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offers_offer_id_seq")
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
	@Column(name="user_recipent_id")
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
	@JoinColumn(name="item_list_1_id")
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
	@JoinColumn(name="item_list_2_id")
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
	@Column(name="user_creator_id")
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
