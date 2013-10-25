package net.latroquette.web.beans.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import net.latroquette.common.util.Services;
import net.latroquette.web.beans.profile.UserBean;
import net.latroquette.web.security.SecurityUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.springframework.util.CollectionUtils;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.web.faces.FacesUtil;

@ManagedBean
@ViewScoped
public class ItemBean implements Serializable {
	
	@ManagedProperty(value=Services.ITEMS_SERVICE_JSF)
	private transient ItemsService itemsService;
	@ManagedProperty(value=Services.FILES_SERVICE_JSF)
	private transient FilesService filesService;
	/**
	 * @param filesService the filesService to set
	 */
	public void setFilesService(FilesService filesService) {
		this.filesService = filesService;
	}
	/**
	 * @param itemsService the itemsService to set
	 */
	public void setItemsService(ItemsService itemsService) {
		this.itemsService = itemsService;
	}

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
		for(File file : fileList){
			file.setGarbageStatus(GarbageFileStatus.VALIDATE);
		}
		item.setImageList(fileList);
		item.setStatusId(ItemStatus.DRAFT);
		item.setDatabaseOperation(operation);
		item = itemsService.modifyItem(item, userBean.getUser());
		return "viewItem?faces-redirect=true&item="+item.getId();
	}
	
	public String uploadPic(){
		File file = filesService.uploadNewPicFile(newFile, userBean.getUser());
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
		newFile = null;
		return null;
	}
	
	public String removePic(String imageId){
		File image = (File) CommonUtils.findById(item.getImageList(), imageId);
		if(item.getId() != null){
			image.setGarbageStatus(GarbageFileStatus.NOT_LINKED);
			filesService.modifyFile(image, userBean.getUser());
		}else{
			filesService.removeFile(image);
		}
		fileList.remove(image);
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
		if(CollectionUtils.isEmpty(fileList) && item != null){
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
			item = itemsService.getItemById(Integer.valueOf(itemId));
		}else if(item == null){
			item = new Item();
		}
	}

	public void checkItemAndUser(){
		//First check if user is logged
		boolean userCheck = SecurityUtil.checkUserLogged(userBean);

		//Next get item and check if user is available to modify this item
		loadItem();
		//Pass to viewItem only if user is logged for this check
		if (item.getId() != null && userCheck && !userBean.getId().equals(item.getUser().getId())){
			FacesUtil.navigationRedirect("viewItem");
		}
	}
	
}
