package net.latroquette.common.database.data.item;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.item.wish.Wish;
import net.latroquette.common.database.data.keyword.KeywordSource;
import net.latroquette.common.database.data.keyword.MainKeyword;
import net.latroquette.web.beans.item.ViewableItem;

import com.amazon.ECS.client.jax.BrowseNode;
import com.amazon.ECS.client.jax.Item;
import com.amazon.ECS.client.jax.Price;

@XmlRootElement
public class AmazonItem implements ViewableItem, Wish{
	
	private String smallImageUrl;
	private String imageUrl;
	private String sourceUrl;
	private String name;
	private String fullName;
	private Integer id;
	private String amazonId;
	private String description;
	private String formattedPrice;
	private Double price;
	private List<BrowseNode> browseNodes;
	
	public AmazonItem(Item amazonItem){
		smallImageUrl = amazonItem.getSmallImage() != null ? amazonItem.getSmallImage().getURL() : null;
		sourceUrl = amazonItem.getDetailPageURL();
		imageUrl = amazonItem.getLargeImage() != null ? amazonItem.getLargeImage().getURL() : null ;
		name = amazonItem.getItemAttributes().getTitle();
		amazonId = amazonItem.getASIN();
		if(amazonItem.getOfferSummary()  != null){
			Price price = amazonItem.getOfferSummary().getLowestUsedPrice() != null ? 
					amazonItem.getOfferSummary().getLowestUsedPrice() : amazonItem.getOfferSummary().getLowestNewPrice();
			if(price != null){
				formattedPrice = price.getFormattedPrice();
				this.price = price.getAmount().doubleValue() / 100;
			}
		}
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
	public String getSource() {
		return KeywordSource.AMAZON_SOURCE.getSourceId();
	}
	public List<MainKeyword> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the browseNodes
	 */
	@XmlTransient
	public List<BrowseNode> getBrowseNodes() {
		return browseNodes;
	}
	/**
	 * @param browseNodes the browseNodes to set
	 */
	public void setBrowseNodes(List<BrowseNode> browseNodes) {
		this.browseNodes = browseNodes;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the formattedPrice
	 */
	public String getFormattedPrice() {
		return formattedPrice;
	}
	/**
	 * @param formattedPrice the formattedPrice to set
	 */
	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the sourceUrl
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}
	/**
	 * @param sourceUrl the sourceUrl to set
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	
	public String getUid(){
		return getAmazonId();
	}

	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		return this.equals((Wish)o);
	}
	public boolean equals(Wish wish){
		return this.getSource().equals(wish.getSource()) &&
				this.getUid().equals(wish.getUid());
	}
}
