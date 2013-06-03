package net.latroquette.common.database.data.item;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.web.beans.item.ViewableItem;

import com.amazon.ECS.client.jax.BrowseNode;
import com.amazon.ECS.client.jax.Item;

@XmlRootElement
public class AmazonItem implements ViewableItem{
	
	private String smallImageUrl;
	private String imageUrl;
	private String name;
	private String fullName;
	private Integer id;
	private String amazonId;
	private List<BrowseNode> browseNodes;
	
	public AmazonItem(Item amazonItem){
		smallImageUrl = amazonItem.getSmallImage() != null ? amazonItem.getSmallImage().getURL() : null;
		imageUrl = amazonItem.getLargeImage() != null ? amazonItem.getLargeImage().getURL() : null ;
		name = amazonItem.getItemAttributes().getTitle();
		amazonId = amazonItem.getASIN();
		browseNodes = amazonItem.getBrowseNodes() != null ? amazonItem.getBrowseNodes().getBrowseNode() : null;
	}
	/**
	 * @return the smallImageUrl
	 */
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	/**
	 * @param smallImageUrl the smallImageUrl to set
	 */
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	/**
	 * @return the name
	 */
	public String getLabel() {
		return getName();
	}
	/**
	 * @param name the name to set
	 */
	public void setLabel(String name) {
		this.setName(name);
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getValue() {
		return getAmazonId();
	}
	/**
	 * @param id the id to set
	 */
	public void setValue(String id) {
		this.setAmazonId(id);
	}
	/**
	 * @return the amazonId
	 */
	public String getAmazonId() {
		return amazonId;
	}
	/**
	 * @param amazonId the amazonId to set
	 */
	public void setAmazonId(String amazonId) {
		this.amazonId = amazonId;
	}
	@Override
	public int getSource() {
		return ViewableItem.AMAZON_SOURCE;
	}
	@Override
	public List<MainKeyword> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the browseNodes
	 */
	public List<BrowseNode> getBrowseNodes() {
		return browseNodes;
	}
	/**
	 * @param browseNodes the browseNodes to set
	 */
	public void setBrowseNodes(List<BrowseNode> browseNodes) {
		this.browseNodes = browseNodes;
	}

}
