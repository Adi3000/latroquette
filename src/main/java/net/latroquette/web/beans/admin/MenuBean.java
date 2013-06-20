package net.latroquette.web.beans.admin;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;

import com.adi3000.common.web.faces.ui.tree.Node;
import com.adi3000.common.web.faces.ui.tree.TreeNodeList;

@ManagedBean(eager=true)
@ApplicationScoped
public class MenuBean {

	private List<Node<MainKeyword>> orderedNodelist;
	private TreeNodeList<MainKeyword> treeNodeList;

	@PostConstruct
	public void reloadMenuEntries(){
		MenuNode rootNode = new MenuNode(KeywordsService.getRootForMenu());
		treeNodeList = new TreeNodeList<MainKeyword>(rootNode);
		KeywordsService keywordsService  = new KeywordsService();
		treeNodeList.getRootNode().addChildren(keywordsService.getMenuRootEntries());
		keywordsService.close();
		orderedNodelist = treeNodeList.getTreeNodeList();
	}
	/**
	 * @return the menuEntries
	 */
	public List<Node<MainKeyword>> getMenuEntries() {
		orderedNodelist = treeNodeList.getTreeNodeList();
		return orderedNodelist;
	}
	

}
