package net.latroquette.service.amazon;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import net.latroquette.common.util.parameters.ParameterName;
import net.latroquette.common.util.parameters.Parameters;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.amazon.ECS.client.jax.AWSECommerceService;
import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemLookup;
import com.amazon.ECS.client.jax.ItemLookupRequest;
import com.amazon.ECS.client.jax.ItemSearch;
import com.amazon.ECS.client.jax.ItemSearchRequest;
import com.amazon.ECS.client.jax.OperationRequest;
import com.amazon.ECS.client.jax.tools.AwsHandlerResolver;

@Service
@Scope(value="singleton")
public class AmazonWService {
	
	
	private static final Logger logger = LoggerFactory
			.getLogger(AmazonWService.class);
	
	public static final String AWS_ACCESS_KEY = "AKIAJI5JPPYSQLRZXSNA"; 
	public static final String AWS_SECRET_KEY= "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" ;
	public static final String AWS_ASSOCIATE_TAG= "latronet-21" ;
	private static final List<String> AVAILABLE_CATEGORY = Arrays.asList( 
			"all","apparel","appliances","baby","beauty","blended",
		    "books","classical","dvd","electronics","boreignbooks",
		    "healthpersonalcare","homeimprovement","jewelry","kindlestore",
		    "kitchen","lighting","marketplace","mp3downloads","music",
		    "musicalinstruments","musictracks","officeproducts",
		    "petsupplies","pchardware","Shoes","software",
		    "softwarevideogames","sportinggoods","toys"
    );
	@Autowired
	private AWSECommerceService service ;
	@Autowired
	private Parameters parameters;
	
	private String amazonAccessKey;
	
	public AmazonWService(){}
	
	/**
	 * @param service the service to set
	 */
	public void setService(AWSECommerceService service) {
		this.service = service;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	@PostConstruct
	public void initializeWebService(){
		setService(new AWSECommerceService());
		String awsSecretKey = parameters.getStringValue(ParameterName.AMAZON_WS_SECRET_KEY);
		amazonAccessKey = parameters.getStringValue(ParameterName.AMAZON_WS_ACCESS_KEY);
		service.setHandlerResolver(new AwsHandlerResolver(awsSecretKey));
	}

	public AWSECommerceServicePortType getPort(){
		return service.getAWSECommerceServicePortFR();
	}
	
	public List<com.amazon.ECS.client.jax.Items> itemSearch(AWSECommerceServicePortType port, ItemSearchRequest itemSearch){
		ItemSearch request = new ItemSearch();
		request.setAWSAccessKeyId(amazonAccessKey);
		request.getRequest().add(itemSearch);
		
		Holder<OperationRequest> operationRequest = new Holder<OperationRequest>(); 
		Holder<List<com.amazon.ECS.client.jax.Items>> items = new Holder<java.util.List<com.amazon.ECS.client.jax.Items>> ();
		
		try{
			logger.debug("AWS request : [{}] keywords : {} ", itemSearch.getSearchIndex(), itemSearch.getKeywords() );
			port.itemSearch("", amazonAccessKey, AWS_ASSOCIATE_TAG, "", "", 
					itemSearch, request.getRequest(),operationRequest, items);
		}catch(WebServiceException e){
			throw e;
		}
		
		return items.value;
	}
	public List<com.amazon.ECS.client.jax.Items> itemLookup(AWSECommerceServicePortType port, ItemLookupRequest itemSearch){
		ItemLookup request = new ItemLookup();
		request.setAWSAccessKeyId(amazonAccessKey);
		request.getRequest().add(itemSearch);
		
		Holder<OperationRequest> operationRequest = new Holder<OperationRequest>(); 
		Holder<List<com.amazon.ECS.client.jax.Items>> items = new Holder<java.util.List<com.amazon.ECS.client.jax.Items>> ();
		
		try{
			logger.debug("AWS request : [{}] keywords : {} ", itemSearch.getSearchIndex(), itemSearch.getItemId() );
			port.itemLookup("", amazonAccessKey, AWS_ASSOCIATE_TAG, "", "", 
					itemSearch, request.getRequest(),operationRequest, items);
		}catch(WebServiceException e){
			throw e;
		}
		
		return items.value;
	}
	
	public static boolean isValideCategory(String cat){
		if(StringUtils.isEmpty(cat)){
			return false;
		}
		return AVAILABLE_CATEGORY.contains(cat.toLowerCase());
	}
}
