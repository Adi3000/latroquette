/**
 * 
 */
package net.latroquette.common.database.data.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import net.latroquette.common.database.data.item.wish.SuitableItem;
import net.latroquette.common.database.data.item.wish.WishedItem;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.profile.User;

import org.apache.commons.lang.NotImplementedException;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

/**
 * @author XJBU125
 *
 */
public class ItemList extends AbstractDataObject implements Collection<SuitableItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5980782137746050430L;
	private Integer id;
	private Set<Item> itemsList;
	private Set<WishedItem> wisheslist;
	private Collection<SuitableItem> list;
	private User user;
	private Date creationDate;

	/**
	 * @return the itemsList
	 */
	public Set<Item> getItemsList() {
		return itemsList;
	}

	/**
	 * @param itemsList the itemsList to set
	 */
	public void setItemsList(Set<Item> itemsList) {
		this.itemsList = itemsList;
	}

	/**
	 * @return the wisheslist
	 */
	public Set<WishedItem> getWisheslist() {
		return wisheslist;
	}

	/**
	 * @param wisheslist the wisheslist to set
	 */
	public void setWisheslist(Set<WishedItem> wisheslist) {
		this.wisheslist = wisheslist;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(Collection<SuitableItem> list) {
		this.clear();
		this.addAll(list);
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/* (non-Javadoc)
	 * @see com.adi3000.common.database.hibernate.data.AbstractDataObject#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public int size() {
		return getItemsList().size() + getWisheslist().size();
	}

	@Override
	public boolean isEmpty() {
		return getItemsList().isEmpty() && getWisheslist().isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		SuitableItem item =(SuitableItem) o;
		switch (KeywordSource.get(item.getSource())) {
			case ITEM_SOURCE:
				return getItemsList().contains((Item)item);
			case WISHES_SOURCE:
				return getWisheslist().contains((WishedItem)item);
			default:
				throw new IllegalArgumentException("Invalid SuitableItem from : " + item.getSource());
		}
	}
	private Collection<SuitableItem> getList(){
		list = new ArrayList<>(this.size());
		list.addAll(getItemsList());
		list.addAll(getWisheslist());
		return  list;
	}
	
	@Override
	public Iterator<SuitableItem> iterator() {
		return getList().iterator();
	}

	@Override
	public Object[] toArray() {
		
		return  getList().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getList().toArray(a);
	}

	@Override
	public boolean add(SuitableItem e) {
		if(e == null){
			return false;
		}
		switch (KeywordSource.get(e.getSource())) {
		case ITEM_SOURCE:
			return getItemsList().add((Item)e);
		case WISHES_SOURCE:
			return getWisheslist().add((WishedItem)e);
		default:
			throw new IllegalArgumentException("Invalid SuitableItem from : " + e.getSource());
		}
	}

	@Override
	public boolean remove(Object o) {
		boolean result = false;
		SuitableItem item =(SuitableItem) o;
		if(item != null){
			switch (KeywordSource.get(item.getSource())) {
				case ITEM_SOURCE:
					result |= getItemsList().remove((Item)item);
				case WISHES_SOURCE:
					result |= getWisheslist().remove((WishedItem)item);
				default:
					throw new IllegalArgumentException("Invalid SuitableItem from : " + item.getSource());
			}
		}else{
			result |= false;
		}
		return result;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if(c == null){
			return false;
		}
		boolean result = true;
		SuitableItem item = null;
		for(Object o : c){
			item =(SuitableItem) o;
			if(item != null){
				result &= this.contains(item);
			}else{
				result &= false;
			}
		}
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends SuitableItem> c) {
		if(c == null){
			return false;
		}
		boolean result = true;
		for(SuitableItem item: c){
			if(item != null){
				result &= this.add(item);
			}else{
				result &= false;
			}
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if(c == null){
			return false;
		}
		boolean result = true;
		SuitableItem item = null;
		for(Object o : c){
			item =(SuitableItem) o;
			if(item != null){
				result &= this.remove(item);
			}else{
				result &= false;
			}
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new NotImplementedException("This can't be done yet");
	}

	@Override
	public void clear() {
		getItemsList().clear();
		getWisheslist().clear();
	}

}
