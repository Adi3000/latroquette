package net.latroquette.common.database.data.item;

public class AmazonBrowseNode {
	
	private AmazonBrowseNode ancestor;
	private String browseNodeId;
	private String name;
	
	public AmazonBrowseNode(AmazonBrowseNode ancestor,String browseNodeId, String name){
		this.ancestor = ancestor;
		this.name = name;
		this.browseNodeId = browseNodeId;
	}
	/**
	 * @return the ancestor
	 */
	public AmazonBrowseNode getAncestor() {
		return ancestor;
	}
	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(AmazonBrowseNode ancestor) {
		this.ancestor = ancestor;
	}
	/**
	 * @return the browseNodeId
	 */
	public String getBrowseNodeId() {
		return browseNodeId;
	}
	/**
	 * @param browseNodeId the browseNodeId to set
	 */
	public void setBrowseNodeId(String browseNodeId) {
		this.browseNodeId = browseNodeId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
