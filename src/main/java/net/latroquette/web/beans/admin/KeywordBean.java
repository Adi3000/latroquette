package net.latroquette.web.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.Keyword;
import net.latroquette.common.database.data.keyword.Keywords;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.CommonUtils;
import net.latroquette.common.util.optimizer.CommonValues;

import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.lang.StringUtils;

@ManagedBean
@ViewScoped
public class KeywordBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3518984670303172622L;
	private String newKeywordName;
	private String newParentKeywordName;
	private MainKeyword parentKeyword;
	private String keywordId;
	private List<MainKeyword> orphanMainKeywords;
	private List<ExternalKeyword> orphanExternalKeywords;
	private Map<String,String> childrenIds;
	private Map<String,String> synonymsIds;
	private String childrenToAdd;
	private String synonymToAdd;
	private Set<Keyword> modifiedKeywords;
	private List<MainKeyword> breadcrumb;

	
	
	public void init(){
		childrenIds = new HashMap<String,String>();
		synonymsIds = new HashMap<String,String>();
		loadKeyword();
	}
	public void loadKeyword(){
		
		Keywords keywords = new Keywords();
		if(StringUtils.isNotBlank(keywordId)){
			parentKeyword = keywords.getKeywordById(Integer.valueOf(keywordId));
			newParentKeywordName = new String(parentKeyword.getName());
			breadcrumb = new ArrayList<MainKeyword>();
			MainKeyword currentKeyword = parentKeyword.getAncestor();
			while(currentKeyword != null){
				breadcrumb.add(currentKeyword);
				currentKeyword = currentKeyword.getAncestor();
			}
			Collections.reverse(breadcrumb);
		}else{
			parentKeyword = null;
		}
		orphanMainKeywords = keywords.getOrphaMainKeywords();
		orphanExternalKeywords = keywords.getOrphanExternalKeywords();
		keywords.closeSession();
	}
	/**
	 * Create a new keyword
	 */
	public void create(){
		MainKeyword newKeyword = new MainKeyword();
		newKeyword.setName(newKeywordName);
		newKeyword.setIsSynonym(CommonValues.FALSE);
		newKeyword.setInMenu(CommonValues.FALSE);
		newKeyword.setName(newKeywordName);
		if(!isRoot()){
			newKeyword.setAncestor(parentKeyword);
			parentKeyword.getChildren().add(newKeyword);
			parentKeyword.setDatabaseOperation(IDatabaseConstants.UPDATE);
		}
		newKeyword.setDatabaseOperation(IDatabaseConstants.INSERT);
		Keywords keywords = new Keywords();
		keywords.modifyKeyword(newKeyword, parentKeyword);
		keywords.closeSession();
	}
	
	public void save(){

		modifiedKeywords = new HashSet<Keyword>(getKeywordList());
		modifyChildrenRelationship(false);
		modifyChildrenRelationship(true);
		if(!isRoot()){
			modifiedKeywords.add(parentKeyword);
			modifyParentKeywordName();
			modifyRelationship(parentKeyword,childrenToAdd,false);
			modifyRelationship(parentKeyword,synonymToAdd,true);
		}
		Keywords keywords = new Keywords();
		keywords.modifyKeywords(modifiedKeywords);
		keywords.closeSession();
	}
	
	public void remove(Integer childId, Integer parentId){
		if(!isRoot() && parentId.equals(parentKeyword.getId())){
			//TODO manage deleting from here
		}else{
			MainKeyword parent = (MainKeyword) CommonUtils.findById(getKeywordList(), parentId);
			MainKeyword child = (MainKeyword) CommonUtils.findById(parent.getChildren(), childId);
			if(child != null){
				parent.getChildren().remove(child);
				child.setInMenu(CommonUtils.toChar(false));
				child.setIsSynonym(CommonUtils.toChar(false));
				child.setAncestor(null);
				Keywords keywords =  new Keywords();
				keywords.modifyKeyword(child, parent);
				keywords.closeSession();
			}
		}
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
				if(keywordId.startsWith(CommonValues.EXTERNAL_KEYWORD_PREFIX)){
					externalKeywords.add((ExternalKeyword) CommonUtils.findById(orphanExternalKeywords, keywordId.substring(CommonValues.EXTERNAL_KEYWORD_PREFIX.length())));
				}else{
					mainKeywords.add((MainKeyword) CommonUtils.findById(orphanMainKeywords, keywordId));
				}
			}
			for(MainKeyword child : mainKeywords){
				parent.getChildren().add(child);
				child.setAncestor(parent);
				child.setIsSynonym(CommonUtils.toChar(isSynonym));
				parent.setDatabaseOperation(IDatabaseConstants.UPDATE);
				child.setDatabaseOperation(IDatabaseConstants.UPDATE);
				modifiedKeywords.add(child);
				modified |= true;
			}
			if(!externalKeywords.isEmpty()){
				//TODO Implement in ExternalKeyword class the proper association
				for(ExternalKeyword externalKeyword : externalKeywords){
					/*externalKeyword.setDatabaseOperation(IDatabaseConstants.UPDATE);
					modifiedKeywords.add(externalKeyword);
					modified |= true;*/
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
}
