package net.latroquette.web.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;

import org.apache.commons.lang.StringUtils;

import com.adi3000.common.database.hibernate.DatabaseOperation;
import com.adi3000.common.util.CommonUtils;
import com.adi3000.common.util.optimizer.CommonValues;
import com.adi3000.common.util.tree.Breadcrumb;

@ManagedBean
@ViewScoped
public class KeywordBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3518984670303172622L;
	@ManagedProperty(value="#{menuBean}")
	private MenuBean menuBean;
	private String newKeywordName;
	private String newParentKeywordName;
	private MainKeyword parentKeyword;
	private String keywordId;
	private List<MainKeyword> orphanMainKeywords;
	private List<ExternalKeyword> orphanExternalKeywords;
	private List<MainKeyword> additionnalMainKeywords;
	private List<ExternalKeyword> additionnalExternalKeywords;
	private Map<String,String> childrenIds;
	private Map<String,String> synonymsIds;
	private String childrenToAdd;
	private String synonymToAdd;
	private Set<Keyword> modifiedKeywords;
	private List<MainKeyword> breadcrumb;
	private String keywordToSearch;
	private boolean createOnSearch;

	
	
	public void init(){
		childrenIds = new HashMap<String,String>();
		synonymsIds = new HashMap<String,String>();
		loadKeyword();
	}
	public void loadKeyword(){
		
		KeywordsService keywordsService = new KeywordsService();
		if(StringUtils.isNotBlank(keywordId)){
			parentKeyword = keywordsService.getKeywordById(Integer.valueOf(keywordId), true);
			newParentKeywordName = new String(parentKeyword.getName());
			breadcrumb = new Breadcrumb<MainKeyword>(parentKeyword).getBreadcrumb();
		}else{
			parentKeyword = null;
		}
		orphanMainKeywords = keywordsService.getOrphanMainKeywords();
		orphanExternalKeywords = keywordsService.getOrphanExternalKeywords();
		if(additionnalMainKeywords == null){
			additionnalMainKeywords = new ArrayList<MainKeyword>();
		}
		keywordsService.close();
	}
	
	public String searchKeyword(){
		KeywordsService keywordsService = new KeywordsService();
		additionnalMainKeywords.addAll(keywordsService.searchMainKeyword(keywordToSearch));
		keywordsService.close();
		return null;
	}
	/**
	 * Create a new {@link MainKeyword}
	 */
	public String create(){
		MainKeyword newKeyword = new MainKeyword();
		newKeyword.setName(newKeywordName);
		newKeyword.setIsSynonym(CommonValues.FALSE);
		newKeyword.setInMenu(CommonValues.FALSE);
		newKeyword.setName(newKeywordName);
		if(!isRoot()){
			newKeyword.setAncestor(parentKeyword);
			parentKeyword.getChildren().add(newKeyword);
			parentKeyword.setDatabaseOperation(DatabaseOperation.UPDATE);
		}
		newKeyword.setDatabaseOperation(DatabaseOperation.INSERT);
		KeywordsService keywordsService = new KeywordsService();
		keywordsService.modifyKeyword(newKeyword, parentKeyword);
		keywordsService.close();
		
		return null;
	}
	
	/**
	 * Remove {@link Keyword} child from its parent
	 * @param childId
	 * @param parentId
	 * @return
	 */
	public String remove(Integer childId, Integer parentId){
		if(!isRoot() && parentId.equals(parentKeyword.getId())){
			//TODO manage deleting from here deleting the current categorie
		}else{
			MainKeyword parent = (MainKeyword) CommonUtils.findById(getKeywordList(), parentId);
			MainKeyword child = (MainKeyword) CommonUtils.findById(parent.getChildren(), childId);
			if(child != null){
				parent.getChildren().remove(child);
				child.setInMenu(CommonUtils.toChar(false));
				child.setIsSynonym(CommonUtils.toChar(false));
				child.setAncestor(null);
				KeywordsService keywordsService =  new KeywordsService();
				keywordsService.modifyKeyword(child, parent);
				keywordsService.close();
			}
		}
		return null;
	}
	
	/**
	 * Delete a {@link MainKeyword} from database
	 * @param keywordId
	 * @return
	 */
	public String deleteMainKeyword(Integer keywordId){
		MainKeyword keyword = (MainKeyword) CommonUtils.findById(orphanMainKeywords, keywordId);
		KeywordsService keywordsService = new KeywordsService();
		keywordsService.deleteKeyword(keyword);
		orphanMainKeywords = keywordsService.getOrphanMainKeywords();
		keywordsService.close();
		return null;
	}
	
	/**
	 * Exclude an {@link ExternalKeyword} from the view
	 * @param keywordId
	 * @return
	 */
	public String excludeExternalKeyword(Integer keywordId){
		ExternalKeyword keyword = (ExternalKeyword) CommonUtils.findById(orphanExternalKeywords, keywordId);
		KeywordsService keywordsService = new KeywordsService();
		keywordsService.excludeExternalKeyword(keyword);
		orphanExternalKeywords = keywordsService.getOrphanExternalKeywords();
		keywordsService.close();
		return null;
	}
	
	/**
	 * Save the actual configuration of {@link Keyword} elements
	 * @return
	 */
	public String save(){
		
		modifiedKeywords = new HashSet<Keyword>(getKeywordList());
		modifyChildrenRelationship(false);
		modifyChildrenRelationship(true);
		if(!isRoot()){
			modifiedKeywords.add(parentKeyword);
			modifyParentKeywordName();
			modifyRelationship(parentKeyword,childrenToAdd,false);
			modifyRelationship(parentKeyword,synonymToAdd,true);
		}
		KeywordsService keywords = new KeywordsService();
		keywords.modifyKeywords(modifiedKeywords);
		keywords.close();
		//Rebuild menu if something has changed
		menuBean.reloadMenuEntries();
		return null;
	}
	
	private boolean modifyParentKeywordName(){
		boolean modified = false;
		if(!isRoot() && StringUtils.isNotBlank(newKeywordName) && ! parentKeyword.getName().equals(newKeywordName)){
			parentKeyword.setName(newKeywordName);
			modified = true;
		}
		return modified;
	}
	
	private boolean modifyRelationship(MainKeyword parent, String ids, boolean isSynonym){
		boolean modified = false;
		if(StringUtils.isNotEmpty(ids)){
			List<String> parsedKeywordIds = CommonUtils.parseStringToList(ids);
			List<ExternalKeyword> externalKeywords = new ArrayList<ExternalKeyword>();
			List<MainKeyword> mainKeywords = new ArrayList<MainKeyword>();
			for(String keywordId : parsedKeywordIds){
				//Id is an additionnal+external keyword
				if(keywordId.startsWith(WebConstantsBean.ADDITIONNAL_EXTERNAL_KEYWORD_PREFIX)){
					externalKeywords.add((ExternalKeyword) CommonUtils.findById(additionnalExternalKeywords, keywordId.substring(WebConstantsBean.ADDITIONNAL_EXTERNAL_KEYWORD_PREFIX.length())));
				//Id is an additionnal+main keyword
				}else if(keywordId.startsWith(WebConstantsBean.ADDITIONNAL_KEYWORD_PREFIX)){
					mainKeywords.add((MainKeyword) CommonUtils.findById(additionnalMainKeywords, keywordId.substring(WebConstantsBean.ADDITIONNAL_KEYWORD_PREFIX.length())));
				//Id is an external orphan keyword
				}else if(keywordId.startsWith(WebConstantsBean.EXTERNAL_KEYWORD_PREFIX)){
					externalKeywords.add((ExternalKeyword) CommonUtils.findById(orphanExternalKeywords, keywordId.substring(WebConstantsBean.EXTERNAL_KEYWORD_PREFIX.length())));
				//Id is a main orphan keyword
				}else{
					mainKeywords.add((MainKeyword) CommonUtils.findById(orphanMainKeywords, keywordId));
				}
			}
			for(MainKeyword child : mainKeywords){
				parent.getChildren().add(child);
				child.setAncestor(parent);
				child.setIsSynonym(CommonUtils.toChar(isSynonym));
				parent.setDatabaseOperation(DatabaseOperation.UPDATE);
				child.setDatabaseOperation(DatabaseOperation.UPDATE);
				modifiedKeywords.add(child);
				modified |= true;
			}
			if(!externalKeywords.isEmpty()){
				for(ExternalKeyword externalKeyword : externalKeywords){
					parent.getExternalKeywords().add(externalKeyword);
					modifiedKeywords.add(parent);
					modified |= true;
				}
			}
		}
		return modified;
	}
	
	private boolean modifyChildrenRelationship(boolean isSynonym){
		boolean modified = false;
		List<MainKeyword> parentList = getKeywordList();
		Map<String,String> childrenMap = isSynonym ? synonymsIds : childrenIds;
		MainKeyword parent = null; 
		String childrenList = null;
		for(Entry<String, String> entry : childrenMap.entrySet()){
			parent = (MainKeyword) CommonUtils.findById(parentList, entry.getKey());
			childrenList = entry.getValue();
			modified |= modifyRelationship(parent,childrenList,isSynonym);
		}
		return modified;
	}
	
	public List<MainKeyword> getKeywordList(){
		return isRoot() ? orphanMainKeywords : parentKeyword.getChildren();
	}
	
	/**
	 * @return the newKeywordName
	 */
	public String getNewKeywordName() {
		return newKeywordName;
	}
	/**
	 * @param newKeywordName the newKeywordName to set
	 */
	public void setNewKeywordName(String newKeywordName) {
		this.newKeywordName = newKeywordName;
	}
	/**
	 * @return the parentKeyword
	 */
	public MainKeyword getParentKeyword() {
		return parentKeyword;
	}
	/**
	 * @param parentKeyword the parentKeyword to set
	 */
	public void setParentKeyword(MainKeyword parentKeyword) {
		this.parentKeyword = parentKeyword;
	}
	/**
	 * @return the keywordId
	 */
	public String getKeywordId() {
		return keywordId;
	}
	/**
	 * @param keywordId the keywordId to set
	 */
	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}
	
	/**
	 * @return the orphanMainKeywords
	 */
	public List<MainKeyword> getOrphanMainKeywords() {
		return orphanMainKeywords;
	}
	/**
	 * @param orphanMainKeywords the orphanMainKeywords to set
	 */
	public void setOrphanMainKeywords(List<MainKeyword> orphanMainKeywords) {
		this.orphanMainKeywords = orphanMainKeywords;
	}
	/**
	 * @return the orphanExternalKeywords
	 */
	public List<ExternalKeyword> getOrphanExternalKeywords() {
		return orphanExternalKeywords;
	}
	/**
	 * @param orphanExternalKeywords the orphanExternalKeywords to set
	 */
	public void setOrphanExternalKeywords(List<ExternalKeyword> orphanExternalKeywords) {
		this.orphanExternalKeywords = orphanExternalKeywords;
	}

	/**
	 * @return the childrenIds
	 */
	public Map<String,String> getChildrenIds() {
		return childrenIds;
	}

	/**
	 * @param childrenIds the childrenIds to set
	 */
	public void setChildrenIds(Map<String,String> childrenIds) {
		this.childrenIds = childrenIds;
	}

	/**
	 * @return the childrenToAdd
	 */
	public String getChildrenToAdd() {
		return childrenToAdd;
	}

	/**
	 * @param childrenToAdd the childrenToAdd to set
	 */
	public void setChildrenToAdd(String childrenToAdd) {
		this.childrenToAdd = childrenToAdd;
	}

	/**
	 * @return the synonymToAdd
	 */
	public String getSynonymToAdd() {
		return synonymToAdd;
	}

	/**
	 * @param synonymToAdd the synonymToAdd to set
	 */
	public void setSynonymToAdd(String synonymToAdd) {
		this.synonymToAdd = synonymToAdd;
	}

	/**
	 * @return the synonymsIds
	 */
	public Map<String,String> getSynonymsIds() {
		return synonymsIds;
	}

	/**
	 * @param synonymsIds the synonymsIds to set
	 */
	public void setSynonymsIds(Map<String,String> synonymsIds) {
		this.synonymsIds = synonymsIds;
	}

	/**
	 * @return the newParentKeywordName
	 */
	public String getNewParentKeywordName() {
		return newParentKeywordName;
	}

	/**
	 * @param newParentKeywordName the newParentKeywordName to set
	 */
	public void setNewParentKeywordName(String newParentKeywordName) {
		this.newParentKeywordName = newParentKeywordName;
	}

	public boolean isRoot(){
		return parentKeyword == null;
	}

	/**
	 * @return the breadcrumb
	 */
	public List<MainKeyword> getBreadcrumb() {
		return breadcrumb;
	}

	/**
	 * @param breadcrumb the breadcrumb to set
	 */
	public void setBreadcrumb(List<MainKeyword> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}
	/**
	 * @return the menuBean
	 */
	public MenuBean getMenuBean() {
		return menuBean;
	}
	/**
	 * @param menuBean the menuBean to set
	 */
	public void setMenuBean(MenuBean menuBean) {
		this.menuBean = menuBean;
	}
	/**
	 * @return the additionnalMainKeywords
	 */
	public List<MainKeyword> getAdditionnalMainKeywords() {
		return additionnalMainKeywords;
	}
	/**
	 * @param additionnalMainKeywords the additionnalMainKeywords to set
	 */
	public void setAdditionnalMainKeywords(List<MainKeyword> additionnalMainKeywords) {
		this.additionnalMainKeywords = additionnalMainKeywords;
	}
	/**
	 * @return the additionnalExternalKeywords
	 */
	public List<ExternalKeyword> getAdditionnalExternalKeywords() {
		return additionnalExternalKeywords;
	}
	/**
	 * @param additionnalExternalKeywords the additionnalExternalKeywords to set
	 */
	public void setAdditionnalExternalKeywords(
			List<ExternalKeyword> additionnalExternalKeywords) {
		this.additionnalExternalKeywords = additionnalExternalKeywords;
	}
	/**
	 * @return the createOnSearch
	 */
	public boolean isCreateOnSearch() {
		return createOnSearch;
	}
	/**
	 * @param createOnSearch the createOnSearch to set
	 */
	public void setCreateOnSearch(boolean createOnSearch) {
		this.createOnSearch = createOnSearch;
	}
	/**
	 * @return the keywordToSearch
	 */
	public String getKeywordToSearch() {
		return keywordToSearch;
	}
	/**
	 * @param keywordToSearch the keywordToSearch to set
	 */
	public void setKeywordToSearch(String keywordToSearch) {
		this.keywordToSearch = keywordToSearch;
	}
}
