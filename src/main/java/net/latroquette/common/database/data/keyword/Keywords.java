package net.latroquette.common.database.data.keyword;

import java.util.ArrayList;
import java.util.List;

import net.latroquette.common.database.IDatabaseConstants;
import net.latroquette.common.database.data.AbstractDAO;
import net.latroquette.common.database.data.item.AmazonItem;
import net.latroquette.common.database.data.item.Items;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.amazon.ECS.client.jax.BrowseNode;

public class Keywords extends AbstractDAO<MainKeyword> {

	public static final String KEYWORD_ANCESTOR_SEPARATOR = " > ";
	
	public MainKeyword getKeywordById(Integer id){
		return super.getDataObjectById(id, MainKeyword.class);
	}
	public MainKeyword getKeyword(MainKeyword keyword){
		return super.getDataObject(keyword);
	}
	
	public MainKeyword modifyKeyword(MainKeyword keyword){
		return modifyDataObject(keyword);
	}
	
	public boolean deleteKeyword(MainKeyword keyword){
		return deleteDataObject(keyword);
	}
	
	public List<ExternalKeyword> getAmazonKeywordsForTitle(String title){
		List<AmazonItem> amazonItems = Items.searchAmazonItems(null, title);
		List<ExternalKeyword> amazonKeywords = new ArrayList<ExternalKeyword>();
		for(AmazonItem amazonItem : amazonItems){
			if(amazonItem.getBrowseNodes() !=null){
				for(BrowseNode browseNode : amazonItem.getBrowseNodes()){
					ExternalKeyword amazonKeyword= getAmazonKeyword(browseNode);
					if(amazonKeyword == null){
						amazonKeyword = createAmazonKeyword(browseNode);
					}
					if(!amazonKeywords.contains(amazonKeyword)){
						amazonKeywords.add(amazonKeyword);
					}
				}
			}
		}
		//TODO remove commit : just for testing
		commit();
		return amazonKeywords;
	}
	
	public ExternalKeyword getAmazonKeyword(BrowseNode amazonBrowseNode){
		Criteria req = this.session.createCriteria(ExternalKeyword.class)
				.add(Restrictions.eq("uid", amazonBrowseNode.getBrowseNodeId()))
				.add(Restrictions.eq("source", ExternalKeyword.AMAZON_SOURCE))
				.setFetchMode("", FetchMode.SELECT);
		return (ExternalKeyword)req.uniqueResult();
	}
	
	public ExternalKeyword createAmazonKeyword(BrowseNode amazonBrowseNode){
		ExternalKeyword newAmazonKeyWord = new ExternalKeyword();
		newAmazonKeyWord.setUid(amazonBrowseNode.getBrowseNodeId());
		newAmazonKeyWord.setName(amazonBrowseNode.getName());
		newAmazonKeyWord.setSource(ExternalKeyword.AMAZON_SOURCE);
		
		//Search and build hierarchy
		BrowseNode parentNode =  
				(amazonBrowseNode.isIsCategoryRoot() == null || ! amazonBrowseNode.isIsCategoryRoot() ) &&
				amazonBrowseNode.getAncestors() != null && 
				amazonBrowseNode.getAncestors().getBrowseNode() != null &&
				! amazonBrowseNode.getAncestors().getBrowseNode().isEmpty() ? 
					amazonBrowseNode.getAncestors().getBrowseNode().get(0) : null;
		if(parentNode != null){
			
			ExternalKeyword amazonKeyWordAncestor = getAmazonKeyword(parentNode);
			if(amazonKeyWordAncestor == null){
				amazonKeyWordAncestor = createAmazonKeyword(parentNode);
			}
			newAmazonKeyWord.setAncestor(amazonKeyWordAncestor);
			newAmazonKeyWord.setFullname(
					amazonKeyWordAncestor.getFullname().concat(KEYWORD_ANCESTOR_SEPARATOR).concat(newAmazonKeyWord.getName()));
		}else{
			newAmazonKeyWord.setAncestor(null);
			newAmazonKeyWord.setFullname(amazonBrowseNode.getName());
		}
		newAmazonKeyWord.setDatabaseOperation(IDatabaseConstants.INSERT);
		persist(newAmazonKeyWord);
		return newAmazonKeyWord;
	}
}
