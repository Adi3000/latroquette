package net.latroquette.web.beans.admin;

import java.io.Serializable;

import net.latroquette.common.database.data.keyword.MainKeyword;

import com.adi3000.common.web.faces.ui.tree.Node;

public class MenuNode extends Node<MainKeyword> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1053429173078919787L;

	public MenuNode(Node<MainKeyword> parent, MainKeyword value) {
		super(parent, value);
	}
	
	public MenuNode(MainKeyword value){
		this(null, value);
	}

	@Override
	protected boolean filter(MainKeyword keyword){
		return keyword.getInMenu();
	}
	
	@Override
	protected MenuNode initializaNewChildNode(MainKeyword keyword){
		return new MenuNode(this, keyword);
	}
}
