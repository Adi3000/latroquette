package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.file.Files;
import net.latroquette.common.database.data.file.GarbageFileStatus;
import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemStatus;
import net.latroquette.common.database.data.item.Items;
import net.latroquette.common.security.Security;
import net.latroquette.common.util.CommonUtils;
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
	private UploadedFile newFile;
	private List<String> wishies;
	private List<String> keywords;
	private List<File> fileList;
	
	@PostConstruct
	public void init(){
		Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String itemId = parameterMap.get("item");
		if(StringUtils.isNotEmpty(itemId) ){
			Items itemSearch = new Items();
			item = itemSearch.getItemById(Integer.valueOf(itemId));
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
	public UploadedFile getFile() {
		return newFile;
	}
	/**
	 * @param file1 the file1 to set
	 */
	public void setNewFile(UploadedFile file) {
		this.newFile = file;
	}
	
	public String createItem(){
		Items items = new Items();
		for(File file : fileList){
			file.setGarbageStatus(GarbageFileStatus.VALIDATE);
		}
		item.setImageList(fileList);
		item.setStatusId(ItemStatus.DRAFT);
		item.setDatabaseOperation(IDatabaseConstants.INSERT);
		item = items.modifyItem(item, userBean);
		items.closeSession();
		return "viewItem?faces-redirect=true&item="+item.getId();
	}
	
	public void uploadPic(){
		Files files = new Files();
		File file = files.uploadNewPicFile(newFile, userBean);
		if(file == null){
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage("Uploading error", 
					"Adding file : upload failed, please try later or ask for support");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, msg);
			fc.validationFailed();
		}else{
			getFileList().add(file);
		}
		files.closeSession();
		newFile = null;
	}
	
	public void removePic(File image){
		Files files = new Files();
		fileList.remove(fileList.indexOf(image));
		files.removeFile(image);
	}
	
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
	
	public List<File> getFileList(){
		if(this.fileList == null && item != null){
			this.fileList = item.getImageList();
		}
		if(this.fileList == null){
			this.fileList = new ArrayList<File>();
		}
		return this.fileList;
	}
	
	public void setFileList(List<File> fileList){
		this.fileList = fileList;
	}
	
	public UploadedFile getNewFile(){
		return newFile;
	}
	
	public void checkItemForUser(){
		//First check if user is logged
		userBean.checkUserLogged();
		//Next check if user is available to modify this item
		if (!userBean.getId().equals(item.getUser().getId())){
			ConfigurableNavigationHandler nav 
			   = (ConfigurableNavigationHandler) 
					   FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			nav.performNavigation("viewItem");
		}
	}
	
}
