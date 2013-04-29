package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemStatus;
import net.latroquette.common.database.data.item.Items;
import net.latroquette.common.utils.CommonUtils;
import net.latroquette.web.beans.profile.UserBean;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean
@ViewScoped
public class ItemBean implements Serializable {
	
	public static Logger LOG = Logger.getLogger(ItemBean.class.toString());
	/**
	 * 
	 */
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	

	private static final long serialVersionUID = 8400549541055176853L;
	private Item item;
	private UploadedFile file1;
	private UploadedFile file2;
	private UploadedFile file3;
	private List<String> wishies;
	private List<String> keywords;
	
	@PostConstruct
	public void init(){
		Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String itemId = parameterMap.get("item");
		if(StringUtils.isNotEmpty(itemId) ){
			Items itemSearch = new Items();
			item = itemSearch.getItemById(itemId);
		}else if(item == null){
			item = new Item();
		}
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return item.getTitle();
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.item.setTitle(title);
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return item.getDescription();
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.item.setDescription(description);
	}
	/**
	 * @return the file1
	 */
	public UploadedFile getFile1() {
		return file1;
	}
	/**
	 * @param file1 the file1 to set
	 */
	public void setFile1(UploadedFile file1) {
		this.file1 = file1;
	}
	/**
	 * @return the file2
	 */
	public UploadedFile getFile2() {
		return file2;
	}
	/**
	 * @param file2 the file2 to set
	 */
	public void setFile2(UploadedFile file2) {
		this.file2 = file2;
	}
	/**
	 * @return the file3
	 */
	public UploadedFile getFile3() {
		return file3;
	}
	/**
	 * @param file3 the file3 to set
	 */
	public void setFile3(UploadedFile file3) {
		this.file3 = file3;
	}

	public String createItem(){
		item.setStatusId(ItemStatus.DRAFT);
		Items items = new Items();
		item = items.modifyItem(item, userBean);
		return null;
	}
	
	/*public String uploadPic(int id){
		//TODO : Create small and resized image
		//TODO : Drop the rest
		return null;
	}*/
	
	/*private boolean itemFound(){
		return item != null;
	}*/
	
	public void setKeywordListString(String keywordsListString){
		keywords = CommonUtils.parseStringToList(keywordsListString);
	}
	
	public String getKeywordListString(){
		return CommonUtils.formatListToString(keywords);
	}
	
	public void setWishiesListString(String wishiesListString){
		wishies = CommonUtils.parseStringToList(wishiesListString);
	}
	
	public String getWishiesListString(){
		return CommonUtils.formatListToString(wishies);
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
}
