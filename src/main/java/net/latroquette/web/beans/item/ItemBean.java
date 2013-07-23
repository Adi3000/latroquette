package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.latroquette.common.database.data.file.File;
import net.latroquette.common.database.data.file.FilesService;
import net.latroquette.common.database.data.file.GarbageFileStatus;
import net.latroquette.common.database.data.item.Item;
import net.latroquette.common.database.data.item.ItemStatus;
import net.latroquette.common.database.data.item.ItemsService;
import net.latroquette.web.beans.profile.UserBean;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.web.faces.FacesUtils;

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
	private String itemId;

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
	
	public Item getItem(){
		return item;
	}
	public void setItem(Item item){
		this.item = item;
	}

	public String createItem(){
		return registerItem(DatabaseOperation.INSERT);
	}
	public String updateItem(){
		return registerItem(DatabaseOperation.UPDATE);
	}
	
	private String registerItem(DatabaseOperation operation){
		ItemsService itemsService = new ItemsService();
		for(File file : fileList){
			file.setGarbageStatus(GarbageFileStatus.VALIDATE);
		}
		item.setImageList(fileList);
		item.setStatusId(ItemStatus.DRAFT);
		item.setDatabaseOperation(operation);
		item = itemsService.modifyItem(item, userBean.getUser());
		itemsService.close();
		return "viewItem?faces-redirect=true&item="+item.getId();
	}
	
	public String uploadPic(){
		FilesService files = new FilesService();
		File file = files.uploadNewPicFile(newFile, userBean.getUser());
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
		files.close();
		newFile = null;
		return null;
	}
	
	public String removePic(String imageId){
		File image = (File) CommonUtils.findById(item.getImageList(), imageId);
		FilesService filesService = new FilesService();
		if(item.getId() != null){
			image.setGarbageStatus(GarbageFileStatus.NOT_LINKED);
			filesService.modifyFile(image, userBean.getUser());
		}else{
			filesService.removeFile(image);
		}
		fileList.remove(image);
		filesService.close();
		return null;
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
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public void loadItem(){
		if(StringUtils.isNotEmpty(itemId) ){
			ItemsService itemSearch = new ItemsService();
			item = itemSearch.getItemById(Integer.valueOf(itemId));
			itemSearch.close();
		}else if(item == null){
			item = new Item();
		}
	}

	public void checkItemAndUser(){
		//First check if user is logged
		boolean userCheck = UserBean.checkUserLogged(userBean);

		//Next get item and check if user is available to modify this item
		loadItem();
		//Pass to viewItem only if user is logged for this check
		if (item.getId() != null && userCheck && !userBean.getId().equals(item.getUser().getId())){
			FacesUtils.navigationRedirect("viewItem");
		}
	}
	
}
