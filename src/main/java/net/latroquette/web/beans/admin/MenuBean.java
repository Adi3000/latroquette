package net.latroquette.web.beans.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.common.util.Services;

import com.adi3000.common.web.faces.ui.tree.Node;
import com.adi3000.common.web.faces.ui.tree.TreeNodeList;


@ManagedBean(eager=true)
@ApplicationScoped
public class MenuBean implements Serializable{

	@ManagedProperty(value=Services.KEYWORDS_SERVICE_JSF)
	private transient KeywordsService keywordsService;
	
	/**
	 * @param keywordsService the keywordsService to set
	 */
	public void setKeywordsService(KeywordsService keywordsService) {
		this.keywordsService = keywordsService;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2537528425083329571L;
	private List<Node<MainKeyword>> orderedNodelist;
	private TreeNodeList<MainKeyword> treeNodeList;
	private List<MainKeyword> menuRootEntries;
	private List<MainKeyword> rootCategoriesEntries;

	@PostConstruct
	public void reloadMenuEntries(){
		MenuNode rootNode = new MenuNode(keywordsService.getRootForMenu());
		treeNodeList = new TreeNodeList<MainKeyword>(rootNode);
		menuRootEntries = keywordsService.getMenuRootEntries();
		rootCategoriesEntries = keywordsService.getRootCategoriesEntries();
		treeNodeList.getRootNode().addChildren(keywordsService.getMenuRootEntries());
		orderedNodelist = treeNodeList.getTreeNodeList();
	}
	/**
	 * @return the menuEntries
	 */
	public List<Node<MainKeyword>> getMenuEntries() {
		orderedNodelist = treeNodeList.getTreeNodeList();
		return orderedNodelist;
	}
	
	/**
	 * @return the menuRootEntries
	 */
	public List<MainKeyword> getMenuRootEntries() {
		return menuRootEntries;
	}
	/**
	 * @return the rootCategoriesEntries
	 */
	public List<MainKeyword> getRootCategoriesEntries() {
		return rootCategoriesEntries;
	}
	
	

}
