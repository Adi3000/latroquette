package net.latroquette.web.beans.admin;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import net.latroquette.common.database.data.keyword.KeywordsService;
import net.latroquette.common.database.data.keyword.MainKeyword;

import com.adi3000.common.util.tree.TreeNodeList;
import com.adi3000.common.web.faces.ui.tree.Node;

@ManagedBean(eager=true)
@ApplicationScoped
public class MenuBean {

	private List<Node<MainKeyword>> orderedNodelist;
	private TreeNodeList<MainKeyword> treeNodeList;

	@PostConstruct
	public void reloadMenuEntries(){
		treeNodeList = new TreeNodeList<MainKeyword>(KeywordsService.getRootForMenu());
		KeywordsService keywordsService  = new KeywordsService();
		treeNodeList.getRootNode().addChildren(keywordsService.getMenuRootEntries());
		keywordsService.closeSession();
		orderedNodelist = treeNodeList.getTreeNodeList();
	}
	/**
	 * @return the menuEntries
	 */
	public List<Node<MainKeyword>> getMenuEntries() {
		return orderedNodelist;
	}
	

}
