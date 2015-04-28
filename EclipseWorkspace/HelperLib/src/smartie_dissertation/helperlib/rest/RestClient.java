package smartie_dissertation.helperlib.rest;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONException;
import org.json.JSONObject;

import smartie_dissertation.helperlib.log.MyLog;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
 
/**
 * @author Vedran Semenski
 * 
 */
 
public class RestClient {
	
	private Client client;
	private WebResource service;
		
	public RestClient(String restBaseURI){		
		ClientConfig config = new DefaultClientConfig();
		this.client = Client.create(config);
		client.addFilter(new LoggingFilter());
		this.service = client.resource(restBaseURI);
	}

	//TODO add SSL functionality
	//TODO add get,put,delete...
	
	public JSONObject postRequest(String functionName, HashMap<String, String> formData){
		MultivaluedMap<String, String> restFormData = new MultivaluedMapImpl();
//		JSONObject inputJsonObj = new JSONObject();
		for(Map.Entry<String, String> entry : formData.entrySet()){
			restFormData.add(entry.getKey(), entry.getValue());	
//			inputJsonObj.put(entry.getKey(), entry.getValue());	
		}
		JSONObject responseJsonObject = new JSONObject();
		try {			
			responseJsonObject = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, restFormData);
//			if (responseJsonObject.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			} 				
		}catch (Exception e) {
			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}		
		return responseJsonObject;	
	}
	
}