package net.latroquette.web.beans.item;

import java.util.List;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.keyword.Keyword;

public class NewItemBean {
	
	private String title;
	private File[] imagesList;
	private String description;
	private List<Keyword> keyWordList;
	private Keyword category;
	private Keyword wishies;
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the imagesList
	 */
	public File[] getImagesList() {
		return imagesList;
	}
	/**
	 * @param imagesList the imagesList to set
	 */
	public void setImagesList(File[] imagesList) {
		this.imagesList = imagesList;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the keyWordList
	 */
	public List<Keyword> getKeyWordList() {
		return keyWordList;
	}
	/**
	 * @param keyWordList the keyWordList to set
	 */
	public void setKeyWordList(List<Keyword> keyWordList) {
		//TODO : if Amazon object create a over keyword without label
		this.keyWordList = keyWordList;
	}
	/**
	 * @return the category
	 */
	public Keyword getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Keyword category) {
		this.category = category;
	}
	/**
	 * @return the wishies
	 */
	public Keyword getWishies() {
		return wishies;
	}
	/**
	 * @param wishies the wishies to set
	 */
	public void setWishies(Keyword wishies) {
		this.wishies = wishies;
	}
	
	public String submit(){
		//TODO : Check if wishies are up to date
		//TODO : Strip/Check title + title is required
		//TODO : Strip/Check description
		//TODO : create object
		//TODO : Associate keyword with object
		return null;
	}
	
	public String uploadPic(int id){
		//TODO : Create small and resized image
		//TODO : Drop the rest
		return null;
	}

}
