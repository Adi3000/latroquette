package net.latroquette.service.amazon;

import java.util.Arrays;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.lang.StringUtils;

import com.amazon.ECS.client.jax.AWSECommerceService;
import com.amazon.ECS.client.jax.AWSECommerceServicePortType;
import com.amazon.ECS.client.jax.ItemSearch;
import com.amazon.ECS.client.jax.ItemSearchRequest;
import com.amazon.ECS.client.jax.OperationRequest;
import com.amazon.ECS.client.jax.tools.AwsHandlerResolver;
import com.sun.xml.internal.ws.client.ClientTransportException;

public class AmazonWServiceClient {
	
	public static final String AWS_ACCESS_KEY = "AKIAJI5JPPYSQLRZXSNA"; 
	public static final String AWS_SECRET_KEY= "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" ;
	public static final AmazonWServiceClient CLIENT = new AmazonWServiceClient();
	public static final String AWS_ASSOCIATE_TAG= "latronet-21" ;
	private static final String AWS_END_POINT= "http://webservices.amazon.com/onca/xml" ;
	private static final String AWS_SERVICE_VERSION= "2011-08-01" ;
	private static final List<String> AVAILABLE_CATEGORY = Arrays.asList( 
			"all","apparel","appliances","baby","beauty","blended",
		    "books","classical","dvd","electronics","boreignbooks",
		    "healthpersonalcare","homeimprovement","jewelry","kindlestore",
		    "kitchen","lighting","marketplace","mp3downloads","music",
		    "musicalinstruments","musictracks","officeproducts",
		    "petsupplies","pchardware","Shoes","software",
		    "softwarevideogames","sportinggoods","toys"
    );
	
	private AWSECommerceService service ; 
	
	private AmazonWServiceClient(){
		service = new AWSECommerceService();
		service.setHandlerResolver(new AwsHandlerResolver(AWS_SECRET_KEY));
		
	}

	public AWSECommerceServicePortType getPort(){
		return service.getAWSECommerceServicePortFR();
	}
	
	public static List<com.amazon.ECS.client.jax.Items> itemSearch(AWSECommerceServicePortType port, ItemSearchRequest itemSearch){
		ItemSearch request = new ItemSearch();
		request.setAWSAccessKeyId(AWS_ACCESS_KEY);
		request.getRequest().add(itemSearch);
		
		Holder<OperationRequest> operationRequest = new Holder<OperationRequest>(); 
		Holder<List<com.amazon.ECS.client.jax.Items>> items = new Holder<java.util.List<com.amazon.ECS.client.jax.Items>> ();
		
		try{
			port.itemSearch("", AWS_ACCESS_KEY, AWS_ASSOCIATE_TAG, "", "", 
					itemSearch, request.getRequest(),operationRequest, items);
		}catch(ClientTransportException e){
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
