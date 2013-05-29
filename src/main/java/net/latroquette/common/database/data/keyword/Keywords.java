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

public class Keywords extends AbstractDAO<Keyword> {

	public static final String KEYWORD_ANCESTOR_SEPARATOR = " > ";
	
	public Keyword getKeywordById(Integer id){
		return super.getDataObjectById(id, Keyword.class);
	}
	public Keyword getKeyword(Keyword keyword){
		return super.getDataObject(keyword);
	}
	
	public Keyword modifyKeyword(Keyword keyword){
		return modifyDataObject(keyword);
	}
	
	public boolean deleteKeyword(Keyword keyword){
		return deleteDataObject(keyword);
	}
	
	public List<AmazonKeyword> getAmazonKeywordsForTitle(String title){
		List<AmazonItem> amazonItems = Items.searchAmazonItems(null, title);
		List<AmazonKeyword> amazonKeywords = new ArrayList<AmazonKeyword>();
		for(AmazonItem amazonItem : amazonItems){
			if(amazonItem.getBrowseNodes() !=null){
				for(BrowseNode browseNode : amazonItem.getBrowseNodes()){
					AmazonKeyword amazonKeyword= getAmazonKeyword(browseNode);
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
	
	public AmazonKeyword getAmazonKeyword(BrowseNode amazonBrowseNode){
		Criteria req = this.session.createCriteria(AmazonKeyword.class)
				.add(Restrictions.eq("id", amazonBrowseNode.getBrowseNodeId()))
				.setFetchMode("", FetchMode.SELECT);
		return (AmazonKeyword)req.uniqueResult();
	}
	
	public AmazonKeyword createAmazonKeyword(BrowseNode amazonBrowseNode){
		AmazonKeyword newAmazonKeyWord = new AmazonKeyword();
		newAmazonKeyWord.setId(amazonBrowseNode.getBrowseNodeId());
		newAmazonKeyWord.setName(amazonBrowseNode.getName());
		
		//Search and build hierarchy
		BrowseNode parentNode =  
				(amazonBrowseNode.isIsCategoryRoot() == null || ! amazonBrowseNode.isIsCategoryRoot() ) &&
				amazonBrowseNode.getAncestors() != null && 
				amazonBrowseNode.getAncestors().getBrowseNode() != null &&
				! amazonBrowseNode.getAncestors().getBrowseNode().isEmpty() ? 
					amazonBrowseNode.getAncestors().getBrowseNode().get(0) : null;
		if(parentNode != null){
			
			AmazonKeyword amazonKeyWordAncestor = getAmazonKeyword(parentNode);
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
