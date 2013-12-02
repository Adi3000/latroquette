package net.latroquette.rest.forum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMFWSClientUtil {
	public static final String SMF_ENDPOINT = "http://localhost/forum/rest/smf";
	private static final Logger logger = LoggerFactory.getLogger(SMFWSClientUtil.class);
	private static final String secretKey = "1";
	private static final String errorFlag = "error";
	
	private SMFWSClientUtil(){}
	public static JSONObject sendRequestPostQuery(SMFMethods method, List<NameValuePair> postParams) throws SMFRestException{
		HttpPost endpoint = new HttpPost(SMF_ENDPOINT + method);
		HttpClient client = new DefaultHttpClient();
		postParams.add(new BasicNameValuePair("secretKey", secretKey));
		endpoint.setHeader("Accept", MediaType.APPLICATION_JSON);
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(postParams,StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			logger.error("Can't use specified encodding " + StandardCharsets.UTF_8.name(), e);
			throw new IllegalCharsetNameException("Can't use specified encodding " + StandardCharsets.UTF_8.name());
		}
		entity.setContentEncoding(StandardCharsets.UTF_8.name());
		endpoint.setEntity(entity);
		String responseData = null;
		try {
		    final HttpResponse response = client.execute(endpoint);
		    responseData = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
		} catch(IOException e) {
		    logger.error("Can't parse server response !", e);
		    throw new SMFRestException("Can't parse server response !",e);
	    }
		JSONObject jsonResult = null;
		try {
			jsonResult = new JSONObject(responseData);
			if(jsonResult.get(errorFlag) != null &&  !JSONObject.NULL.equals(jsonResult.get("error"))){
				throw new SMFRestException("\n"+ responseData +"\nSimple Machine Forum send back an error message : " + jsonResult.get("error"));
			}
		} catch (JSONException e) {
			logger.error("Can't parse server response to JSon ! : \n" + responseData, e);
			throw new SMFRestException("Can't parse server response !\n " + responseData,e);
		}
		return jsonResult;
	}
	
}
